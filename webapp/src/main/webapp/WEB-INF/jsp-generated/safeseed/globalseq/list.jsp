<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalSeq List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>GlobalSeqId</th>
 <th>Locus</th>
 <th>Sequence</th>
 <th>GiNumber</th>
 <th>FromField</th>
 <th>Name1</th>
 <th>Name2</th>
 <th>Description</th>
 <th>DateAdded</th>
 <th>GlobalSeqFrags</th>
 <th>SeqRelationships</th>
 <th>GlobalCollections</th>
 <th>GroupGlobalSeqs</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${globalSeqList}" var="globalSeq"  varStatus="status"><tr>
<td><a href="edit.html?globalSeqId=${globalSeq.globalSeqId}">${globalSeq.globalSeqId}</a></td>
<td>${globalSeq.locus}</td>
<td>${globalSeq.sequence}</td>
<td>${globalSeq.giNumber}</td>
<td>${globalSeq.fromField}</td>
<td>${globalSeq.name1}</td>
<td>${globalSeq.name2}</td>
<td>${globalSeq.description}</td>
<td>${globalSeq.dateAdded}</td>
<td>globalSeqFrags</td>
<td>seqRelationships</td>
<td>globalCollections</td>
<td>groupGlobalSeqs</td>
<td><a href="edit.html?globalSeqId=${globalSeq.globalSeqId}">edit</a> <a href="show.html?globalSeqId=${globalSeq.globalSeqId}">view</a> <a href="delete.html?globalSeqId=${globalSeq.globalSeqId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
