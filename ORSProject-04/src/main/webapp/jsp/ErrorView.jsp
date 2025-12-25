<%@ page isErrorPage="true" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Error</title>
</head>
<body>

<br><br><br>

<div align="center">
    <h1>Oops! Something went wrong.</h1>
    <font style="color:red; font-size:20px;">
        Please try again or contact administrator
    </font>
</div>

<br>

<h4 align="center">
    <a href="<%= request.getContextPath() %>/WelcomeCtl"
       style="color:deepskyblue;">
        *Click here to Go Back*
    </a>
</h4>

</body>
</html>
