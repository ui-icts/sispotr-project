<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalCollection</h1>
<div class="box">
<h2>${globalCollection.personId}</h2>
<form:form method="post" commandName="globalcollection" action="save.html" ><fieldset>
 
 
 <form:hidden path="personId" />
 
 
 <label for="label">Label</label>
 <form:input path="label"   /><br/>
 
 
 <label for="notes">Notes</label>
 <form:input path="notes"   /><br/>
 
 
 <label for="dateAdded">DateAdded</label>
 <form:input path="dateAdded" cssClass="dateInput"  /><br/>
 
 
 <label for="globalSeq.globalSeqId">GlobalSeq</label>
 <form:select path="globalSeq.globalSeqId" items="${globalSeqList}" itemValue="globalSeqId" itemLabel="globalSeqId"/><br/>


 
 
 <label for="person.personId">Person</label>
 <form:select path="person.personId" items="${personList}" itemValue="personId" itemLabel="personId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
