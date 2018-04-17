<%--
  Created by IntelliJ IDEA.
  User: acer
  Date: 2018/3/26
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
            pageEncoding="UTF-8" isELIgnored="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<title>用户管理</title>

<div class="workingArea">
    <h1 class="label label-info">用户管理</h1>
    <br><br>

    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
              <tr class="success">
                <th>ID</th>
                <th>用户管理</th>
              </tr>
            </thead>
            <tbody>
                <c:forEach items="${us}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.name}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>
</div>

<%@include file="../include/admin/adminFooter.jsp"%>