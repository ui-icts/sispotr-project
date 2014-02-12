<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqCollection</h1>
<div class="box">

<h2> ${seqCollectionId.personId}  ${seqCollectionId.seqId} </h2>

 
 SeqCollectionId: 
(personId,${seqCollection.id.personId})(seqId,${seqCollection.id.seqId}) 
 
 
 SeqName: 
 ${seqCollection.seqName}<br/><br/>
 
 
 Description: 
 ${seqCollection.description}<br/><br/>
 
 
 DateAdded: 
 ${seqCollection.dateAdded}<br/><br/>
 
 
 Person: 
 ${seqCollection.person}<br/><br/>
 
 
 Seq: 
 ${seqCollection.seq}<br/><br/>
 
</div>
