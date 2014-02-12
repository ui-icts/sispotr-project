<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Audit</h1>
<div class="box">
<h2>${audit.auditId}</h2>
<form:form method="post" commandName="audit" action="save.html" ><fieldset>
 
 
 <form:hidden path="auditId" />
 
 
 <label for="eventType">EventType</label>
 <form:input path="eventType"   /><br/>
 
 
 <label for="description">Description</label>
 <form:input path="description"   /><br/>
 
 
 <label for="timeRecorded">TimeRecorded</label>
 <form:input path="timeRecorded" cssClass="dateInput"  /><br/>
 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
