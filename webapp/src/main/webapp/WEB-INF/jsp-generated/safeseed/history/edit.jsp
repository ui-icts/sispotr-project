<%@ include file="/WEB-INF/include.jsp"  %>
<h1>History</h1>
<div class="box">
<h2>${history.historyId}</h2>
<form:form method="post" commandName="history" action="save.html" ><fieldset>
 
 
 <form:hidden path="historyId" />
 
 
 <label for="params">Params</label>
 <form:input path="params"   /><br/>
 
 
 <label for="notes">Notes</label>
 <form:input path="notes"   /><br/>
 
 
 <label for="lastViewed">LastViewed</label>
 <form:input path="lastViewed" cssClass="dateInput"  /><br/>
 
 
 <label for="seqFrag.seqFragId">SeqFrag</label>
 <form:select path="seqFrag.seqFragId" items="${seqFragList}" itemValue="seqFragId" itemLabel="seqFragId"/><br/>


 
 
 <label for="person.personId">Person</label>
 <form:select path="person.personId" items="${personList}" itemValue="personId" itemLabel="personId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
