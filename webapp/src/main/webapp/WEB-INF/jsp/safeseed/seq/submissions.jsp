<%@ include file="/WEB-INF/include.jsp"  %>
<h1>My Process Sequence Requests</h1>
<div class="box">
<span class="button" style="float:left;">
<a href="add.html">Add New Sequences</a>
<a href="collection.html">View Full Collection</a>
</span>
<br/>
<br/>
<br/>
<table class="tableData1">
<thead>
<tr>
<th></th>
<th>Label</th>
<th>siRNA Length</th>
<th>Description</th>
<th>Date Submitted</th>
<th></th>
</tr>
</thead>
<tbody>
<c:forEach items="${submissionList}" var="item"  varStatus="status"><tr>
<td>
<c:if test="${item.seqFrag.completed==false && debug!=true}">
<span style="color:#E60">
Processing (ETA: xx hours)
</span>
</c:if>
<c:if test="${item.seqFrag.completed==true || debug ==true}">
<span style="color:#090">
Completed
</span>
</c:if>
</td>

<td>${item.seqFrag.seq.name}</td>
<td>${item.seqFrag.fragLength}</td>
<td>${item.seqFrag.seq.description}</td>

<td>${item.dateRequested}</td> 
<td>
</td>
</tr>
</c:forEach>
</tbody>
</table>

</div>
<script type="text/javascript">
setDataTable(".tableData1","sortable");


</script>
