<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalSeqFrag List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>GlobalSeqFrag</th>
 <th>FragLength</th>
 <th>Completed</th>
 <th>DateCompleted</th>
 <th>GlobalHistorys</th>
 <th>GlobalFrags</th>
 <th>GlobalSeq</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${globalSeqFragList}" var="globalSeqFrag"  varStatus="status"><tr>
<td><a href="edit.html?globalSeqFrag=${globalSeqFrag.globalSeqFrag}">${globalSeqFrag.globalSeqFrag}</a></td>
<td>${globalSeqFrag.fragLength}</td>
<td>${globalSeqFrag.completed}</td>
<td>${globalSeqFrag.dateCompleted}</td>
<td>globalHistorys</td>
<td>globalFrags</td>
<td>${globalSeqFrag.globalSeq.globalSeqId}</td>
<td><a href="edit.html?globalSeqFrag=${globalSeqFrag.globalSeqFrag}">edit</a> <a href="show.html?globalSeqFrag=${globalSeqFrag.globalSeqFrag}">view</a> <a href="delete.html?globalSeqFrag=${globalSeqFrag.globalSeqFrag}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
