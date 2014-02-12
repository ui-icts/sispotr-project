<%@ include file="/WEB-INF/include.jsp"  %>
<script type="text/javascript">
function showSeqDialog(seqId)
{
	$.ajax(
			{
				type: "GET",
				dataType: "html",
				url: "showSeqDetails.html",
				data: "seqId="+seqId,
				success: function(data){
				
					$( "#displaySeq" ).html(data);
					$( "#displaySeq" ).dialog("open");
					
				}
			});
	
}

function selectSeqLengthDialog(seqId)
{
	
	$.ajax(
			{
				type: "GET",
				dataType: "html",
				url: "selectSeqLength.html",
				data: "seqId="+seqId,
				success: function(data){
				
					$( "#lengthContainer" ).html(data);
					$( "#selectSeqLength" ).dialog("open");
					
				}
			});
	
}

function addNewItem()
{
	
	var newVal = $("#lengthSelectValue").val();
	//alert('here'+$("#lengthSelectValue").val());
	if(newVal!=null && newVal !="")
		{
		
		var vals = $("#viewList").val().split(",");
		var exists=false;
		for(var i=0;i<vals.length;i++)
			{
			if(vals[i]==newVal)
				{
					exists=true;
					alert("This is already in list");
					return;
				
				}
			}

			if($("#viewList").val()!="")
				$("#viewList").val($("#viewList").val()+","+newVal);
		else
			$("#viewList").val(newVal);
		}
	
		loadViewList();
	$( "#selectSeqLength" ).dialog("close");
}

function removeItem(sf)
{
	
	
		var vals = $("#viewList").val().split(",");
		
		var newList = "";
		for(var i=0;i<vals.length;i++)
			{
			if(vals[i] != sf )
				{
				if(newList=="")
					{
					
					newList=vals[i];
					}
				else
					newList+=","+vals[i];
				
				}
			}
	
		
		$("#viewList").val(newList);
	
		loadViewList();
	
}

function loadViewList()
{
	
	$.ajax(
			{
				type: "GET",
				dataType: "html",
				url: "loadViewList.html",
				data: "seqLengths="+$("#viewList").val(),
				success: function(data){
				
					$( "#viewListDisplay" ).html(data);
					
					
				}
			});

}

$(function() {
	// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
	$( "#dialog:ui-dialog" ).dialog( "destroy" );
	$( "#displaySeq" ).dialog({
		autoOpen: false,
		height: 500,
		width: 443,
		modal: true
	});

	
	var tips = $( ".validateTips" );
	var allFields = $( [] );
	function updateTips( t ) {
		tips
			.text( t )
			.addClass( "ui-state-highlight" );
		setTimeout(function() {
			tips.removeClass( "ui-state-highlight", 1500 );
		}, 500 );
	}

	function checkLength( o, n, min, max ) {
		if ( o.val().length > max || o.val().length < min ) {
			o.addClass( "ui-state-error" );
			updateTips( "Length of " + n + " must be between " +
				min + " and " + max + "." );
			return false;
		} else {
			return true;
		}
	}
	function notEmpty( o) {

		if ( o.val() == undefined || o == null || o.val() == '' ) {
			
			o.addClass( "ui-state-error" );
			updateTips( " Nothing has been selected..." );
			return false;
		} else {
			return true;
		}
	}

	function checkRegexp( o, regexp, n ) {
		if ( !( regexp.test( o.val() ) ) ) {
			o.addClass( "ui-state-error" );
			updateTips( n );
			return false;
		} else {
			return true;
		}
	}
	$( "#selectSeqLength" ).dialog({
		autoOpen: false,
		height: 130,
		width: 330,
		modal: true
		
	});
	
	$( "#enterParamsDialog" ).dialog({
		autoOpen: false,
		height: 400,
		width: 800,
		modal: true
		
	});
	
	//buttons: {
	//	"Add to list": function() {
	//		var length = $( "#lengthSelectValue" );
	//		var bValid = true;

	//		bValid = bValid && notEmpty( length);

	//		if ( bValid ) {
			
		//		alert(length.val());
				//$("#filesequenceelement").html("<a title='"+seq.val()+"'>Sequence has been entered</a>");
				//$("#seqText").val(seq.val());
	//			$( this ).dialog( "close" );
				
				

//			}
	//	},
		//"Cancel ": function() {
	//		$( this ).dialog( "close" );
//		}
//	},
//	close: function() {
		
//	}

	
});




