<%--
  Created by IntelliJ IDEA.
  User: acer
  Date: 2018/3/21
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
        pageEncoding="utf-8" isELIgnored="false" import="java.util.*" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<%--<script>
    $(function () {
       ${"#addForm"}.submit(function () {
            if(checkEmpty("name","属性名称"))
                return true;
            return false;
        })
    });
</script>--%>

<script>
    $(function () {
       $("#addForm").submit(function () {
           /*js函数，为空返回FALSE*/
           if(checkEmpty("name","属性名称"))
               return true;
           return false;
       });
    });
</script>

<title>属性管理</title>

<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li><a href="admin_category_list?cid=${cid}">${c.name}</a></li>
        <li class="active">属性管理</li>
    </ol>

    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
                <tr class="success">
                    <td>ID</td>
                    <td>属性名称</td>
                    <td>编辑</td>
                    <td>删除</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${ps}" var="p">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td><a href="admin_property_edit?id=${p.id}"><span class="glyphicon glyphicon-edit">
                                </span> </a> </td>
                        <td><a href="admin_property_delete?id=${p.id}"><span class="glyphicon glyphicon-trash">
                                </span> </a> </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <%--分页--%>
    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>

    <%--新增属性--%>
    <div class="panel panel-warning addDiv">
        <div class="panel-heading">新增属性</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_property_add">
                <table class="addTable">
                    <tr>
                        <td>属性名称</td>
                        <td><input type="text" id="name" name="name" class="form-control"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="cid" value="${c.id}">
                            <%--不用pid因为，新增属性的时候，只需要知道cid，pid为数据库自增的--%>
                            <%--<input type="hidden" name="pid" value="${p.id}">--%>
                            <button type="submit" class="btn btn-success" >提T交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>

<%@include file="../include/admin/adminFooter.jsp"%>
