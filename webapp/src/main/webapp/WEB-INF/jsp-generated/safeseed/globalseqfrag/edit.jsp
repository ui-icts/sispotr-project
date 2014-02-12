<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalSeqFrag</h1>
<div class="box">
<h2>${globalSeqFrag.globalSeqFrag}</h2>
<form:form method="post" commandName="globalseqfrag" action="save.html" ><fieldset>
 
 
 <form:hidden path="globalSeqFrag" />
 
 
 <label for="fragLength">FragLength</label>
 <form:input path="fragLength"   /><br/>
 
 
 <label for="completed">Completed</label>
 <form:input path="completed"   /><br/>
 
 
 <label for="dateCompleted">DateCompleted</label>
 <form:input path="dateCompleted" cssClass="dateInput"  /><br/>
 
 

 
 

 
 
 <label for="globalSeq.globalSeqId">GlobalSeq</label>
 <form:select path="globalSeq.globalSeqId" items="${globalSeqList}" itemValue="globalSeqId" itemLabel="globalSeqId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
