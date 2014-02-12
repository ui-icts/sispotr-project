<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Seq</h1>
<div class="box">

<h2> ${seq.seqId} </h2>

 
 SeqId: 
 ${seq.seqId}<br/><br/>
 
 
 Sequence: 
 ${seq.sequence}<br/><br/>
 
 
 Name: 
 ${seq.name}<br/><br/>
 
 
 Description: 
 ${seq.description}<br/><br/>
 
 
 Species: 
 ${seq.species}<br/><br/>
 
 
 DateAdded: 
 ${seq.dateAdded}<br/><br/>
 
 
 GroupSeqs: 
 not implemented<br/><br/>
 
 
 SeqFrags: 
 <ul><c:forEach items="${seq.seqFrags}" var="item" varStatus="itemStatus" > <li><a href="../seqfrag/edit.html?seqFragId=${item.seqFragId}" > ${item.seqFragId}</a></li> </c:forEach></ul><br/><br/>
 
 
 Submissions: 
 <ul><c:forEach items="${seq.submissions}" var="item" varStatus="itemStatus" > <li><a href="../submission/edit.html?submissionId=${item.submissionId}" > ${item.submissionId}</a></li> </c:forEach></ul><br/><br/>
 
 
 SeqRelationships: 
 not implemented<br/><br/>
 
 
 Collections: 
 not implemented<br/><br/>
 
 
 Person: 
 ${seq.person}<br/><br/>
 
</div>
