<!DOCTYPE html PUBLIC
          "-//W3C//DTD XHTML 1.0 Transitional//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include.jsp"  %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html>
<head>
<tiles:insertAttribute name="head" />
<script src="<c:url value="/"/>/resources/js/bootstrap.min.js"></script>

</head>

<body >
<div id="container">
<tiles:insertAttribute name="topnav" />

    
    <tiles:insertAttribute name="topnav" />

    
    <div class="row-fluid" id="banner">
    <div class="span12">
             <tiles:insertAttribute name="branding" />
             </div>
    </div>

    <div class="container-fluid">
<div class="row-fluid" >

<div class="span12"  id="bodyContainer">

<tiles:insertAttribute name="body" />
</div>
</div>


<div  id="footer">
 <div class="row-fluid">
 
<div class="span12">
<tiles:insertAttribute name="footer" />
</div>
</div>
</div>

</div>
</div>


</body>
</html>