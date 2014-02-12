<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Seq List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SeqId</th>
 <th>Sequence</th>
 <th>Name</th>
 <th>Description</th>
 <th>Species</th>
 <th>DateAdded</th>
 <th>GroupSeqs</th>
 <th>SeqFrags</th>
 <th>Submissions</th>
 <th>SeqRelationships</th>
 <th>Collections</th>
 <th>Person</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${seqList}" var="seq"  varStatus="status"><tr>
<td><a href="edit.html?seqId=${seq.seqId}">${seq.seqId}</a></td>
<td>${seq.sequence}</td>
<td>${seq.name}</td>
<td>${seq.description}</td>
<td>${seq.species}</td>
<td>${seq.dateAdded}</td>
<td>groupSeqs</td>
<td>seqFrags</td>
<td>submissions</td>
<td>seqRelationships</td>
<td>collections</td>
<td>${seq.person.personId}</td>
<td><a href="edit.html?seqId=${seq.seqId}">edit</a> <a href="show.html?seqId=${seq.seqId}">view</a> <a href="delete.html?seqId=${seq.seqId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
