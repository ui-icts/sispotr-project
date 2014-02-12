<%@ include file="/WEB-INF/include.jsp"  %>
<h1>Collection</h1>
<div class="box">

<h2> ${collectionId.personId}  ${collectionId.seqId} </h2>

 
 CollectionId: 
(personId,${collection.id.personId})(seqId,${collection.id.seqId}) 
 
 
 Name: 
 ${collection.name}<br/><br/>
 
 
 Notes: 
 ${collection.notes}<br/><br/>
 
 
 DateAdded: 
 ${collection.dateAdded}<br/><br/>
 
 
 Person: 
 ${collection.person}<br/><br/>
 
 
 Seq: 
 ${collection.seq}<br/><br/>
 
</div>
