<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Seq</h1>
<div class="box">
<h2>${seq.seqId}</h2>
<form:form method="post" commandName="seq" action="save.html" ><fieldset>
 
 
 <form:hidden path="seqId" />
 
 
 <label for="sequence">Sequence</label>
 <form:input path="sequence"   /><br/>
 
 
 <label for="name">Name</label>
 <form:input path="name"   /><br/>
 
 
 <label for="description">Description</label>
 <form:input path="description"   /><br/>
 
 
 <label for="species">Species</label>
 <form:input path="species"   /><br/>
 
 
 <label for="dateAdded">DateAdded</label>
 <form:input path="dateAdded" cssClass="dateInput"  /><br/>
 
 

 
 

 
 

 
 

 
 

 
 
 <label for="person.personId">Person</label>
 <form:select path="person.personId" items="${personList}" itemValue="personId" itemLabel="personId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
