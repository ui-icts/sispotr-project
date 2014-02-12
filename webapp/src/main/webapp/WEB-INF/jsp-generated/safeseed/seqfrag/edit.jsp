<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqFrag</h1>
<div class="box">
<h2>${seqFrag.seqFragId}</h2>
<form:form method="post" commandName="seqfrag" action="save.html" ><fieldset>
 
 
 <form:hidden path="seqFragId" />
 
 
 <label for="fragLength">FragLength</label>
 <form:input path="fragLength"   /><br/>
 
 
 <label for="completed">Completed</label>
 <form:input path="completed"   /><br/>
 
 

 
 

 
 

 
 

 
 
 <label for="seq.seqId">Seq</label>
 <form:select path="seq.seqId" items="${seqList}" itemValue="seqId" itemLabel="seqId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
