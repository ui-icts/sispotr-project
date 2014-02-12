<%@ include file="/WEB-INF/include.jsp"  %>
<h1>PersonGroup</h1>
<div class="box">

<h2> ${personGroupId.personId}  ${personGroupId.groupId} </h2>

 
 PersonGroupId: 
(personId,${personGroup.id.personId})(groupId,${personGroup.id.groupId}) 
 
 
 Role: 
 ${personGroup.role}<br/><br/>
 
 
 Group: 
 ${personGroup.group}<br/><br/>
 
 
 Person: 
 ${personGroup.person}<br/><br/>
 
</div>
