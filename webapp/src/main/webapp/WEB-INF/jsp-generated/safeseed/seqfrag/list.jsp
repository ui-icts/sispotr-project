<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqFrag List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SeqFragId</th>
 <th>FragLength</th>
 <th>Completed</th>
 <th>Frags</th>
 <th>Queues</th>
 <th>Submissions</th>
 <th>Historys</th>
 <th>Seq</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${seqFragList}" var="seqFrag"  varStatus="status"><tr>
<td><a href="edit.html?seqFragId=${seqFrag.seqFragId}">${seqFrag.seqFragId}</a></td>
<td>${seqFrag.fragLength}</td>
<td>${seqFrag.completed}</td>
<td>frags</td>
<td>queues</td>
<td>submissions</td>
<td>historys</td>
<td>${seqFrag.seq.seqId}</td>
<td><a href="edit.html?seqFragId=${seqFrag.seqFragId}">edit</a> <a href="show.html?seqFragId=${seqFrag.seqFragId}">view</a> <a href="delete.html?seqFragId=${seqFrag.seqFragId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
