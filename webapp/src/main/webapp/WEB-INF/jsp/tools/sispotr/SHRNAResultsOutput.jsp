<%@ include file="/WEB-INF/include.jsp"%>


<div class="row-fluid">

<div class="span11">
<h1>shRNA Results</h1>
<div class="btn-group">
 
<!-- <a  class="btn" href="shRNABuilder.html"> -->
<!-- back to shRNA Designer -->
<!-- </a> -->
<a  class="btn" href="displayResults.html?type=SHRNA">
shRNA Results 
</a>
<a  class="btn" href="design.html">
Add Sequences
</a>
</div>

<br/>

<table class="table" id="shrnaResultsTable"> 
<thead >
<tr>
<th>
Seq ID
</th>
<th>
Start Position
</th>
<th>
POTS
</th>
<th>
Primers
</th>
<th>
mouse U6 Forward
</th>
</tr>
</thead>
<tbody>
<c:forEach  items="${shrnaList}"  var="shrna" varStatus="status0">
<tr>
<td>
${shrna.item.attribMap['aid']}
</td>
<td>
${shrna.item.attribMap['start_pos']} 
</td>
<td>
${shrna.item.attribMap['pots']}

</td>
<td>
<!-- <strong>Forward Primer 1 and 2</strong><br/> -->
<%-- ${shrna.fwdPrimer1} <br/> --%>
<%-- ${shrna.fwdPrimer2} --%>
<!-- <br/><br/> -->
<strong>Reverse Cloning Primer</strong><br/>
${shrna.reversePrimer1}<br/>
 ${shrna.reversePrimer2}


</td>
<td>
CGACGCCGCCATCTCTAG

</td>

    
</tr>
</c:forEach>
</tbody>
</table>
</div>
</div>

<script>
$("#shrnaResultsTable").dataTable( {
	
    bFilter: false,
   
    "bScrollCollapse" : true,
    "bPaginate": false

} );


</script>	
