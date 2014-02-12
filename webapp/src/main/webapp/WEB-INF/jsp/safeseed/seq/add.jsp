<%@ include file="/WEB-INF/include.jsp"  %>
<script type="text/javascript">

jQuery().ready(function($) {
   $("#addPasteButton").click(function(){$( "#pasteSeqDialog" ).dialog("open");});
   $("#addFileButton").click(function(){$( "#fileSeqDialog" ).dialog("open");});
   $("#addAidButton").click(function(){alert("Not implemented.  Try Paste or File"); 
   //$( "#aidSeqDialog" ).dialog("open");
   }
   );
   $("#addPublicButton").click(function(){alert("Not implemented.  Try Paste or File"); });
});

$(function() {
	// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
	$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
	
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
			updateTips( " Nothing has been entered..." );
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
	
	$( "#pasteSeqDialog" ).dialog({
		autoOpen: false,
		height: 500,
		width: 550,
		modal: true,
		buttons: {
			"Save": function() {
				var seq = $( "#seqTextPaste" );
				var bValid = true;

				bValid = bValid && notEmpty( seq);

				if ( bValid ) {
				
					//$("#filesequenceelement").html("<a title='"+seq.val()+"'>Sequence has been entered</a>");
					//$("#seqText").val(seq.val());
					$("#pasteSeqForm").submit();
					
					

				}
			},
			"Cancel ": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			allFields.val( "" ).removeClass( "ui-state-error" );
		}
	});
	
	
	$( "#fileSeqDialog" ).dialog({
		autoOpen: false,
		height: 300,
		width: 550,
		modal: true,
		buttons: {
			"Save": function() {
				var seq = $( "#seq_file" );
				var bValid = true;

				bValid = bValid && notEmpty( seq);

				if ( bValid ) {
				
				
					$("#fileSeqForm").submit();
					
					

				}
			},
			"Cancel ": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			allFields.val( "" ).removeClass( "ui-state-error" );
		}
	});
	
	$( "#aidSeqDialog" ).dialog({
		autoOpen: false,
		height: 300,
		width: 400,
		modal: true,
		buttons: {
			"Save": function() {
				var seq = $( "#accessionList" );
				var bValid = true;

				bValid = bValid && notEmpty( seq);

				if ( bValid ) {
				
				
				
					$("#aidSeqForm").submit();
					
					

				}
			},
			"Cancel ": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			allFields.val( "" ).removeClass( "ui-state-error" );
		}
	});
	
});

</script>
 
<div id="pasteSeqDialog" title="Paste Sequence(s)">
 <form action="savePasted.html" method="post" id="pasteSeqForm" enctype="multipart/form-data">
	<p class="validateTips">Please paste your sequence(s) in below</p>
	<textarea rows="15" cols="60" style="font-family:monospace;" name="seqTextPaste" id="seqTextPaste"></textarea>
	</form>
</div>

<div id="fileSeqDialog" title="Select Sequence File">
 <form action="saveFile.html" method="post"id="fileSeqForm" enctype="multipart/form-data">
 	<br/>
	<p class="validateTips">Please select your sequence file in below</p>

	<br/>
	<input type="file" name="seq_file" id="seq_file" size="50"/>

	</form>
</div>

<div id="aidSeqDialog" title="Enter Accession Numbers">
 <form action="saveAccession.html" method="post" id="aidSeqForm" enctype="multipart/form-data">
	<p class="validateTips">Please enter Accession numbers separated by a new line or comma</p>
	<textarea rows="5" cols="40" style="font-family:monospace;" name="accessionList" id="accessionList"></textarea>
	</form>
</div>


<h1>Add Sequence(s)</h1>
<div class="box">
<p>Please select a method to add the sequences:</p>
<div class="button">
<ul style="list-style: none;">

<li><a id="addPublicButton">Select</a> Add <strong>public</strong> sequence(s)</li>
<li><a id="addPasteButton">Select</a> <strong>Paste</strong> in sequence(s)</li>
<li><a id="addFileButton">Select</a> Add sequence(s) from <strong>file</strong></li>
<li><a id="addAidButton">Select</a> Add by <strong>accession</strong> number(s)</li>
</ul>
</div>



</div>
