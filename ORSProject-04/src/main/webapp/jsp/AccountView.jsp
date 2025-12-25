<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.controller.AccountCtl"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.bean.AccountBean"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Account</title>
</head>

<body>

<%@ include file="Header.jsp"%>

<form action="<%=ORSView.ACCOUNT_CTL%>" method="post">

    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.AccountBean"
        scope="request"></jsp:useBean>

    <%
        HashMap<String, String> map =
            (HashMap<String, String>) request.getAttribute("map");
    %>

    <div align="center">

        <h1 style="margin-bottom: -15; color: navy">
            <%
                if (bean != null && bean.getId() > 0) {
            %>
                Update
            <%
                } else {
            %>
                Add
            <%
                }
            %>
            Account
        </h1>

        <!-- Success / Error -->
        <div style="height: 15px; margin-bottom: 12px">
            <h3 align="center">
                <font color="green">
                    <%=ServletUtility.getSuccessMessage(request)%>
                </font>
            </h3>
            <h3 align="center">
                <font color="red">
                    <%=ServletUtility.getErrorMessage(request)%>
                </font>
            </h3>
        </div>

        <!-- Hidden Fields (FIXED) -->
        <input type="hidden" name="id" value="<%=bean.getId()%>">
        <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
        <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
        <input type="hidden" name="createdDatetime"
            value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
        <input type="hidden" name="modifiedDatetime"
            value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

        <table>

            <!-- Account No -->
            <tr>
                <th align="left">Account No <span style="color:red">*</span></th>
                <td>
                    <input type="text" name="accountNo"
                        placeholder="Enter Account No"
                        value="<%=DataUtility.getStringData(bean.getAccountNo())%>">
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("accountNo", request)%>
                    </font>
                </td>
            </tr>

            <!-- Account Type (PRELOAD FIX) -->
            <tr>
                <th align="left">Account Type <span style="color:red">*</span></th>
                <td>
                    <%=HTMLUtility.getList("accountType",
                            bean.getAccountType(), map)%>
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("accountType", request)%>
                    </font>
                </td>
            </tr>

            <!-- Bank Name -->
            <tr>
                <th align="left">Bank Name <span style="color:red">*</span></th>
                <td>
                    <input type="text" name="bankName"
                        placeholder="Enter Bank Name"
                        value="<%=DataUtility.getStringData(bean.getBankName())%>">
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("bankName", request)%>
                    </font>
                </td>
            </tr>

            <!-- Balance -->
            <tr>
                <th align="left">Balance <span style="color:red">*</span></th>
                <td>
                    <input type="text" name="balance"
                        placeholder="Enter Balance"
                        value="<%=DataUtility.getStringData(bean.getBalance())%>">
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("balance", request)%>
                    </font>
                </td>
            </tr>

            <!-- Buttons -->
            <tr>
                <th></th>
                <%
                    if (bean != null && bean.getId() > 0) {
                %>
                <td colspan="2">
                    <input type="submit" name="operation"
                        value="<%=AccountCtl.OP_UPDATE%>">
                    <input type="submit" name="operation"
                        value="<%=AccountCtl.OP_CANCEL%>">
                </td>
                <%
                    } else {
                %>
                <td colspan="2">
                    <input type="submit" name="operation"
                        value="<%=AccountCtl.OP_SAVE%>">
                    <input type="submit" name="operation"
                        value="<%=AccountCtl.OP_RESET%>">
                </td>
                <%
                    }
                %>
            </tr>

        </table>

    </div>

</form>

</body>
</html>