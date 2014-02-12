<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SearchResult List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SearchResultId</th>
 <th>SearchSeqRc</th>
 <th>ResultSeq</th>
 <th>ResultAccession</th>
 <th>ResultAccessionOffset</th>
 <th>ResultOffset</th>
 <th>TimeStamp</th>
 <th>SearchStatus</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${searchResultList}" var="searchResult"  varStatus="status"><tr>
<td><a href="edit.html?searchResultId=${searchResult.searchResultId}">${searchResult.searchResultId}</a></td>
<td>${searchResult.searchSeqRc}</td>
<td>${searchResult.resultSeq}</td>
<td>${searchResult.resultAccession}</td>
<td>${searchResult.resultAccessionOffset}</td>
<td>${searchResult.resultOffset}</td>
<td>${searchResult.timeStamp}</td>
<td>${searchResult.searchStatus.searchSeq}</td>
<td><a href="edit.html?searchResultId=${searchResult.searchResultId}">edit</a> <a href="show.html?searchResultId=${searchResult.searchResultId}">view</a> <a href="delete.html?searchResultId=${searchResult.searchResultId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
