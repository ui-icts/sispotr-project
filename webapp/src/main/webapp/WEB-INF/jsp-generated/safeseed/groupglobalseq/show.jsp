<%@ include file="/WEB-INF/include.jsp"  %>
<h1>GroupGlobalSeq</h1>
<div class="box">

<h2> ${groupGlobalSeqId.groupId}  ${groupGlobalSeqId.globalSeqId} </h2>

 
 GroupGlobalSeqId: 
(groupId,${groupGlobalSeq.id.groupId})(globalSeqId,${groupGlobalSeq.id.globalSeqId}) 
 
 
 Label: 
 ${groupGlobalSeq.label}<br/><br/>
 
 
 Notes: 
 ${groupGlobalSeq.notes}<br/><br/>
 
 
 DateAdded: 
 ${groupGlobalSeq.dateAdded}<br/><br/>
 
 
 Group: 
 ${groupGlobalSeq.group}<br/><br/>
 
 
 GlobalSeq: 
 ${groupGlobalSeq.globalSeq}<br/><br/>
 
</div>
