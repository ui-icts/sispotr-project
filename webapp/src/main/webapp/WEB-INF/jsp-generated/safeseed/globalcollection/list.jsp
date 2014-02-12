<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalCollection List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>PersonId</th>
 <th>Label</th>
 <th>Notes</th>
 <th>DateAdded</th>
 <th>GlobalSeq</th>
 <th>Person</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${globalCollectionList}" var="globalCollection"  varStatus="status"><tr>
<td><a href="edit.html?personId=${globalCollection.personId}">${globalCollection.personId}</a></td>
<td>${globalCollection.label}</td>
<td>${globalCollection.notes}</td>
<td>${globalCollection.dateAdded}</td>
<td>${globalCollection.globalSeq.globalSeqId}</td>
<td>${globalCollection.person}</td>
<td><a href="edit.html?personId=${globalCollection.personId}">edit</a> <a href="show.html?personId=${globalCollection.personId}">view</a> <a href="delete.html?personId=${globalCollection.personId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
