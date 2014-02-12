<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Frag List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>FragId</th>
 <th>StartSeq</th>
 <th>GcContentPercentage</th>
 <th>Three</th>
 <th>Four</th>
 <th>Nineteen</th>
 <th>Twenty</th>
 <th>ThreePlusFour</th>
 <th>PotsNt</th>
 <th>SeqFrag</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${fragList}" var="frag"  varStatus="status"><tr>
<td><a href="edit.html?fragId=${frag.fragId}">${frag.fragId}</a></td>
<td>${frag.startSeq}</td>
<td>${frag.gcContentPercentage}</td>
<td>${frag.three}</td>
<td>${frag.four}</td>
<td>${frag.nineteen}</td>
<td>${frag.twenty}</td>
<td>${frag.threePlusFour}</td>
<td>${frag.potsNt}</td>
<td>${frag.seqFrag.seqFragId}</td>
<td><a href="edit.html?fragId=${frag.fragId}">edit</a> <a href="show.html?fragId=${frag.fragId}">view</a> <a href="delete.html?fragId=${frag.fragId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
