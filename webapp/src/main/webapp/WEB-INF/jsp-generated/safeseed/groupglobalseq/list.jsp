<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GroupGlobalSeq List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>GroupGlobalSeqId</th>
 <th>Label</th>
 <th>Notes</th>
 <th>DateAdded</th>
 <th>Group</th>
 <th>GlobalSeq</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${groupGlobalSeqList}" var="groupGlobalSeq"  varStatus="status"><tr>
<td><a href="edit.html?groupId=${groupGlobalSeq.id.groupId}&globalSeqId=${groupGlobalSeq.id.globalSeqId}">(groupId,${groupGlobalSeq.id.groupId})(globalSeqId,${groupGlobalSeq.id.globalSeqId})</a></td> 
<td>${groupGlobalSeq.label}</td>
<td>${groupGlobalSeq.notes}</td>
<td>${groupGlobalSeq.dateAdded}</td>
<td>${groupGlobalSeq.group.groupId}</td>
<td>${groupGlobalSeq.globalSeq.globalSeqId}</td>
<td><a href="edit.html?groupId=${groupGlobalSeq.id.groupId}&globalSeqId=${groupGlobalSeq.id.globalSeqId}">edit</a> <a href="show.html?groupId=${groupGlobalSeq.id.groupId}&globalSeqId=${groupGlobalSeq.id.globalSeqId}">view</a> <a href="delete.html?groupId=${groupGlobalSeq.id.groupId}&globalSeqId=${groupGlobalSeq.id.globalSeqId}">delete</a></td></tr>
</c:forEach>
</tbody>
</table>
