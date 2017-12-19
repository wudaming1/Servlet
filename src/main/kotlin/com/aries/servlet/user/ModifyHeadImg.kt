package com.aries.servlet.user

import com.aries.servlet.ModifyServletRequestWrapper
import com.aries.servlet.base.BaseServlet
import com.aries.servlet.bean.HttpResultCode
import com.aries.servlet.bean.ResponseBean
import com.aries.servlet.utils.JsonUtil
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import java.io.File
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ModifyHeadImg : BaseServlet() {

    //头像图片最大不应该超过1M
    private val maxFileSize = 1 * 1024 * 1024L
    private val maxMenSize = 10 * 1024


    private lateinit var parentPath: String

    override fun init(config: ServletConfig?) {
        super.init(config)
        parentPath = config?.getInitParameter("path") ?: ""
    }

    //上传，修改头像。
    override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doPut(req, resp)
        val responseBean = ResponseBean()
        if (parentPath.isEmpty()) {
            responseBean.resultCode = HttpResultCode.FAIL
            responseBean.message = "存储地址初始化错误"
        }
        val id = (req as ModifyServletRequestWrapper).userId
        val path = "$parentPath/$id/info/portrait.jpg"
        val file = File(path)
        if (file.exists()) {
            file.createNewFile()
        } else {
            val parentFile = file.parentFile
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            file.createNewFile()
        }

        //检查有一个文件上传请求
        if (ServletFileUpload.isMultipartContent(req)) {
            val factory = DiskFileItemFactory()
            factory.sizeThreshold = maxMenSize// 超过10k的数据采用临时文件缓存
            factory.repository = File("$parentPath/temp")

            val upload = ServletFileUpload(factory)
            //所有文件总t最大大小
            upload.fileSizeMax = maxFileSize
            //单个文件最大大小
            upload.fileSizeMax = maxFileSize

            try {
                val fileItems = upload.parseRequest(req)
                fileItems[0].write(file)
                responseBean.resultCode = HttpResultCode.SUCCESS
            } catch (e: Exception) {
                e.printStackTrace()
                responseBean.resultCode = HttpResultCode.FAIL
                responseBean.message = e.message?:""
            }


        }else{
            responseBean.resultCode = HttpResultCode.FAIL
            responseBean.message = "未接收到图片"
        }
        resp.writer.println(JsonUtil.writeValueAsString(responseBean))

    }
}