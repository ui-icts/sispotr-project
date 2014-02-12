<%@ include file="/WEB-INF/include.jsp"%>
<h1>shRNA Designer</h1>
<style type="text/css">
#builderDiv
{
border:1px solid #aaa;
background:#cdcdcd;
border-radius: 5px;
padding:0px;
}

#builderDiv button
{
font-weight: bold;
margin-left:10px;
margin-bottom:10px
}
#builder
{
border:0px solid #ccc;
background:none;


}
#builder thead tr th
{
border:0px solid #bbb;
background:none;
text-align: center;
font-weight: normal;
}
#builder tbody tr td
{
text-align: center;
border:1px solid #999;
background-color:#aaa;
padding-top:5px;
padding-bottom:5px;
}
</style>
<div class="btn-group">
 
<a class="btn" href="displayResults.html?type=SHRNA">
Back to shRNA Results 
</a>

</div>
<div class="row-fluid">
<div class="span11">
<h3>shRNA Template</h3>




<form action="generateSHRNA.html">
<div id="builderDiv">

<table class="table" id="builder" >
<thead>
<tr>
<th>
Start Sequence
</th>
<th>

</th>
<th>
Loop Sequence
</th>
<th>

</th>
<th>
End Sequence
</th>
</tr>
</thead>
<tbody>
<tr>
<td>
<select name="start_seq">
<option>
CCCTTGGAGAAAAGCCTTGTTT
</option>
</select>
</td>
<td style="white-space:nowrap">
Passenger (sense)
</td>
<td>
<select name="loop_seq">
<option>
CTGTAAAGCCACAGATGGG
</option>
</select>
</td>
<td style="white-space:nowrap">
Antisense (guide)
</td>
<td>
<select name="end_seq">
<option>
TTTTTTctcgag
</option>
</select>

</td>
</tr>
</tbody>
</table>

<button class="btn btn-primary" type="submit">Generate shRNA from selected results</button>
</div>


</form>

<br/>
</div>
</div>
<div class="row-fluid">
<div class="span11">
<h3>Selected Results</h3>
<table class="table"  id="carttable" >
<thead>
<tr>
<th>Item</th>
<th>Seq</th>
<th>Start Position</th>
<th>POTS</th>
<th>Antisense (guide)</th>
<th>Passenger (sense)</th>
<th>Seed</th>
</tr>
</thead>
<tbody>
<c:forEach items="${cart.itemList}" var="item" varStatus="status0">
<tr>
<td> ${status0.count}</td>
<td> ${item.attribMap["aid"]}</td>
<td> ${item.attribMap["start_pos"]}</td>
<td> ${item.attribMap["pots"]}</td>
<td> ${item.attribMap["guide"]}</td>
<td> ${item.attribMap["passenger"]}</td>
<td> ${item.attribMap["seed"]}</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>
</div>
<script>
$("#carttable").dataTable( {
	
    bFilter: false,
   // "bRetrieve":true,
    "bScrollCollapse" : true,
    "bPaginate": false
 //   "aaSorting": [[ 2, "asc" ]],                                // sor the first col asc on initialization
  
  //"aoColumns" : [{ "bSortable":false }, null,null,null,null,null,null,null,null,null,null]
} );

</script>