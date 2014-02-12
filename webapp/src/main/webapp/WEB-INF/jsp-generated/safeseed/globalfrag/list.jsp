<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalFrag List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>GlobalFragId</th>
 <th>StartSeq</th>
 <th>GcContentPercentage</th>
 <th>Three</th>
 <th>Four</th>
 <th>Nineteen</th>
 <th>Twenty</th>
 <th>ThreePlusFour</th>
 <th>PotsNt</th>
 <th>GlobalSeqFrag</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${globalFragList}" var="globalFrag"  varStatus="status"><tr>
<td><a href="edit.html?globalFragId=${globalFrag.globalFragId}">${globalFrag.globalFragId}</a></td>
<td>${globalFrag.startSeq}</td>
<td>${globalFrag.gcContentPercentage}</td>
<td>${globalFrag.three}</td>
<td>${globalFrag.four}</td>
<td>${globalFrag.nineteen}</td>
<td>${globalFrag.twenty}</td>
<td>${globalFrag.threePlusFour}</td>
<td>${globalFrag.potsNt}</td>
<td>${globalFrag.globalSeqFrag.globalSeqFrag}</td>
<td><a href="edit.html?globalFragId=${globalFrag.globalFragId}">edit</a> <a href="show.html?globalFragId=${globalFrag.globalFragId}">view</a> <a href="delete.html?globalFragId=${globalFrag.globalFragId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
