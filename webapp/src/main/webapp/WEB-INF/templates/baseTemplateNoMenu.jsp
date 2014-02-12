<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include.jsp"  %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>


<html>
<head>
<tiles:insertAttribute name="head" />

</head>

<body>

<div id="roof"><tiles:insertAttribute name="roof" /></div>

<div id="content">
<tiles:insertAttribute name="header" />

<div id="centerCol">
<tiles:insertAttribute name="body" />

<div id="footer">
<tiles:insertAttribute name="footer" />
</div>


</div>
</div>

</body>
</html>
