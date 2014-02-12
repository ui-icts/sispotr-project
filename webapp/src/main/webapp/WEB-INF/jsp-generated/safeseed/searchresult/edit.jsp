<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SearchResult</h1>
<div class="box">
<h2>${searchResult.searchResultId}</h2>
<form:form method="post" commandName="searchresult" action="save.html" ><fieldset>
 
 
 <form:hidden path="searchResultId" />
 
 
 <label for="searchSeqRc">SearchSeqRc</label>
 <form:input path="searchSeqRc"   /><br/>
 
 
 <label for="resultSeq">ResultSeq</label>
 <form:input path="resultSeq"   /><br/>
 
 
 <label for="resultAccession">ResultAccession</label>
 <form:input path="resultAccession"   /><br/>
 
 
 <label for="resultAccessionOffset">ResultAccessionOffset</label>
 <form:input path="resultAccessionOffset"   /><br/>
 
 
 <label for="resultOffset">ResultOffset</label>
 <form:input path="resultOffset"   /><br/>
 
 
 <label for="timeStamp">TimeStamp</label>
 <form:input path="timeStamp" cssClass="dateInput"  /><br/>
 
 
 <label for="searchStatus.searchSeq">SearchStatus</label>
 <form:select path="searchStatus.searchSeq" items="${searchStatusList}" itemValue="searchSeq" itemLabel="searchSeq"/><br/>


 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
