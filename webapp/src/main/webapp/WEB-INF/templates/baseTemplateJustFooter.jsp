<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%><%@ include file="/WEB-INF/include.jsp"  %><%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%><html>
<head>
<tiles:insertAttribute name="head" />
</head>
<body >
<div class="container_12">
<div class="grid_12">
<tiles:insertAttribute name="body" />
</div>
<div class="clear"></div>
<div class="grid_12">
<tiles:insertAttribute name="footer" />
</div>
<div class="clear"></div>
</div>
</body>
</html>

