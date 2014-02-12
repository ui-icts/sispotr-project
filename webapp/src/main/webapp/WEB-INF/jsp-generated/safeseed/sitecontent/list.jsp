<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SiteContent List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SiteContentId</th>
 <th>Label</th>
 <th>Content</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${siteContentList}" var="siteContent"  varStatus="status"><tr>
<td><a href="edit.html?siteContentId=${siteContent.siteContentId}">${siteContent.siteContentId}</a></td>
<td>${siteContent.label}</td>
<td>${siteContent.content}</td>
<td><a href="edit.html?siteContentId=${siteContent.siteContentId}">edit</a> <a href="show.html?siteContentId=${siteContent.siteContentId}">view</a> <a href="delete.html?siteContentId=${siteContent.siteContentId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
