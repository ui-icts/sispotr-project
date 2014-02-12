<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqRelationship</h1>
<div class="box">

<form:form method="post" commandName="seqrelationship" action="save.html" ><fieldset>
 

 
 
 <label for="note">Note</label>
 <form:input path="note"   /><br/>
 
 
 <label for="type">Type</label>
 <form:input path="type"   /><br/>
 
 
 <label for="globalSeq.globalSeqId">GlobalSeq</label>
 <form:select path="globalSeq.globalSeqId" items="${globalSeqList}" itemValue="globalSeqId" itemLabel="globalSeqId"/><br/>


 
 
 <label for="seq.seqId">Seq</label>
 <form:select path="seq.seqId" items="${seqList}" itemValue="seqId" itemLabel="seqId"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
