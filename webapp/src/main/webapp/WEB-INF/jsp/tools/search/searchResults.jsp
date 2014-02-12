<%@ include file="/WEB-INF/include.jsp"%>
<c:if test="${ss.status==0}">
loading
<br/>
<c:forEach begin="0" end="${ss.readCount}" step="1">
.
</c:forEach>
<script>
setTimeout(	function(){loadResults("${ss.searchSeq}");}	,1000);
</script>

</c:if>

<c:if test="${ss.status==1}">
searching
<br/>
<c:forEach begin="0" end="${ss.readCount}" step="1"  >
.
</c:forEach>
<script>
setTimeout(	function(){loadResults("${ss.searchSeq}");}	,1000);
</script>
</c:if>

<c:if test="${ss.status==2}">
complete


<style>
.resultsTable 
{
width:250px;
}

.lside, .rside
{
background:#AAA;
}
.compRow td
{
text-align: center;
}

.searchRow td
{
text-align: center;
}

.resultsRow td
{
text-align: center;
}
</style>

<c:forEach items="${resultList}" var="result" varStatus="status">

${i.count}:
<br/>


<table class="resultsTable">
<tr>
<th colspan=6>
Result Accession Id: ${result.searchResult.resultAccession}
</th>
<th colspan=8>
Result Accession Offset: ${result.searchResult.resultAccessionOffset}
</th>
<th colspan=6>
Match Accuracy: ${result.matchAccuracy}
</th>
</tr>

<tr class="searchRow">
<td class="lside">
</td>

<c:forEach items="${result.searchChars}" var="c">
<td>
${c}
</td>
</c:forEach>
<td class="rside">
</td>

</tr>
<tr class="compRow">
<td class="lside">
</td>
<c:forEach items="${result.comp}" var="c">
<td>
<c:if test="${c==true}">
|
</c:if>

</td>
</c:forEach>
<td class="rside">
</td>

</tr>
<tr class="resultRow">
<td class="lside">
5'
</td>
<c:forEach items="${result.resultChars}" var="c">
<td>
${c}
</td>
</c:forEach>
<td class="rside">
3'
</td>

</tr>

</table>

</c:forEach>
</c:if>


