<%--
  Created by IntelliJ IDEA.
  User: acer
  Date: 2018/3/24
  Time: 14:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
            pageEncoding="UTF-8" isELIgnored="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<title>产品编辑</title>

<script>
    $(function() {
        $("#editForm").submit(function() {
            if (!checkEmpty("name", "产品名称"))
                return false;
//          if (!checkEmpty("subTitle", "小标题"))
//              return false;
            if (!checkNumber("orignalPrice", "原价格"))
                return false;
            if (!checkNumber("promotePrice", "优惠价格"))
                return false;
            if (!checkInt("stock", "库存"))
                return false;
            return true;
        });
    });
</script>


<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a> </li>
        <li><a href="admin_category_list?cid=${p.category.id}">${p.category.name}</a> </li>
        <li><a href="admin_product_list?pid=${p.id}">${p.name}</a> </li>
        <li class="active">编辑产品</li>
    </ol>

    <div class="panel panel-warning editDiv">
        <div class="panel-heading">编辑产品</div>
        <div class="panel-body">
            <form method="post" id="editForm" action="admin_product_update">
                <table class="editTable">
                    <tr>
                        <td>产品名称</td>
                        <td><input type="text" id="name" name="name" value="${p.name}"
                                   class="form-control"></td>
                    </tr>
                    <tr>
                        <td>产品小标题</td>
                        <td><input type="text" id="subTitle" name="subTitle" value="${p.subTitle}"
                                    class="form-control"></td>
                    </tr>

                    <tr>
                        <td>原价格</td>
                        <td><input type="text" id="orignalPrice" name="orignalPrice"
                                   value="${p.orignalPrice}" class="form-control"></td>
                    </tr>

                    <tr>
                        <td>优惠价格</td>
                        <td><input type="text" name="promotePrice" id="promotePrice"
                                    value="${p.promotePrice}" class="form-control">
                        </td>
                    </tr>
                    <tr>
                        <td>库存</td>
                        <td><input type="text" id="stock" name="stock" value="${p.stock}"
                                    class="form-control"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="id" value="${p.id}">
                            <input type="hidden" name="cid" value="${p.category.id}">
                            <button type="submit" class="btn btn-success">修改</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>