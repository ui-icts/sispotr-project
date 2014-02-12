<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqCollection List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SeqCollectionId</th>
 <th>SeqName</th>
 <th>Description</th>
 <th>DateAdded</th>
 <th>Person</th>
 <th>Seq</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${seqCollectionList}" var="seqCollection"  varStatus="status"><tr>
<td><a href="edit.html?personId=${seqCollection.id.personId}&seqId=${seqCollection.id.seqId}">(personId,${seqCollection.id.personId})(seqId,${seqCollection.id.seqId})</a></td> 
<td>${seqCollection.seqName}</td>
<td>${seqCollection.description}</td>
<td>${seqCollection.dateAdded}</td>
<td>${seqCollection.person.personId}</td>
<td>${seqCollection.seq.seqId}</td>
<td><a href="edit.html?personId=${seqCollection.id.personId}&seqId=${seqCollection.id.seqId}">edit</a> <a href="show.html?personId=${seqCollection.id.personId}&seqId=${seqCollection.id.seqId}">view</a> <a href="delete.html?personId=${seqCollection.id.personId}&seqId=${seqCollection.id.seqId}">delete</a></td></tr>
</c:forEach>
</tbody>
</table>
