<%--
  Created by IntelliJ IDEA.
  User: acer
  Date: 2018/3/24
  Time: 10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
               pageEncoding="utf-8" isELIgnored="false" import="java.util.*" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>


<script>
    $(function () {
       $("#addForm").submit(function () {
           if(!checkEmpty("name","产品名称"))
               return false;
           if(!checkEmpty("orignalPrice","原价格"))
               return false;
           if(!checkEmpty("promotePrice","优惠价格"))
               return false;
           if(!checkEmpty("stock","库存数量"))
               return false;
           return true;
       });
    });
</script>


<title>产品管理</title>

<div class="workingArea">

    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li><a href="admin_category_list?cid=${c.id}">${c.name}</a> </li>
        <li>产品管理</li>
    </ol>

    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
                <tr class="success">
                    <th>ID</th>
                    <th>图片</th>
                    <th>产品名称</th>
                    <th>产品小标题</th>
                    <th width="53px">原价格</th>
                    <th width="80px">优惠价格</th>
                    <th width="80px">库存数量</th>
                    <th width="80px">图片管理</th>
                    <th width="80px">设置属性</th>
                    <th width="42px">编辑</th>
                    <th width="42px">删除</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${ps}" var="p">
                    <tr>
                        <td>${p.id}</td>
                        <td><c:if test="${!empty p.firstProductImage}">
                            <img width="40px" src="img/productSigle/${p.firstProductImage.id}.jpg"></c:if> </td>
                        <td>${p.name}</td>
                        <td>${p.subTitle}</td>
                        <td>${p.orignalPrice}</td>
                        <td>${p.promotePrice}</td>
                        <td>${p.stock}</td>
                        <td><a href="admin_productImage_list?pid=${p.id}">
                                <span class="glyphicon glyphicon-picture"></span></a> </td>
                        <td><a href="admin_product_editPropertyValue?id=${p.id}">
                                <span class="glyphicon glyphicon-th-list"></span></a> </td>
                        <%--这里为什么是id=,而不是pid--%>
                        <%--因为ProductServlet中使用name为"id"--%>
                        <td><a href="admin_product_edit?id=${p.id}">
                                <span class="glyphicon glyphicon-edit"></span> </a> </td>
                        <td><a deleteLink="true" href="admin_product_delete?id=${p.id}">
                                <span class="glyphicon glyphicon-trash"></span> </a> </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>

    <div class="panel panel-warning addDiv">
        <div class="panel-heading">新增产品</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_product_add">
                <table class="addTable">
                    <tr>
                        <td>产品名称</td>
                        <td><input type="text" name="name" id="name" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>产品小标题</td>
                        <td><input type="text" name="subTitle" id="subTitle"
                                   class="form-control"></td>
                    </tr>
                    <tr>
                        <td>原价格</td>
                        <td><input type="text" name="orignalPrice" id="orignalPrice"
                                   value="99.9" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>优惠价格</td>
                        <td><input type="text" name="promotePrice" id="promotePrice"
                                   value="19.9" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>库存</td>
                        <td><input type="text" name="stock" id="stock" value="9"
                                    class="form-control"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <%--这里name="cid",而不是id="cid",id是用来JS代码的，name是用来param串参数的--%>
                            <input type="hidden" name="cid" value="${c.id}">
                            <button type="submit" class="button button-success">提P交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>

<%@include file="../include/admin/adminFooter.jsp"%>
