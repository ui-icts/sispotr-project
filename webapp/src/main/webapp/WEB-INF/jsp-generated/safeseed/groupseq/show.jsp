<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GroupSeq</h1>
<div class="box">

<h2> ${groupSeqId.groupId}  ${groupSeqId.seqId} </h2>

 
 GroupSeqId: 
(groupId,${groupSeq.id.groupId})(seqId,${groupSeq.id.seqId}) 
 
 
 Name: 
 ${groupSeq.name}<br/><br/>
 
 
 Notes: 
 ${groupSeq.notes}<br/><br/>
 
 
 DateAdded: 
 ${groupSeq.dateAdded}<br/><br/>
 
 
 Group: 
 ${groupSeq.group}<br/><br/>
 
 
 Seq: 
 ${groupSeq.seq}<br/><br/>
 
</div>
