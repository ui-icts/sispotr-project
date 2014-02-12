<%@ include file="/WEB-INF/include.jsp"  %>
<script type="text/javascript">
function showSeqDialog(seqId)
{
	$.ajax(
			{
				type: "GET",
				dataType: "html",
				url: "showSequence.html",
				data: "seqId="+seqId,
				success: function(data){
				
					$( "#displaySeq" ).html(data);
					$( "#displaySeq" ).dialog("open");
					
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

	

	
});


</script>
<div id=displaySeq title="Sequence" style="word-wrap: break-word; font-family: monospace;">
	Error
</div>

<h1>Submitted Sequence Details</h1>
<div class="grid_8">
<div class="box">
<form action="updateSequenceDetails.html" method="post" enctype="multipart/form-data">
<c:forEach items="${seqList}" var="seq"  varStatus="status">
<div style="margin-bottom:15px;padding:3px;">
<fieldset class="clear">
<legend>Sequence ${status.count}</legend>
<table class="clear" >
<tr>
<td>
<label for="length_${seq.seqId}" style="font-weight: bold;">siRNA Length:</label>
<br/>
<input id="length_${seq.seqId}" name="length_${seq.seqId}" class="sirna_length" type="text" size="4" value=""/>
<br/>
<br/>
<label for="species_${seq.seqId}" style="font-weight: bold;">Species:</label>
<br/>
<select id="species_${seq.seqId}" name="species_${seq.seqId}">
<option value="human">human</option>
<option value="human">[not implemented]</option>
</select>
<br/>
<br/>
<div class="button">
<a id="button" onclick="showSeqDialog('${seq.seqId}');">Show Sequence</a>
</div>
</td>
<td>
<label for="name_${seq.seqId}" style="font-weight: bold;" >Label:</label>
<br/>
<input id="name_${seq.seqId}" name="name_${seq.seqId}" type="text" value="${seq.name}" size="30"/>
<br/>
<br/>
<label for="description_${seq.seqId}" style="font-weight: bold;">Description:</label><br/>
<textarea id="description_${seq.seqId}" cols="40" rows="5" name="description_${seq.seqId}">${seq.description}</textarea>
</td>
</tr>
</table>
</fieldset>
</div>
</c:forEach>
<input type="submit" value="Save"/>
</form>
</div>
</div>
<div class="grid_4">
<div class="box">
<p><strong>Instructions:</strong>
At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.
 </p>
</div>
</div>
<script type="text/javascript">
$('.sirna_length').spinner({step: 1, min: 19, max: 30 });
</script>
