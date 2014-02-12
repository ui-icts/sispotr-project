<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Queue List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>QueueId</th>
 <th>Complete</th>
 <th>DateCompleted</th>
 <th>LogText</th>
 <th>PriorityLevel</th>
 <th>SeqFrag</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${queueList}" var="queue"  varStatus="status"><tr>
<td><a href="edit.html?queueId=${queue.queueId}">${queue.queueId}</a></td>
<td>${queue.complete}</td>
<td>${queue.dateCompleted}</td>
<td>${queue.logText}</td>
<td>${queue.priorityLevel}</td>
<td>${queue.seqFrag.seqFragId}</td>
<td><a href="edit.html?queueId=${queue.queueId}">edit</a> <a href="show.html?queueId=${queue.queueId}">view</a> <a href="delete.html?queueId=${queue.queueId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
