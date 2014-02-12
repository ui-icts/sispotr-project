<%@ include file="/WEB-INF/include.jsp"  %>
<h1>History List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>HistoryId</th>
 <th>Params</th>
 <th>Notes</th>
 <th>LastViewed</th>
 <th>SeqFrag</th>
 <th>Person</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${historyList}" var="history"  varStatus="status"><tr>
<td><a href="edit.html?historyId=${history.historyId}">${history.historyId}</a></td>
<td>${history.params}</td>
<td>${history.notes}</td>
<td>${history.lastViewed}</td>
<td>${history.seqFrag.seqFragId}</td>
<td>${history.person.personId}</td>
<td><a href="edit.html?historyId=${history.historyId}">edit</a> <a href="show.html?historyId=${history.historyId}">view</a> <a href="delete.html?historyId=${history.historyId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
