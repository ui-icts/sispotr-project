<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Submission</h1>
<div class="box">

<h2> ${submission.submissionId} </h2>

 
 SubmissionId: 
 ${submission.submissionId}<br/><br/>
 
 
 DateRequested: 
 ${submission.dateRequested}<br/><br/>
 
 
 GroupSeqs: 
 <ul><c:forEach items="${submission.groupSeqs}" var="item" varStatus="itemStatus" > <li><a href="../groupseq/edit.html?groupId=${item.groupId}" > ${item.groupId}</a></li> </c:forEach></ul><br/><br/>
 
 
 SeqFrag: 
 ${submission.seqFrag}<br/><br/>
 
 
 Person: 
 ${submission.person}<br/><br/>
 
</div>
