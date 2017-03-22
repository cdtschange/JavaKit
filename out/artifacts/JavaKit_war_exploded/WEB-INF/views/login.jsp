<%--
  Created by IntelliJ IDEA.
  User: cdts
  Date: 22/03/2017
  Time: 9:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form method="post" action="login">
    Username:<input type="text" name="username"/>
    Password:<input type="password" name="password"/>
    <input type='submit' value='Login'>

</form>
<br/>
<span>${result}</span>
</body>
</html>
