<%--
  Created by IntelliJ IDEA.
  User: wudaming
  Date: 2017/12/4
  Time: 下午5:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<sql:query var="rs" dataSource="jdbc/TestDB">
    select user_id, user_name, password from app_user
</sql:query>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Results</h2>

<c:forEach var="row" items="${rs.rows}">
    Foo ${row.user_name}<br/>
    Bar ${row.password}<br/>
</c:forEach>

</body>
</html>
