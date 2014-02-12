<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SearchStatus</h1>
<div class="box">

<h2> ${searchStatus.searchSeq} </h2>

 
 SearchSeq: 
 ${searchStatus.searchSeq}<br/><br/>
 
 
 SearchSeqFull: 
 ${searchStatus.searchSeqFull}<br/><br/>
 
 
 Status: 
 ${searchStatus.status}<br/><br/>
 
 
 EntryTime: 
 ${searchStatus.entryTime}<br/><br/>
 
 
 CheckoutTime: 
 ${searchStatus.checkoutTime}<br/><br/>
 
 
 FinishTime: 
 ${searchStatus.finishTime}<br/><br/>
 
 
 ReadCount: 
 ${searchStatus.readCount}<br/><br/>
 
 
 SearchResults: 
 <ul><c:forEach items="${searchStatus.searchResults}" var="item" varStatus="itemStatus" > <li><a href="../searchresult/edit.html?searchResultId=${item.searchResultId}" > ${item.searchResultId}</a></li> </c:forEach></ul><br/><br/>
 
</div>
