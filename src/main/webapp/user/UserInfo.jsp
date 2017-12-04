<%@ page import="com.aries.servlet.bean.UserBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人信息</title>
</head>
<body>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
userName : <%= user.getUserName()%>
password : <%= user.getPassword()%>
</body>
</html>