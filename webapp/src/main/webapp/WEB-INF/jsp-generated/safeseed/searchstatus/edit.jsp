<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SearchStatus</h1>
<div class="box">
<h2>${searchStatus.searchSeq}</h2>
<form:form method="post" commandName="searchstatus" action="save.html" ><fieldset>
 
 
 <form:hidden path="searchSeq" />
 
 
 <label for="searchSeqFull">SearchSeqFull</label>
 <form:input path="searchSeqFull"   /><br/>
 
 
 <label for="status">Status</label>
 <form:input path="status"   /><br/>
 
 
 <label for="entryTime">EntryTime</label>
 <form:input path="entryTime" cssClass="dateInput"  /><br/>
 
 
 <label for="checkoutTime">CheckoutTime</label>
 <form:input path="checkoutTime" cssClass="dateInput"  /><br/>
 
 
 <label for="finishTime">FinishTime</label>
 <form:input path="finishTime" cssClass="dateInput"  /><br/>
 
 
 <label for="readCount">ReadCount</label>
 <form:input path="readCount"   /><br/>
 
 

 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
