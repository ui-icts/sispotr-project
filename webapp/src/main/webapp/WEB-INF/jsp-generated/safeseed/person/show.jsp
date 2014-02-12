<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Person</h1>
<div class="box">

<h2> ${person.personId} </h2>

 
 PersonId: 
 ${person.personId}<br/><br/>
 
 
 Username: 
 ${person.username}<br/><br/>
 
 
 Guid: 
 ${person.guid}<br/><br/>
 
 
 Domain: 
 ${person.domain}<br/><br/>
 
 
 Email: 
 ${person.email}<br/><br/>
 
 
 Seqs: 
 <ul><c:forEach items="${person.seqs}" var="item" varStatus="itemStatus" > <li><a href="../seq/edit.html?seqId=${item.seqId}" > ${item.seqId}</a></li> </c:forEach></ul><br/><br/>
 
 
 GlobalHistorys: 
 <ul><c:forEach items="${person.globalHistorys}" var="item" varStatus="itemStatus" > <li><a href="../globalhistory/edit.html?globalHistoryId=${item.globalHistoryId}" > ${item.globalHistoryId}</a></li> </c:forEach></ul><br/><br/>
 
 
 PersonGroups: 
 not implemented<br/><br/>
 
 
 Submissions: 
 <ul><c:forEach items="${person.submissions}" var="item" varStatus="itemStatus" > <li><a href="../submission/edit.html?submissionId=${item.submissionId}" > ${item.submissionId}</a></li> </c:forEach></ul><br/><br/>
 
 
 Collections: 
 not implemented<br/><br/>
 
 
 GlobalCollections: 
 <ul><c:forEach items="${person.globalCollections}" var="item" varStatus="itemStatus" > <li><a href="../globalcollection/edit.html?personId=${item.personId}" > ${item.personId}</a></li> </c:forEach></ul><br/><br/>
 
 
 Historys: 
 <ul><c:forEach items="${person.historys}" var="item" varStatus="itemStatus" > <li><a href="../history/edit.html?historyId=${item.historyId}" > ${item.historyId}</a></li> </c:forEach></ul><br/><br/>
 
</div>
