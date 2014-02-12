<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqFrag</h1>
<div class="box">

<h2> ${seqFrag.seqFragId} </h2>

 
 SeqFragId: 
 ${seqFrag.seqFragId}<br/><br/>
 
 
 FragLength: 
 ${seqFrag.fragLength}<br/><br/>
 
 
 Completed: 
 ${seqFrag.completed}<br/><br/>
 
 
 Frags: 
 <ul><c:forEach items="${seqFrag.frags}" var="item" varStatus="itemStatus" > <li><a href="../frag/edit.html?fragId=${item.fragId}" > ${item.fragId}</a></li> </c:forEach></ul><br/><br/>
 
 
 Queues: 
 <ul><c:forEach items="${seqFrag.queues}" var="item" varStatus="itemStatus" > <li><a href="../queue/edit.html?queueId=${item.queueId}" > ${item.queueId}</a></li> </c:forEach></ul><br/><br/>
 
 
 Submissions: 
 <ul><c:forEach items="${seqFrag.submissions}" var="item" varStatus="itemStatus" > <li><a href="../submission/edit.html?submissionId=${item.submissionId}" > ${item.submissionId}</a></li> </c:forEach></ul><br/><br/>
 
 
 Seq: 
 ${seqFrag.seq}<br/><br/>
 
</div>
