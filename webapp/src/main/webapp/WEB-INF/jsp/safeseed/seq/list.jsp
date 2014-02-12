<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Seq List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SeqId</th>
 <th>AccessionId</th>
 <th>OfficialName</th>
 <th>GiNumber</th>
 <th>FromParam</th>
 <th>Locus</th>
 <th>CustomLabel</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${seqList}" var="seq"  varStatus="status"><tr>
<td><a href="edit.html?seqId=${seq.seqId}">${seq.seqId}</a></td>
<td>${seq.accessionId}</td>
<td>${seq.officialName}</td>
<td>${seq.giNumber}</td>
<td>${seq.fromParam}</td>
<td>${seq.locus}</td>
<td>${seq.customLabel}</td>
<td><a href="edit.html?seqId=${seq.seqId}">edit</a> <a href="show.html?seqId=${seq.seqId}">view</a> <a href="delete.html?seqId=${seq.seqId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
