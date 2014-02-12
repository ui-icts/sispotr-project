<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Collection</h1>
<div class="box">

<form:form method="post" commandName="collection" action="save.html" ><fieldset>
 

 
 
 <label for="name">Name</label>
 <form:input path="name"   /><br/>
 
 
 <label for="notes">Notes</label>
 <form:input path="notes"   /><br/>
 
 
 <label for="dateAdded">DateAdded</label>
 <form:input path="dateAdded" cssClass="dateInput"  /><br/>
 
 
 <label for="person.personId">Person</label>
 <form:select path="person.personId" items="${personList}" itemValue="personId" itemLabel="personId"/><br/>


 
 
 <label for="seq.seqId">Seq</label>
 <form:select path="seq.seqId" items="${seqList}" itemValue="seqId" itemLabel="seqId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
