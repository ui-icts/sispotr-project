<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Person</h1>
<div class="box">
<h2>${person.personId}</h2>
<form:form method="post" commandName="person" action="save.html" ><fieldset>
 
 
 <form:hidden path="personId" />
 
 
 <label for="username">Username</label>
 <form:input path="username"   /><br/>
 
 
 <label for="guid">Guid</label>
 <form:input path="guid"   /><br/>
 
 
 <label for="domain">Domain</label>
 <form:input path="domain"   /><br/>
 
 
 <label for="email">Email</label>
 <form:input path="email"   /><br/>
 
 

 
 

 
 

 
 

 
 

 
 

 
 

 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
