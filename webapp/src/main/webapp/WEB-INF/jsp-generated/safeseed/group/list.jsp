<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Group List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>GroupId</th>
 <th>Name</th>
 <th>Description</th>
 <th>GroupSeqs</th>
 <th>PersonGroups</th>
 <th>GroupGlobalSeqs</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${groupList}" var="group"  varStatus="status"><tr>
<td><a href="edit.html?groupId=${group.groupId}">${group.groupId}</a></td>
<td>${group.name}</td>
<td>${group.description}</td>
<td>groupSeqs</td>
<td>personGroups</td>
<td>groupGlobalSeqs</td>
<td><a href="edit.html?groupId=${group.groupId}">edit</a> <a href="show.html?groupId=${group.groupId}">view</a> <a href="delete.html?groupId=${group.groupId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
