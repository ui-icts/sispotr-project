<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Group</h1>
<div class="box">
<h2>${group.groupId}</h2>
<form:form method="post" commandName="group" action="save.html" ><fieldset>
 
 
 <form:hidden path="groupId" />
 
 
 <label for="name">Name</label>
 <form:input path="name"   /><br/>
 
 
 <label for="description">Description</label>
 <form:input path="description"   /><br/>
 
 

 
 

 
 

 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
