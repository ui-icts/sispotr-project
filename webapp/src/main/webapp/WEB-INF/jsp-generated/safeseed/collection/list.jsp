<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Collection List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>CollectionId</th>
 <th>Name</th>
 <th>Notes</th>
 <th>DateAdded</th>
 <th>Person</th>
 <th>Seq</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${collectionList}" var="collection"  varStatus="status"><tr>
<td><a href="edit.html?personId=${collection.id.personId}&seqId=${collection.id.seqId}">(personId,${collection.id.personId})(seqId,${collection.id.seqId})</a></td> 
<td>${collection.name}</td>
<td>${collection.notes}</td>
<td>${collection.dateAdded}</td>
<td>${collection.person.personId}</td>
<td>${collection.seq.seqId}</td>
<td><a href="edit.html?personId=${collection.id.personId}&seqId=${collection.id.seqId}">edit</a> <a href="show.html?personId=${collection.id.personId}&seqId=${collection.id.seqId}">view</a> <a href="delete.html?personId=${collection.id.personId}&seqId=${collection.id.seqId}">delete</a></td></tr>
</c:forEach>
</tbody>
</table>
