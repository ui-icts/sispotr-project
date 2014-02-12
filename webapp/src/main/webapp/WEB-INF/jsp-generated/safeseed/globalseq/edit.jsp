<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalSeq</h1>
<div class="box">
<h2>${globalSeq.globalSeqId}</h2>
<form:form method="post" commandName="globalseq" action="save.html" ><fieldset>
 
 
 <form:hidden path="globalSeqId" />
 
 
 <label for="locus">Locus</label>
 <form:input path="locus"   /><br/>
 
 
 <label for="sequence">Sequence</label>
 <form:input path="sequence"   /><br/>
 
 
 <label for="giNumber">GiNumber</label>
 <form:input path="giNumber"   /><br/>
 
 
 <label for="fromField">FromField</label>
 <form:input path="fromField"   /><br/>
 
 
 <label for="name1">Name1</label>
 <form:input path="name1"   /><br/>
 
 
 <label for="name2">Name2</label>
 <form:input path="name2"   /><br/>
 
 
 <label for="description">Description</label>
 <form:input path="description"   /><br/>
 
 
 <label for="dateAdded">DateAdded</label>
 <form:input path="dateAdded" cssClass="dateInput"  /><br/>
 
 

 
 

 
 

 
 

 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
