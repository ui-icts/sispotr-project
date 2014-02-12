<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GroupGlobalSeq</h1>
<div class="box">

<form:form method="post" commandName="groupglobalseq" action="save.html" ><fieldset>
 

 
 
 <label for="label">Label</label>
 <form:input path="label"   /><br/>
 
 
 <label for="notes">Notes</label>
 <form:input path="notes"   /><br/>
 
 
 <label for="dateAdded">DateAdded</label>
 <form:input path="dateAdded" cssClass="dateInput"  /><br/>
 
 
 <label for="group.groupId">Group</label>
 <form:select path="group.groupId" items="${groupList}" itemValue="groupId" itemLabel="groupId"/><br/>


 
 
 <label for="globalSeq.globalSeqId">GlobalSeq</label>
 <form:select path="globalSeq.globalSeqId" items="${globalSeqList}" itemValue="globalSeqId" itemLabel="globalSeqId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
