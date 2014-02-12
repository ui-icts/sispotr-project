<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalFrag</h1>
<div class="box">
<h2>${globalFrag.globalFragId}</h2>
<form:form method="post" commandName="globalfrag" action="save.html" ><fieldset>
 
 
 <form:hidden path="globalFragId" />
 
 
 <label for="startSeq">StartSeq</label>
 <form:input path="startSeq"   /><br/>
 
 
 <label for="gcContentPercentage">GcContentPercentage</label>
 <form:input path="gcContentPercentage"   /><br/>
 
 
 <label for="three">Three</label>
 <form:input path="three"   /><br/>
 
 
 <label for="four">Four</label>
 <form:input path="four"   /><br/>
 
 
 <label for="nineteen">Nineteen</label>
 <form:input path="nineteen"   /><br/>
 
 
 <label for="twenty">Twenty</label>
 <form:input path="twenty"   /><br/>
 
 
 <label for="threePlusFour">ThreePlusFour</label>
 <form:input path="threePlusFour"   /><br/>
 
 
 <label for="potsNt">PotsNt</label>
 <form:input path="potsNt"   /><br/>
 
 
 <label for="globalSeqFrag.globalSeqFrag">GlobalSeqFrag</label>
 <form:select path="globalSeqFrag.globalSeqFrag" items="${globalSeqFragList}" itemValue="globalSeqFrag" itemLabel="globalSeqFrag"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
