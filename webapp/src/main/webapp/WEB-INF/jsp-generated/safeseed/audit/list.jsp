<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Audit List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>AuditId</th>
 <th>EventType</th>
 <th>Description</th>
 <th>TimeRecorded</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${auditList}" var="audit"  varStatus="status"><tr>
<td><a href="edit.html?auditId=${audit.auditId}">${audit.auditId}</a></td>
<td>${audit.eventType}</td>
<td>${audit.description}</td>
<td>${audit.timeRecorded}</td>
<td><a href="edit.html?auditId=${audit.auditId}">edit</a> <a href="show.html?auditId=${audit.auditId}">view</a> <a href="delete.html?auditId=${audit.auditId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
