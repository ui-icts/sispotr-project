<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Site Content List</h1>

<div  class="row-fluid">
<div  class="span11">
<div class="btn-group"><a class="btn" href="add.html">Add</a>
<a class="btn" href="repopulate.html">refresh</a> 
</div>

<table class="table">
<thead>
<tr>
<th>Label</th>
<th>Page</th>
<th></th>
</tr>
</thead>
<tbody>
<c:forEach items="${siteContentList}" var="siteContent"  varStatus="status">
<tr>
<td>
<a href="edit.html?siteContentId=${siteContent.siteContentId}">${siteContent.label}</a>
</td>
<td>
<a href="<c:url value="/"/>${siteContent.page}" >${siteContent.page}</a>
</td>
<td>
<div class="btn-group">
<a class="btn" href="edit.html?siteContentId=${siteContent.siteContentId}">edit</a> 
<a class="btn" href="show.html?siteContentId=${siteContent.siteContentId}">view</a> 
<a class="btn" href="delete.html?siteContentId=${siteContent.siteContentId}">delete</a>
</div>
</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>
</div>