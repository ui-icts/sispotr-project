<%@ include file="/WEB-INF/include.jsp"%>
<div style="height:500px; overflow: scroll;">
<c:if test="${fn:length(passList) >=1 }"> 
<table id="commonResultsTable">
<thead>
<tr>
<th>
Guide (Antisense)
</th>
<th>
Query ID
</th>
<th>
Start Position
</th>
<th>
POTS
</th>
</tr>
</thead>
<tbody>
<c:forEach items="${passList}" var="seq" >
<c:forEach items="${fragmentMap[seq]}" var="data" varStatus="fragstat">
<tr>
<c:if test="${fragstat.count==1}">
<td rowspan="${fn:length(fragmentMap[seq])}">
${seq}
</td>
</c:if>
<c:if test="${fragstat.count!=1}">

</c:if>

<td>
${data[0]}
</td>
<td>
${data[2]}
</td>
<td>
${data[3]}
</td>
</tr>    
</c:forEach>



</c:forEach>

</tbody>
</table>


<script>
$().ready(function(){ 
	 $("#commonResultsTable th").each(function(){
	 
	  $(this).addClass("ui-state-default");
	 
	  });
	 $("#commonResultsTable td").each(function(){
	 
	  $(this).addClass("ui-widget-content");
	 
	  });
	 $("#commonResultsTable tr").hover(
	     function()
	     {
	      $(this).children("td").addClass("ui-state-hover");
	     },
	     function()
	     {
	      $(this).children("td").removeClass("ui-state-hover");
	     }
	    );
	 $("#commonResultsTable tr").click(function(){
	   
	   $(this).children("td").toggleClass("ui-state-highlight");
	  });
	 
	}); 
</script>
</c:if>
<c:if test="${fn:length(passList) ==0 }">
<span style="">
No common guide (antisense) sequences found among input sequences.
</span>
</c:if> 
</div>
