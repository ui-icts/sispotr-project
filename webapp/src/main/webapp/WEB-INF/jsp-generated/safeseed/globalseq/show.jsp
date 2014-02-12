<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GlobalSeq</h1>
<div class="box">

<h2> ${globalSeq.globalSeqId} </h2>

 
 GlobalSeqId: 
 ${globalSeq.globalSeqId}<br/><br/>
 
 
 Locus: 
 ${globalSeq.locus}<br/><br/>
 
 
 Sequence: 
 ${globalSeq.sequence}<br/><br/>
 
 
 GiNumber: 
 ${globalSeq.giNumber}<br/><br/>
 
 
 FromField: 
 ${globalSeq.fromField}<br/><br/>
 
 
 Name1: 
 ${globalSeq.name1}<br/><br/>
 
 
 Name2: 
 ${globalSeq.name2}<br/><br/>
 
 
 Description: 
 ${globalSeq.description}<br/><br/>
 
 
 DateAdded: 
 ${globalSeq.dateAdded}<br/><br/>
 
 
 GlobalSeqFrags: 
 <ul><c:forEach items="${globalSeq.globalSeqFrags}" var="item" varStatus="itemStatus" > <li><a href="../globalseqfrag/edit.html?globalSeqFrag=${item.globalSeqFrag}" > ${item.globalSeqFrag}</a></li> </c:forEach></ul><br/><br/>
 
 
 SeqRelationships: 
 not implemented<br/><br/>
 
 
 GlobalCollections: 
 <ul><c:forEach items="${globalSeq.globalCollections}" var="item" varStatus="itemStatus" > <li><a href="../globalcollection/edit.html?personId=${item.personId}" > ${item.personId}</a></li> </c:forEach></ul><br/><br/>
 
 
 GroupGlobalSeqs: 
 not implemented<br/><br/>
 
</div>
