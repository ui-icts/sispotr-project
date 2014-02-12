<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Submission</h1>
<div class="box">
<h2>${submission.submissionId}</h2>
<form:form method="post" commandName="submission" action="save.html" ><fieldset>
 
 
 <form:hidden path="submissionId" />
 
 
 <label for="dateRequested">DateRequested</label>
 <form:input path="dateRequested" cssClass="dateInput"  /><br/>
 
 

 
 
 <label for="seqFrag.seqFragId">SeqFrag</label>
 <form:select path="seqFrag.seqFragId" items="${seqFragList}" itemValue="seqFragId" itemLabel="seqFragId"/><br/>


 
 
 <label for="person.personId">Person</label>
 <form:select path="person.personId" items="${personList}" itemValue="personId" itemLabel="personId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
