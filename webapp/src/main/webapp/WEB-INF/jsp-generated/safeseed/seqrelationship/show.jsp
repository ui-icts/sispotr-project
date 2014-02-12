<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SeqRelationship</h1>
<div class="box">

<h2> ${seqRelationshipId.seqId}  ${seqRelationshipId.globalSeqId} </h2>

 
 SeqRelationshipId: 
(seqId,${seqRelationship.id.seqId})(globalSeqId,${seqRelationship.id.globalSeqId}) 
 
 
 Note: 
 ${seqRelationship.note}<br/><br/>
 
 
 Type: 
 ${seqRelationship.type}<br/><br/>
 
 
 GlobalSeq: 
 ${seqRelationship.globalSeq}<br/><br/>
 
 
 Seq: 
 ${seqRelationship.seq}<br/><br/>
 
</div>
