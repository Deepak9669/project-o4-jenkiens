<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.DataValidator"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="in.co.rays.proj4.controller.LoginCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="Header.jsp"%>
	<form action="<%=ORSView.LOGIN_CTL%>" method="post">
		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.UserBean"
			scope="request"></jsp:useBean>
		<div align="center">
			<h1>Login</h1>
			<div style="height: 15px; margin-bottom: 12px">
				<H3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</H3>
				<H3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</H3>
			</div>

			<table>
				<tr>
					<th>Login Id:</th>
					<td><input type="text" name="login"
						placeholder="Enter Login Id"
						value="<%=DataUtility.getStringData(bean.getLogin())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("login", request)%></font></td>

				</tr>
				<tr>
					<th>password</th>
					<td><input type="password" name="password"
						placeholder="Enter Password"
						value="<%=DataUtility.getStringData(bean.getPassword())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("password", request)%></font></td>
				</tr>

				<tr>
					<th></th>

					<td><input type="submit" name="operation"
						value="<%=LoginCtl.OP_SIGN_IN%>"> <input type="submit"
						name="operation" value="<%=LoginCtl.OP_SIGN_UP%>"></td>

				</tr>


			</table>
			<a href="<%=ORSView.FORGET_PASSWORD_CTL%>"><b>Forget My Password
			</b></a>
		</div>
	</form>
	<%@ include file="Footer.jsp"%>
</body>
</html>