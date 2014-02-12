<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Person List</h1>

<div class="btn-group"><a class="btn" href="add.html">Add</a></div>
<table class="table">
<thead>
<tr>
 <th>Username</th>
 <th>Email</th>
  <th>Organization</th>
   <th>Creation Date</th>
    <th>Access Level</th>

<th></th></tr>
</thead>
<tbody>
<c:forEach items="${personList}" var="person"  varStatus="status"><tr>
<td><a href="edit.html?personId=${person.personId}">${person.username}</a></td>
<td>${person.email}</td>
<td>${person.organization}</td>
<td>${person.creationDate}</td>
<td>${person.accessLevel}</td>
<td>
<div class="btn-group">
<%-- <a href="edit.html?personId=${person.personId}">edit</a>  --%>
<a class="btn"  href="show.html?personId=${person.personId}">view</a>
 <a class="btn" href="delete.html?personId=${person.personId}">delete</a>
 </div></td> </tr>
 
</c:forEach>
</tbody>
</table>
