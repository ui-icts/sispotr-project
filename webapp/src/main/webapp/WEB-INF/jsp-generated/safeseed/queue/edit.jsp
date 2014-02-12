<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Queue</h1>
<div class="box">
<h2>${queue.queueId}</h2>
<form:form method="post" commandName="queue" action="save.html" ><fieldset>
 
 
 <form:hidden path="queueId" />
 
 
 <label for="complete">Complete</label>
 <form:input path="complete"   /><br/>
 
 
 <label for="dateCompleted">DateCompleted</label>
 <form:input path="dateCompleted" cssClass="dateInput"  /><br/>
 
 
 <label for="logText">LogText</label>
 <form:input path="logText"   /><br/>
 
 
 <label for="priorityLevel">PriorityLevel</label>
 <form:input path="priorityLevel"   /><br/>
 
 
 <label for="seqFrag.seqFragId">SeqFrag</label>
 <form:select path="seqFrag.seqFragId" items="${seqFragList}" itemValue="seqFragId" itemLabel="seqFragId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
