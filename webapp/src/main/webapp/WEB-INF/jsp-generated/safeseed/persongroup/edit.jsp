<%@ include file="/WEB-INF/include.jsp"  %>
<h1>PersonGroup</h1>
<div class="box">

<form:form method="post" commandName="persongroup" action="save.html" ><fieldset>
 

 
 
 <label for="role">Role</label>
 <form:input path="role"   /><br/>
 
 
 <label for="group.groupId">Group</label>
 <form:select path="group.groupId" items="${groupList}" itemValue="groupId" itemLabel="groupId"/><br/>


 
 
 <label for="person.personId">Person</label>
 <form:select path="person.personId" items="${personList}" itemValue="personId" itemLabel="personId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
