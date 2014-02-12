<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GroupSeq</h1>
<div class="box">

<form:form method="post" commandName="groupseq" action="save.html" ><fieldset>
 

 
 
 <label for="name">Name</label>
 <form:input path="name"   /><br/>
 
 
 <label for="notes">Notes</label>
 <form:input path="notes"   /><br/>
 
 
 <label for="dateAdded">DateAdded</label>
 <form:input path="dateAdded" cssClass="dateInput"  /><br/>
 
 
 <label for="group.groupId">Group</label>
 <form:select path="group.groupId" items="${groupList}" itemValue="groupId" itemLabel="groupId"/><br/>


 
 
 <label for="seq.seqId">Seq</label>
 <form:select path="seq.seqId" items="${seqList}" itemValue="seqId" itemLabel="seqId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
