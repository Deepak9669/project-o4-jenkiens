<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.controller.AccountListCtl"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.bean.AccountBean"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Account List</title>
</head>
<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.AccountBean"
		scope="request"></jsp:useBean>

	<div align="center">

		<h1 align="center" style="margin-bottom: -15; color: navy;">
			Account List
		</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>

		<form action="<%=ORSView.ACCOUNT_LIST_CTL%>" method="post">

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;
				int nextListSize =
						DataUtility.getInt(request.getAttribute("nextListSize").toString());

				HashMap<String, String> map =
						(HashMap<String, String>) request.getAttribute("map");

				List<AccountBean> list =
						(List<AccountBean>) ServletUtility.getList(request);

				Iterator<AccountBean> it = list.iterator();

				if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>">
			<input type="hidden" name="pageSize" value="<%=pageSize%>">

			<!-- ================= SEARCH PANEL ================= -->

			<table style="width: 100%">
				<tr>
					<td align="center">

						<label><b>Account No :</b></label>
						<input type="text" name="accountNo"
							placeholder="Enter Account No"
							value="<%=ServletUtility.getParameter("accountNo", request)%>">
						&emsp;

						<label><b>Bank Name :</b></label>
						<input type="text" name="bankName"
							placeholder="Enter Bank Name"
							value="<%=ServletUtility.getParameter("bankName", request)%>">
						&emsp;

						<label><b>Account Type :</b></label>
						<%=HTMLUtility.getList("accountType",
								String.valueOf(bean.getAccountType()), map)%>
						&emsp;

						<input type="submit" name="operation"
							value="<%=AccountListCtl.OP_SEARCH%>">
						&nbsp;
						<input type="submit" name="operation"
							value="<%=AccountListCtl.OP_RESET%>">

					</td>
				</tr>
			</table>

			<br>

			<!-- ================= LIST TABLE ================= -->

			<table border="1" style="width: 100%; border: groove;">
				<tr style="background-color: #e1e6f1e3;">
					<th width="5%"><input type="checkbox" id="selectall" /></th>
					<th width="5%">S.No</th>
					<th width="20%">Account No</th>
					<th width="20%">Account Type</th>
					<th width="20%">Bank Name</th>
					<th width="15%">Balance</th>
					<th width="5%">Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
						bean = it.next();
				%>

				<tr>
					<td style="text-align: center;">
						<input type="checkbox" name="ids" value="<%=bean.getId()%>">
					</td>

					<td style="text-align: center;"><%=index++%></td>
					<td style="text-align: center;"><%=bean.getAccountNo()%></td>
					<td style="text-align: center;"><%=bean.getAccountType()%></td>
					<td style="text-align: center;"><%=bean.getBankName()%></td>
					<td style="text-align: center;"><%=bean.getBalance()%></td>

					<td style="text-align: center;">
						<a href="AccountCtl?id=<%=bean.getId()%>">Edit</a>
					</td>
				</tr>

				<%
					}
				%>
			</table>

			<!-- ================= PAGINATION ================= -->

			<table style="width: 100%">
				<tr>
					<td style="width: 25%">
						<input type="submit" name="operation"
							value="<%=AccountListCtl.OP_PREVIOUS%>"
							<%=pageNo > 1 ? "" : "disabled"%>>
					</td>

					<td align="center" style="width: 25%">
						<input type="submit" name="operation"
							value="<%=AccountListCtl.OP_NEW%>">
					</td>

					<td align="center" style="width: 25%">
						<input type="submit" name="operation"
							value="<%=AccountListCtl.OP_DELETE%>">
					</td>

					<td align="right" style="width: 25%">
						<input type="submit" name="operation"
							value="<%=AccountListCtl.OP_NEXT%>"
							<%=nextListSize != 0 ? "" : "disabled"%>>
					</td>
				</tr>
			</table>

			<%
				} else {
			%>

			<table>
				<tr>
					<td align="right">
						<input type="submit" name="operation"
							value="<%=AccountListCtl.OP_BACK%>">
					</td>
				</tr>
			</table>

			<%
				}
			%>

		</form>
	</div>

</body>
</html>