<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalSeqFrag</h1>
<div class="box">

<h2> ${globalSeqFrag.globalSeqFrag} </h2>

 
 GlobalSeqFrag: 
 ${globalSeqFrag.globalSeqFrag}<br/><br/>
 
 
 FragLength: 
 ${globalSeqFrag.fragLength}<br/><br/>
 
 
 Completed: 
 ${globalSeqFrag.completed}<br/><br/>
 
 
 DateCompleted: 
 ${globalSeqFrag.dateCompleted}<br/><br/>
 
 
 GlobalHistorys: 
 <ul><c:forEach items="${globalSeqFrag.globalHistorys}" var="item" varStatus="itemStatus" > <li><a href="../globalhistory/edit.html?globalHistoryId=${item.globalHistoryId}" > ${item.globalHistoryId}</a></li> </c:forEach></ul><br/><br/>
 
 
 GlobalFrags: 
 <ul><c:forEach items="${globalSeqFrag.globalFrags}" var="item" varStatus="itemStatus" > <li><a href="../globalfrag/edit.html?globalFragId=${item.globalFragId}" > ${item.globalFragId}</a></li> </c:forEach></ul><br/><br/>
 
 
 GlobalSeq: 
 ${globalSeqFrag.globalSeq}<br/><br/>
 
</div>
