<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GroupSeq List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>GroupSeqId</th>
 <th>Name</th>
 <th>Notes</th>
 <th>DateAdded</th>
 <th>Group</th>
 <th>Seq</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${groupSeqList}" var="groupSeq"  varStatus="status"><tr>
<td><a href="edit.html?groupId=${groupSeq.id.groupId}&seqId=${groupSeq.id.seqId}">(groupId,${groupSeq.id.groupId})(seqId,${groupSeq.id.seqId})</a></td> 
<td>${groupSeq.name}</td>
<td>${groupSeq.notes}</td>
<td>${groupSeq.dateAdded}</td>
<td>${groupSeq.group.groupId}</td>
<td>${groupSeq.seq.seqId}</td>
<td><a href="edit.html?groupId=${groupSeq.id.groupId}&seqId=${groupSeq.id.seqId}">edit</a> <a href="show.html?groupId=${groupSeq.id.groupId}&seqId=${groupSeq.id.seqId}">view</a> <a href="delete.html?groupId=${groupSeq.id.groupId}&seqId=${groupSeq.id.seqId}">delete</a></td></tr>
</c:forEach>
</tbody>
</table>
