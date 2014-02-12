<%@ include file="/WEB-INF/include.jsp"  %>
 Sequence Name:
 <br/> 
 ${col.name}
 <br/>
 <br/>
  Notes: 
 <br/>
 ${col.notes}
 <br/><br/>
 Sequence Original Name:
 <br/> 
 ${col.name}
 <br/><br/>
 siRNA Lengths (completed): 
 <ul>
 <c:forEach items="${col.seq.seqFrags}" var="item" varStatus="itemStatus" > 
 <c:if test="${item.completed==true }">
 <li>
<span style="color:#090">
${item.fragLength} 
</span>
 </li> 
</c:if>
 </c:forEach>
 </ul>
 siRNA Lengths (processing): 
 <ul>
 <c:forEach items="${col.seq.seqFrags}" var="item" varStatus="itemStatus" > 
 <c:if test="${item.completed==false }">
 <li>
<span style="color:#E60">
${item.fragLength} 
</span>
 </li> 
</c:if>
 </c:forEach>
 </ul>
 siRNA Lengths available for processing 
 <ul>
 <c:forEach items="${notProcessed}" var="item" varStatus="itemStatus" > 
 <li>
<span style="color:#E00">
 ${item}
 </span> <a href="processRequest.html?seqId=${col.seq.seqId}&length=${item}">(process)</a>
 
 </li>
 </c:forEach> 
 </ul>
 
 Sequence:<br/>
 <span title="Sequence" style="word-wrap: break-word; font-family: monospace;">
  ${col.seq.sequence}
  </span>
 
