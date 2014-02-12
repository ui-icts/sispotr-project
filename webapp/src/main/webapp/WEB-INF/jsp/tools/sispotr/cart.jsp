<%@ include file="/WEB-INF/include.jsp"%>
<div class="btn-toolbar">
<div class="btn-group">

<a id="viewCartButton" class="btn">
<i class=" icon-shopping-cart"></i>
View shRNA Cart
<c:if test="${not empty cart}"> 
(${cart.cartSize} items) 
</c:if>
<c:if test="${empty cart}">
 (empty)
</c:if>
</a>
<c:if test="${not empty cart}"> 
<a class="btn btn-success" href="shRNABuilder.html" >
Create shRNA
</a>

<a class="btn btn-danger" href="displayResults.html?type=SHRNA&clearCart=true">
Clear Cart
</a>
</c:if>

</div>
</div>
<div id="cartDialog" title="shRNA Design Cart" >
<table id="carttable" class="table" >
<thead>
<tr>
<th>Item</th>
<th>Seq</th>
<th>Start Position</th>
<th>POTS</th>
</tr>
</thead>
<tbody>
<c:forEach items="${cart.itemList}" var="item" varStatus="status0">
<tr>
<td> ${status0.count}</td>
<td> ${item.attribMap["aid"]}</td>
<td> ${item.attribMap["start_pos"]}</td>
<td> ${item.attribMap["pots"]}</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>

<br/>
<strong>Instructions:</strong> Please click add button next to desired sequence to add to shRNA Design Cart.
<script>
$("#carttable").dataTable( {
	
    bFilter: false,
   // "bRetrieve":true,
   
    "bScrollCollapse" : true,
    "bPaginate": false
 //   "aaSorting": [[ 2, "asc" ]],                                // sor the first col asc on initialization
//     "bJQueryUI": true                                          // use the JQueryUI                   
// pagination styl
  //"aoColumns" : [{ "bSortable":false }, null,null,null,null,null,null,null,null,null,null]
} );
$( "#cartDialog" ).dialog({
	autoOpen: false,
  modal: true,
  width: 500,
  buttons: {
    "Start Designer": function() {
    	 window.location = "shRNABuilder.html"
    },
    "Clear Cart": function() {
   	 clearCart();
   	$( this ).dialog( "close" );
   },
    "Back": function() {
      $( this ).dialog( "close" );
    }
  }
});

$("#viewCartButton").click(function(){
 $( "#cartDialog" ).dialog("open");
});
  $("a", ".button").button();
  $( "#cartDialog" ).dialog("close");

</script>