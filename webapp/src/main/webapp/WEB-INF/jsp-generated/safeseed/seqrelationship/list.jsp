<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqRelationship List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SeqRelationshipId</th>
 <th>Note</th>
 <th>Type</th>
 <th>GlobalSeq</th>
 <th>Seq</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${seqRelationshipList}" var="seqRelationship"  varStatus="status"><tr>
<td><a href="edit.html?seqId=${seqRelationship.id.seqId}&globalSeqId=${seqRelationship.id.globalSeqId}">(seqId,${seqRelationship.id.seqId})(globalSeqId,${seqRelationship.id.globalSeqId})</a></td> 
<td>${seqRelationship.note}</td>
<td>${seqRelationship.type}</td>
<td>${seqRelationship.globalSeq.globalSeqId}</td>
<td>${seqRelationship.seq.seqId}</td>
<td><a href="edit.html?seqId=${seqRelationship.id.seqId}&globalSeqId=${seqRelationship.id.globalSeqId}">edit</a> <a href="show.html?seqId=${seqRelationship.id.seqId}&globalSeqId=${seqRelationship.id.globalSeqId}">view</a> <a href="delete.html?seqId=${seqRelationship.id.seqId}&globalSeqId=${seqRelationship.id.globalSeqId}">delete</a></td></tr>
</c:forEach>
</tbody>
</table>
