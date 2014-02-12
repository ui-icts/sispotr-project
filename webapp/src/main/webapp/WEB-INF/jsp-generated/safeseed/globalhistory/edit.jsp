<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalHistory</h1>
<div class="box">
<h2>${globalHistory.globalHistoryId}</h2>
<form:form method="post" commandName="globalhistory" action="save.html" ><fieldset>
 
 
 <form:hidden path="globalHistoryId" />
 
 
 <label for="params">Params</label>
 <form:input path="params"   /><br/>
 
 
 <label for="notes">Notes</label>
 <form:input path="notes"   /><br/>
 
 
 <label for="lastViewed">LastViewed</label>
 <form:input path="lastViewed" cssClass="dateInput"  /><br/>
 
 
 <label for="globalSeqFrag.globalSeqFrag">GlobalSeqFrag</label>
 <form:select path="globalSeqFrag.globalSeqFrag" items="${globalSeqFragList}" itemValue="globalSeqFrag" itemLabel="globalSeqFrag"/><br/>


 
 
 <label for="person.personId">Person</label>
 <form:select path="person.personId" items="${personList}" itemValue="personId" itemLabel="personId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
