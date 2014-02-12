<%@ include file="/WEB-INF/include.jsp"%>
<h1>Built Indexes</h1>
<div class="box">
<h2>Index Info</h2>
  <p>The following indexes have been built</p>


<table >
<thead>
<tr>
<th>
IndexName
</th>

<tbody>
<c:forEach items="${indexList}" var="index">
<tr>

<td>${index.indexName}</td>




</tr>


</c:forEach>
</tbody>
</table>
</div>

