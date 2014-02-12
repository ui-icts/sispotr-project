<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Person List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>PersonId</th>
 <th>Username</th>
 <th>Guid</th>
 <th>Domain</th>
 <th>Email</th>
 <th>Seqs</th>
 <th>GlobalHistorys</th>
 <th>PersonGroups</th>
 <th>Submissions</th>
 <th>Collections</th>
 <th>GlobalCollections</th>
 <th>Historys</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${personList}" var="person"  varStatus="status"><tr>
<td><a href="edit.html?personId=${person.personId}">${person.personId}</a></td>
<td>${person.username}</td>
<td>${person.guid}</td>
<td>${person.domain}</td>
<td>${person.email}</td>
<td>seqs</td>
<td>globalHistorys</td>
<td>personGroups</td>
<td>submissions</td>
<td>collections</td>
<td>globalCollections</td>
<td>historys</td>
<td><a href="edit.html?personId=${person.personId}">edit</a> <a href="show.html?personId=${person.personId}">view</a> <a href="delete.html?personId=${person.personId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
