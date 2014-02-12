<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Seq</h1>
<div class="box">

<h2> ${seq.seqId} </h2>

 
 SeqId: 
 ${seq.seqId}<br/><br/>
 
 
 AccessionId: 
 ${seq.accessionId}<br/><br/>
 
 
 OfficialName: 
 ${seq.officialName}<br/><br/>
 
 
 Sequence: 
 ${seq.sequence}<br/><br/>
 
 
 GiNumber: 
 ${seq.giNumber}<br/><br/>
 
 
 FromParam: 
 ${seq.fromParam}<br/><br/>
 
 
 Locus: 
 ${seq.locus}<br/><br/>
 
 
 CustomLabel: 
 ${seq.customLabel}<br/><br/>
 
 
 Frags: 
 <ul><c:forEach items="${seq.frags}" var="item" varStatus="itemStatus" > <li><a href="../frag/edit.html?fragId=${item.fragId}" > ${item.fragId}</a></li> </c:forEach></ul><br/><br/>
 
 
 Historys: 
 <ul><c:forEach items="${seq.historys}" var="item" varStatus="itemStatus" > <li><a href="../history/edit.html?historyId=${item.historyId}" > ${item.historyId}</a></li> </c:forEach></ul><br/><br/>
 
 
 GroupSeqs: 
 not implemented<br/><br/>
 
 
 SeqOwners: 
 not implemented<br/><br/>
 
</div>
