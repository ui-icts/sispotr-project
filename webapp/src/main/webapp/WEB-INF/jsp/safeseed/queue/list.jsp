<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Process Queue</h1>
<div class="box">
<a href="processNext.html">Process Next</a>
<table class="tableData1">
<thead>
<tr>
<th></th>
 <th>Complete</th>
  <th>Seq</th>
  <th>Length</th>
 <th>DateCompleted</th>
 <th>LogText</th>
 <th>PriorityLevel</th>
 <th>SeqFrag</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${queueList}" var="queue"  varStatus="status"><tr>
<td><a href="processNow.html?queueId=${queue.queueId}">Process Now</a></td>
<td>${queue.complete}</td>
<td>${queue.seqFrag.seq.name}</td>
<td>${queue.seqFrag.fragLength}</td>

<td>${queue.dateCompleted}</td>
<td>${queue.logText}</td>
<td>${queue.priorityLevel}</td>
<td>${queue.seqFrag.seqFragId}</td>
<td><a href="delete.html?queueId=${queue.queueId}">cancel</a></td> </tr>
</c:forEach>
</tbody>
</table>
</div>
