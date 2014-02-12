<%@ include file="/WEB-INF/include.jsp"  %>

<style type="text/css">


table.seqList,table.seqList tr,table.seqList th, table.seqList td {

	background: none;
	color: inherit;
	border: none;
	padding: 0px;
	margin: 0px;
	

}
table.seqList
{
border-bottom: 0px solid #ccc;
}
table.seqList th
{
border-bottom: 0px solid #ccc;

}
table.seqList td
{
border-left: 0px solid #ccc;
border-right: 0px solid #ccc;

}

</style>
<c:if test="${empty sfList}">
No sequences in list
</c:if>
<c:if test="${not empty sfList}">
<table class="seqList">
<thead><tr>
<th>
Length
</th>
<th>
Name
</th>
<th>
</th>
</tr>
</thead>
<tbody>
 <c:forEach items="${sfList}" var="item" varStatus="itemStatus" >
 <tr>
 <td>
${item.fragLength}
</td>
<td>
${seqNamesMap[item.seq.seqId]}
</td>
<td>


<a onclick="removeItem('${item.seqFragId}');" title="remove from list">x</a>
</td>
</tr>
 </c:forEach>
 </tbody>
 </table>

 </c:if>