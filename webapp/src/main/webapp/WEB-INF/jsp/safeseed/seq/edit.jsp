<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Seq</h1>
<div class="box">
<h2>${seq.seqId}</h2>
<form:form method="post" commandName="seq" action="save.html" ><fieldset>
 
 
 <form:hidden path="seqId" />
 
 
 <label for="accessionId">AccessionId</label>
 <form:input path="accessionId"   /><br/>
 
 
 <label for="officialName">OfficialName</label>
 <form:input path="officialName"   /><br/>
 
 
 <label for="sequence">Sequence</label>
 <form:input path="sequence"   /><br/>
 
 
 <label for="giNumber">GiNumber</label>
 <form:input path="giNumber"   /><br/>
 
 
 <label for="fromParam">FromParam</label>
 <form:input path="fromParam"   /><br/>
 
 
 <label for="locus">Locus</label>
 <form:input path="locus"   /><br/>
 
 
 <label for="customLabel">CustomLabel</label>
 <form:input path="customLabel"   /><br/>
 
 

 
 

 
 

 
 

 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
