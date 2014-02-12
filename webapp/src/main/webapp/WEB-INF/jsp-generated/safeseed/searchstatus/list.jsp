<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SearchStatus List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SearchSeq</th>
 <th>SearchSeqFull</th>
 <th>Status</th>
 <th>EntryTime</th>
 <th>CheckoutTime</th>
 <th>FinishTime</th>
 <th>ReadCount</th>
 <th>SearchResults</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${searchStatusList}" var="searchStatus"  varStatus="status"><tr>
<td><a href="edit.html?searchSeq=${searchStatus.searchSeq}">${searchStatus.searchSeq}</a></td>
<td>${searchStatus.searchSeqFull}</td>
<td>${searchStatus.status}</td>
<td>${searchStatus.entryTime}</td>
<td>${searchStatus.checkoutTime}</td>
<td>${searchStatus.finishTime}</td>
<td>${searchStatus.readCount}</td>
<td>searchResults</td>
<td><a href="edit.html?searchSeq=${searchStatus.searchSeq}">edit</a> <a href="show.html?searchSeq=${searchStatus.searchSeq}">view</a> <a href="delete.html?searchSeq=${searchStatus.searchSeq}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
