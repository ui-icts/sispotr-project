<%@ include file="/WEB-INF/include.jsp"  %>
<h1>PersonGroup List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>PersonGroupId</th>
 <th>Role</th>
 <th>Group</th>
 <th>Person</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${personGroupList}" var="personGroup"  varStatus="status"><tr>
<td><a href="edit.html?personId=${personGroup.id.personId}&groupId=${personGroup.id.groupId}">(personId,${personGroup.id.personId})(groupId,${personGroup.id.groupId})</a></td> 
<td>${personGroup.role}</td>
<td>${personGroup.group.groupId}</td>
<td>${personGroup.person.personId}</td>
<td><a href="edit.html?personId=${personGroup.id.personId}&groupId=${personGroup.id.groupId}">edit</a> <a href="show.html?personId=${personGroup.id.personId}&groupId=${personGroup.id.groupId}">view</a> <a href="delete.html?personId=${personGroup.id.personId}&groupId=${personGroup.id.groupId}">delete</a></td></tr>
</c:forEach>
</tbody>
</table>
