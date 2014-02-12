<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Submission List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SubmissionId</th>
 <th>DateRequested</th>
 <th>GroupSeqs</th>
 <th>SeqFrag</th>
 <th>Person</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${submissionList}" var="submission"  varStatus="status"><tr>
<td><a href="edit.html?submissionId=${submission.submissionId}">${submission.submissionId}</a></td>
<td>${submission.dateRequested}</td>
<td>groupSeqs</td>
<td>${submission.seqFrag.seqFragId}</td>
<td>${submission.person.personId}</td>
<td><a href="edit.html?submissionId=${submission.submissionId}">edit</a> <a href="show.html?submissionId=${submission.submissionId}">view</a> <a href="delete.html?submissionId=${submission.submissionId}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>