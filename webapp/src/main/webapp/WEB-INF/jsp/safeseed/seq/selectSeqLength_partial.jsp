<%@ include file="/WEB-INF/include.jsp"  %>
<c:set var="hasCompleted" value="false"/>
 <c:forEach items="${col.seq.seqFrags}" var="item" varStatus="itemStatus" > 
 <c:if test="${item.completed==true }">
  <c:set var="hasCompleted" value="true"/>
 </c:if>
 </c:forEach>
 
  <c:if test="${hasCompleted==true }">
  	Select a siRNA length: 
 <select id="lengthSelectValue">
 <c:forEach items="${col.seq.seqFrags}" var="item" varStatus="itemStatus" > 
 <c:if test="${item.completed==true }">
 <option value="${item.seqFragId}">${item.fragLength}</option>
 </c:if>
 </c:forEach>
 </select>
 <button onclick="addNewItem()" >add to list</button>
 </c:if>
 
 <c:if test="${hasCompleted==false }">
 Sorry, no siRNA lengths have completed processing. Please try again after a length has finished computing. 
 </c:if>