</script>

<div id=displaySeq title="Sequence" >
	Error
</div>

<div id="selectSeqLength" title="siRNA length">
<p class="validateTips"></p>
	<div id="lengthContainer">
	</div>
</div>

<div id="enterParamsDialog" title="Computing Xmer scores">
<p class="validateTips"></p>
	<div id="enterParamsContainer">
	<%@ include file="enterParams_partial.jsp"%>
	
	</div>
</div>

<h1>Sequence Collection</h1>

<div class="grid_12" >
<div class="box">
<span class="button" style="float:left;">

<a href="add.html" title="add new sequences to your collection by selecting from a file, pasting, searching, etc">Add New Sequences</a>
<a href="<c:url value="/safeseed/seq/submissions.html"  />" title="this will display your requests for a sequence to be processesed with a specific siRNA length">View Process Sequence Request History</a>
</span>
<br/> <br/> <br/>
<strong>siRNA Length Legend</strong>
<br/>
<span style="color:#090">##</span> = completed
<br/> 
<span style="color:#E60">##</span> = processing 
<br/>
<br/>

</div>
</div>
<div class="clear"></div>
<div class="grid_3">
<div class="box">
<h4>View List</h4>
<div id="viewListDisplay">
No Sequences Added
</div>
<br/>
 <form action="loadViewList.html" method="post" >
 <input id="viewList" name="viewList" type="hidden" value=""/>
 <span class="button" style="float:left;">
<button type="button" id="enterParamsDialogButton">View Now</button>
</span>
 </form>
<br/>


</div>
</div>
<div class="grid_9">

   <div id="tabs" align="left">
                <ul>
                  	<li><a href="#customtab">Custom Sequences</a></li>
                  	<li><a href="#publictab">Saved Public Sequences</a></li>
                  	<li><a href="#grouptab">Group Sequences</a></li>
                   	
                </ul>
           
    <div id="customtab">
<table class="tableData1">
<thead>
<tr>
<th></th>
<th>Sequence Name</th>
<th>Species</th>
<th>siRNA Lengths </th>
<th></th>

</tr>
</thead>
<tbody>
<c:forEach items="${collection}" var="item"  varStatus="status"><tr>
<td>


<div class="button">
<a onclick="selectSeqLengthDialog('${item.seq.seqId}');"><strong>&larr;</strong></a>
</div>
</td>
<td>${item.name}</td>
<td>${item.seq.species}</td>
<td>
<c:forEach items="${item.seq.seqFrags}" var="length">
<c:if test="${length.completed==true }">
<span style="color:#090">
${length.fragLength} 
</span>
</c:if>
</c:forEach>
<c:forEach items="${item.seq.seqFrags}" var="length">
<c:if test="${length.completed==false}">
<span style="color:#E60">
${length.fragLength}
</span> 
</c:if>
</c:forEach>
</td>
<td>
<div class="button">
<a onclick="showSeqDialog('${item.seq.seqId}')">details</a>
</div>
</td>
</tr>
</c:forEach>
</tbody>
</table>

</div>
<div id="publictab">
NOT IMPLEMENTED

</div>
<div id="grouptab">
NOT IMPLEMENTED
<br/>
Select group....then show groups sequences  or show all group sequences and show group name
</div>
</div>
</div>

<script type="text/javascript">
$("#tabs").tabs();
setDataTable(".tableData1","sortable");
$('#enterParamsDialogButton').click(
		function()
		{
			$('#enterParamsDialog').dialog('open');
		}
		);
loadViewList();
</script>
