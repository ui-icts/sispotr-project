<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalHistory List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>GlobalHistoryId</th>
 <th>Params</th>
 <th>Notes</th>
 <th>LastViewed</th>
 <th>GlobalSeqFrag</th>
 <th>Person</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${globalHistoryList}" var="globalHistory"  varStatus="status"><tr>
<td><a href="edit.html?globalHistoryId=${globalHistory.globalHistoryId}">${globalHistory.globalHistoryId}</a></td>
<td>${globalHistory.params}</td>
<td>${globalHistory.notes}</td>
<td>${globalHistory.lastViewed}</td>
<td>${globalHistory.globalSeqFrag.globalSeqFrag}</td>
<td>${globalHistory.person.personId}</td>
<td><a href="edit.html?globalHistoryId=${globalHistory.globalHistoryId}">edit</a> <a href="show.html?globalHistoryId=${globalHistory.globalHistoryId}">view</a> <a href="delete.html?globalHistoryId=${globalHistory.globalHistoryId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
