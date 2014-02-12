<%@ include file="/WEB-INF/include.jsp"%>
<script type="text/javascript">

jQuery().ready(function($) {
    $('#spinnerXmer').spinner({step: 1, min: 19, max: 30 });
//     $('#spinnerNumber').spinner({step: 1, min: 1, max: 10000000 });
    //$('#spinnercurrency').spinner({prefix: '$', group: ',', step: 0.01, largeStep: 1, min: -1000000, max: 1000000});
    $('#spinnerGCMin').spinner({step: 1, min: 0, max: 100});
    $('#spinnerGCMax').spinner({step: 1, min: 0, max: 100});
    $('#spinnerGCReq').spinner({step: 1, min: 0, max: 2});
    if (jQuery.browser.msie) {
    	   jQuery('span.ui-spinner').css('vertical-align', 'middle').css('top', '-6px');
    	}
   
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
	
	$( "#pasteSeq" ).dialog({
		autoOpen: false,
		height: 650,
		width: 550,
		modal: true,
		buttons: {
			"Save": function() {
				var seq = $( "#seqTextPaste" );
				var seqstring = seq.val();
				seqstring= seqstring.replace(/\n\n/g,'\n');
				seqstring= seqstring.replace(/\r/g,'');
				var bValid = true;

				bValid = bValid && notEmpty( seq);

				if ( bValid ) {
					
					
				
					$("#selectionStatus").html("<img src='<c:url value="/"/>resources/images/checkbox.png' /> Sequence(s) entered.  Approximately "+seqstring.length+" bases");
					$("#seqText").val(seqstring);
					//alert(seqstring);
					$("#filesequence").val("");
					$( this ).dialog( "close" );
					
					

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


$('#filesequence').live('change',
		function()
		{
	$("#selectionStatus").html("File Selected: "+ $('#filesequence').val());
	 
		}
	);





</script>

<style>
<!--
-->
.left_col
{
float:left;
width:50%;
}
.right_col
{
float:right;
width:50%;
}
table.params td {
	padding: 5px;
}

.shortbutton {
	width: 100px;
	height: 70px;
  white-space: normal;
  
}

#selectionStatus
{
margin-top:10px;
padding:10px;
background:#FFFFCC;
border: 1px solid #FFCC33;
}

#pastesequenceelement{
float:left;
}

#pastesequenceelement{
float:left;

}

#samplesequenceelement{
floaf:right;


}
</style>
<h1><span class="accentedfont">si</span>RNA/<span class="accentedfont">sh</span>RNA Design Tool</h1>
   <div  class="row-fluid">
  <div class="span7 well">
	<h2>Enter Query Sequence</h2>

	<form action="addNewSequences.html" method="post" name="seq"	id="seqform" enctype="multipart/form-data">


		<div id="pasteSeq" title="Paste Sequence"  >
			<p class="validateTips"></p>
			
				<label for="seqTextPaste">Please paste in one or more sequences below.<br />
				<br /> <strong>NOTE:</strong>The first line for each sequence should
				begin with a header that starts with "&gt;" <br />
				<br />
				<strong>Examples:</strong> <br />&gt;gi|gi-number|from|accession2|locus<br />...<br />&gt;mysequence1<br />...
				</label>
			
			<textarea class="text input-xlarge" rows="15" style="font-family: monospace;"
				name="seqTextPaste" id="seqTextPaste"></textarea>
		</div>

		<fieldset class="config">

      <legend>Sequence</legend>
			<input type="hidden" name="htmlFormName" id="" value="submit" /> 
            <input type="hidden" name="seqText" id="seqText" />
             <p>
              Enter	Sequence(s) using a method below (Plain for FASTA format).<br/>

</p> 
	<c:if test="${not empty errorMessage }">
	<div class="error">
	${errorMessage}
	</div>
	</c:if>
<br/>
        <div id="inputButtonContainer">
          <div class="btn-toolbar">
        <div class="btn-group">
<!--         			<span id="pastesequenceelement"> -->
				<button type="button" id="selectPasteButton" class="btn btn-large btn-info" >paste in sequence(s)</button>					
<!-- 			</span> -->
<!-- 			<span id="samplesequenceelement"> -->
				<button type="button" id="useSampleButton" class="btn btn-large">use sample sequences</button> 
<!-- 			</span> -->
			</div>
			</div>	
			</div>
			
	    	<br/>	 
			<span id="filesequenceelement">
				<label	for="filesequence">Sequence from file:</label> 
				<input name="fseq" id="filesequence" type="file" class="input-file" value="Browse for file"> 
				</span>
				<div id="selectionStatus" style="">
				<strong >No sequence selected. Please enter at least one sequence.</strong>
				</div>
			</fieldset> 

			<fieldset class="config">
		<legend>Parameters</legend>
	<div class="left_col">
	
	
	
	
	
	<input type="hidden" name="xmer" id="xmer" value="21" size="8" />
	
	
         <p>Type: <a rel="tooltip" data-original-title="Parameter: Type" data-content="siRNA or shRNA.  If shRNA is selected, the Pol III parameter is required" id="type_tip"><i class="icon-question-sign"></i></a></p>       
      <label class="checkbox" for="designType1"> <input type=radio  class="checkbox" name="designType" id="designType1" value="sirna" checked="checked"/> siRNA </label> 
              
      <label class="checkbox" for="designType2"> <input type=radio  class="checkbox" name="designType" id="designType2" value="shrna"/>  shRNA </label>
                
            
       <script>
       $("#type_tip").popover();
       </script>       

       
      <br/>
          <br/>
  
    <label for="species">Species:</label>
      <select name="species" id="species">
      <option selected="selected">human</option>
      <option>mouse</option>
      </select>
        <br /> <br />
	<label for="resultsCount">Number of Results: </label>
			<select name="number" id="resultsCount">
			<option>10</option>
			<option selected="selected">25</option>
			<option>50</option>
			<option>100</option>
			</select>
			<br /> <br />
			

            
			
			
			
            <label for="spinnerGCReq">How many G/Cs required at 5' end	of Passenger? <a rel="tooltip" 
                        data-original-title="Parameter: # G/C at 5' end" 
                        data-content="Increases stability (decreases loading) of passenger strand. Default=2." 
                        id="gc_tip">
                        <i class="icon-question-sign"></i></a></label>
        	<input type="text" name="required" id="spinnerGCReq"	value="2" size="8" />    
       <script>
       $("#gc_tip").popover();
       </script> 

      
    <br /> 


			</div>
			<div class="right_col">
						<label for="spinnerGCMin">G/C Content% Min
						<a rel="tooltip" 
                        data-original-title="Parameter: G/C Content % min" 
                        data-content="Lower limit of G/C content. Default=20" 
                        id="gcmin_tip">
                        <i class="icon-question-sign"></i></a> 
						</label>
			
			<input type="text" name="gcmin" id="spinnerGCMin" value="20" size="8" />
			                        
       <script>
       $("#gcmin_tip").popover();
       </script> 
		
		
			<br/>		
						<br />
						
				<label for="spinnerGCMax">G/C Content% Max
				  <a rel="tooltip" 
                        data-original-title="Parameter: G/C Content % max" 
                        data-content="Upper limit of G/C content. Default=70" 
                        id="gcmax_tip">
                        <i class="icon-question-sign"></i></a>
				</label>
			<input type="text" name="gcmax" id="spinnerGCMax" value="70" size="8" />
			
						                       
       <script>
       $("#gcmax_tip").popover();
       </script> 
			
			<br/>
			<br/>

 
       


</div>
<br style="clear:both;"/>

     <br/>
     <span id="pol3Box">
      <input type="checkbox"  class="checkbox" name="pol3" id="pol3" value="true"/> 
      <label class="checkbox" for="pol3">Design for Pol III expression 
<a rel="tooltip" 
                        data-original-title="Parameter: Pol III" 
                        data-content="Remove sequences with stretches of four or more T's/U's" 
                        id="pol3_tip">
                        <i class="icon-question-sign"></i></a>      
      </label> 
      
      						                        
       <script>
       $("#pol3_tip").popover();
       </script> 
       </span>
      <br/>
      <br/>
      <button class="btn btn-large btn-success" type="submit" >Run siSPOTR</button>





      
		</fieldset>
    
    <br/>
      
    
	</form>

</div>

<div class="span4">

<c:if test="${fn:length(seqManager.seqs)>0}">
<div class="well">
<h2>Loaded Sequences</h2>
<ul>


          <c:forEach
        items="${seqManager.seqs}"
        var="seq"
        varStatus="status0">
    <li>
   
      <c:forEach
        items="${seq.xmersList}"
        var="x"
        varStatus="status">
       ${x.seq.label} - ${x.seq.species}, ${x.xmerType} 
    </c:forEach>
   
    </li>
    </c:forEach>

</ul>
<br/>
    </div>
    </c:if>

    <div class="well">
 ${siteContentMap['designpage.overview']}
 <sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=designpage.overview">edit</a>
</sec:authorize>


</div>
</div>
</div>


<script type="text/javascript">
$("#gat3_yes").attr("checked","checked");
$("#gat2_yes").attr("checked","checked");
$("#designType1").attr("checked","checked");
$("#designType2").click(
		function(){
			$("#pol3").attr("checked","checked");
			$("#pol3").attr("disabled", "disabled");
			$("#xmer").val(22);

		}
		);
		
$("#designType1").click(
		function(){
			$("#pol3").removeAttr("checked");
			$("#pol3").removeAttr("disabled");
			$("#xmer").val(21);

		}
		);


$("#selectPasteButton").click(
		function(){
			$("#pasteSeq").dialog("open");
			 $( "#seqTextPaste" ).val( $( "#seqText" ).val());
		}
		);
$("#useSampleButton").click(
		function(){
			
			var input = ">VEGF\nCTGACGGACAGACAGACAGACACCGCCCCCAGCCCCAGCTACCACCTCCTCCCCGGCCGGCGGCGGACAGTGGACGCGGCGGCGAGCCGCGGGCAGGGGCCGGAGCCCGCGCCCGGAGGCGGGGTGGAGGGGGTCGGGGCTCGCGGCGTCGCACTGAAACTTTTCGTCCAACTTCTGGGCTGTTCTCGCTTCGGAGGAGCCGTGGTCCGCGCGGGGGAAGCCGAGCCGAGCGGAGCCGCGAGAAGTGCTAGCTCGGGCCGGGAGGAGCCGCAGCCGGAGGAGGGGGAGGAGGAAGAAGAGAAGGAAGAGGAGAGGGGGCCGCAGTGGCGACTCGGCGCTCGGAAGCCGGGCTCATGGACGGGTGAGGCGGCGGTGTGCGCAGACAGTGCTCCAGCCGCGCGCGCTCCCCAGGCCCTGGCCCGGGCCTCGGGCCGGGGAGGAAGAGTAGCTCGCCGAGGCGCCGAGGAGAGCGGGCCGCCCCACAGCCCGAGCCGGAGAGGGAGCGCGAGCCGCGCCGGCCCCGGTCGGGCCTCCGAAACCATGAACTTTCTGCTGTCTTGGGTGCATTGGAGCCTTGCCTTGCTGCTCTACCTCCACCATGCCAAGTGGTCCCAGGCTGCACCCATGGCAGAAGGAGGAGGGCAGAATCATCACGAAGTGGTGAAGTTCATGGATGTCTATCAGCGCAGCTACTGCCATCCAATCGAGACCCTGGTGGACATCTTCCAGGAGTACCCTGATGAGATCGAGTACATCTTCAAGCCATCCTGTGTGCCCCTGATGCGATGCGGGGGCTGCTGCAATGACGAGGGCCTGGAGTGTGTGCCCACTGAGGAGTCCAACATCACCATGCAGATTATGCGGATCAAACCTCACCAAGGCCAGCACATAGGAGAGATGAGCTTCCTACAGCACAACAAATGTGAATGCAGACCAAAGAAAGATAGAGCAAGACAAGAAAAAAAATCAGTTCGAGGAAAGGGAAAGGGGCAAAAACGAAAGCGCAAGAAATCCCGGTATAAGTCCTGGAGCGTGTACGTTGGTGCCCGCTGCTGTCTAATGCCCTGGAGCCTCCCTGGCCCCCATCCCTGTGGGCCTTGCTCAGAGCGGAGAAAGCATTTGTTTGTACAAGATCCGCAGACGTGTAAATGTTCCTGCAAAAACACAGACTCGCGTTGCAAGGCGAGGCAGCTTGAGTTAAACGAACGTACTTGCAGATGTGACAAGCCGAGGCGGTGA";
			// $( "#seqTextPaste" ).val("
			 input = input + "\n\n"+">EGFR\nATGCGACCCTCCGGGACGGCCGGGGCAGCGCTCCTGGCGCTGCTGGCTGCGCTCTGCCCGGCGAGTCGGGCTCTGGAGGAAAAGAAAGTTTGCCAAGGCACGAGTAACAAGCTCACGCAGTTGGGCACTTTTGAAGATCATTTTCTCAGCCTCCAGAGGATGTTCAATAACTGTGAGGTGGTCCTTGGGAATTTGGAAATTACCTATGTGCAGAGGAATTATGATCTTTCCTTCTTAAAGACCATCCAGGAGGTGGCTGGTTATGTCCTCATTGCCCTCAACACAGTGGAGCGAATTCCTTTGGAAAACCTGCAGATCATCAGAGGAAATATGTACTACGAAAATTCCTATGCCTTAGCAGTCTTATCTAACTATGATGCAAATAAAACCGGACTGAAGGAGCTGCCCATGAGAAATTTACAGGAAATCCTGCATGGCGCCGTGCGGTTCAGCAACAACCCTGCCCTGTGCAACGTGGAGAGCATCCAGTGGCGGGACATAGTCAGCAGTGACTTTCTCAGCAACATGTCGATGGACTTCCAGAACCACCTGGGCAGCTGCCAAAAGTGTGATCCAAGCTGTCCCAATGGGAGCTGCTGGGGTGCAGGAGAGGAGAACTGCCAGAAACTGACCAAAATCATCTGTGCCCAGCAGTGCTCCGGGCGCTGCCGTGGCAAGTCCCCCAGTGACTGCTGCCACAACCAGTGTGCTGCAGGCTGCACAGGCCCCCGGGAGAGCGACTGCCTGGTCTGCCGCAAATTCCGAGACGAAGCCACGTGCAAGGACACCTGCCCCCCACTCATGCTCTACAACCCCACCACGTACCAGATGGATGTGAACCCCGAGGGCAAATACAGCTTTGGTGCCACCTGCGTGAAGAAGTGTCCCCGTAATTATGTGGTGACAGATCACGGCTCGTGCGTCCGAGCCTGTGGGGCCGACAGCTATGAGATGGAGGAAGACGGCGTCCGCAAGTGTAAGAAGTGCGAAGGGCCTTGCCGCAAAGTGTGTAACGGAATAGGTATTGGTGAATTTAAAGACTCACTCTCCATAAATGCTACGAATATTAAACACTTCAAAAACTGCACCTCCATCAGTGGCGATCTCCACATCCTGCCGGTGGCATTTAGGGGTGACTCCTTCACACATACTCCTCCTCTGGATCCACAGGAACTGGATATTCTGAAAACCGTAAAGGAAATCACAGGGTTTTTGCTGATTCAGGCTTGGCCTGAAAACAGGACGGACCTCCATGCCTTTGAGAACCTAGAAATCATACGCGGCAGGACCAAGCAACATGGTCAGTTTTCTCTTGCAGTCGTCAGCCTGAACATAACATCCTTGGGATTACGCTCCCTCAAGGAGATAAGTGATGGAGATGTGATAATTTCAGGAAACAAAAATTTGTGCTATGCAAATACAATAAACTGGAAAAAACTGTTTGGGACCTCCGGTCAGAAAACCAAAATTATAAGCAACAGAGGTGAAAACAGCTGCAAGGCCACAGGCCAGGTCTGCCATGCCTTGTGCTCCCCCGAGGGCTGCTGGGGCCCGGAGCCCAGGGACTGCGTCTCTTGCCGGAATGTCAGCCGAGGCAGGGAATGCGTGGACAAGTGCAACCTTCTGGAGGGTGAGCCAAGGGAGTTTGTGGAGAACTCTGAGTGCATACAGTGCCACCCAGAGTGCCTGCCTCAGGCCATGAACATCACCTGCACAGGACGGGGACCAGACAACTGTATCCAGTGTGCCCACTACATTGACGGCCCCCACTGCGTCAAGACCTGCCCGGCAGGAGTCATGGGAGAAAACAACACCCTGGTCTGGAAGTACGCAGACGCCGGCCATGTGTGCCACCTGTGCCATCCAAACTGCACCTACGGATGCACTGGGCCAGGTCTTGAAGGCTGTCCAACGAATGGGCCTAAGATCCCGTCCATCGCCACTGGGATGGTGGGGGCCCTCCTCTTGCTGCTGGTGGTGGCCCTGGGGATCGGCCTCTTCATGCGAAGGCGCCACATCGTTCGGAAGCGCACGCTGCGGAGGCTGCTGCAGGAGAGGGAGCTTGTGGAGCCTCTTACACCCAGTGGAGAAGCTCCCAACCAAGCTCTCTTGAGGATCTTGAAGGAAACTGAATTCAAAAAGATCAAAGTGCTGGGCTCCGGTGCGTTCGGCACGGTGTATAAGGGACTCTGGATCCCAGAAGGTGAGAAAGTTAAAATTCCCGTCGCTATCAAGGAATTAAGAGAAGCAACATCTCCGAAAGCCAACAAGGAAATCCTCGATGAAGCCTACGTGATGGCCAGCGTGGACAACCCCCACGTGTGCCGCCTGCTGGGCATCTGCCTCACCTCCACCGTGCAGCTCATCACGCAGCTCATGCCCTTCGGCTGCCTCCTGGACTATGTCCGGGAACACAAAGACAATATTGGCTCCCAGTACCTGCTCAACTGGTGTGTGCAGATCGCAAAGGGCATGAACTACTTGGAGGACCGTCGCTTGGTGCACCGCGACCTGGCAGCCAGGAACGTACTGGTGAAAACACCGCAGCATGTCAAGATCACAGATTTTGGGCTGGCCAAACTGCTGGGTGCGGAAGAGAAAGAATACCATGCAGAAGGAGGCAAAGTGCCTATCAAGTGGATGGCATTGGAATCAATTTTACACAGAATCTATACCCACCAGAGTGATGTCTGGAGCTACGGGGTGACCGTTTGGGAGTTGATGACCTTTGGATCCAAGCCATATGACGGAATCCCTGCCAGCGAGATCTCCTCCATCCTGGAGAAAGGAGAACGCCTCCCTCAGCCACCCATATGTACCATCGATGTCTACATGATCATGGTCAAGTGCTGGATGATAGACGCAGATAGTCGCCCAAAGTTCCGTGAGTTGATCATCGAATTCTCCAAAATGGCCCGAGACCCCCAGCGCTACCTTGTCATTCAGGGGGATGAAAGAATGCATTTGCCAAGTCCTACAGACTCCAACTTCTACCGTGCCCTGATGGATGAAGAAGACATGGACGACGTGGTGGATGCCGACGAGTACCTCATCCCACAGCAGGGCTTCTTCAGCAGCCCCTCCACGTCACGGACTCCCCTCCTGAGCTCTCTGAGTGCAACCAGCAACAATTCCACCGTGGCTTGCATTGATAGAAATGGGCTGCAAAGCTGTCCCATCAAGGAAGACAGCTTCTTGCAGCGATACAGCTCAGACCCCACAGGCGCCTTGACTGAGGACAGCATAGACGACACCTTCCTCCCAGTGCCTGAATACATAAACCAGTCCGTTCCCAAAAGGCCCGCTGGCTCTGTGCAGAATCCTGTCTATCACAATCAGCCTCTGAACCCCGCGCCCAGCAGAGACCCACACTACCAGGACCCCCACAGCACTGCAGTGGGCAACCCCGAGTATCTCAACACTGTCCAGCCCACCTGTGTCAACAGCACATTCGACAGCCCTGCCCACTGGGCCCAGAAAGGCAGCCACCAAATTAGCCTGGACAACCCTGACTACCAGCAGGACTTCTTTCCCAAGGAAGCCAAGCCAAATGGCATCTTTAAGGGCTCCACAGCTGAAAATGCAGAATACCTAAGGGTCGCGCCACAAAGCAGTGAATTTATTGGAGCATGA";
			 input = input + "\n\n"+">BACE1\nATGGCCCAAGCCCTGCCCTGGCTCCTGCTGTGGATGGGCGCGGGAGTGCTGCCTGCCCACGGCACCCAGCACGGCATCCGGCTGCCCCTGCGCAGCGGCCTGGGGGGCGCCCCCCTGGGGCTGCGGCTGCCCCGGGAGACCGACGAAGAGCCCGAGGAGCCCGGCCGGAGGGGCAGCTTTGTGGAGATGGTGGACAACCTGAGGGGCAAGTCGGGGCAGGGCTACTACGTGGAGATGACCGTGGGCAGCCCCCCGCAGACGCTCAACATCCTGGTGGATACAGGCAGCAGTAACTTTGCAGTGGGTGCTGCCCCCCACCCCTTCCTGCATCGCTACTACCAGAGGCAGCTGTCCAGCACATACCGGGACCTCCGGAAGGGTGTGTATGTGCCCTACACCCAGGGCAAGTGGGAAGGGGAGCTGGGCACCGACCTGGTAAGCATCCCCCATGGCCCCAACGTCACTGTGCGTGCCAACATTGCTGCCATCACTGAATCAGACAAGTTCTTCATCAACGGCTCCAACTGGGAAGGCATCCTGGGGCTGGCCTATGCTGAGATTGCCAGGCCTGACGACTCCCTGGAGCCTTTCTTTGACTCTCTGGTAAAGCAGACCCACGTTCCCAACCTCTTCTCCCTGCAGCTTTGTGGTGCTGGCTTCCCCCTCAACCAGTCTGAAGTGCTGGCCTCTGTCGGAGGGAGCATGATCATTGGAGGTATCGACCACTCGCTGTACACAGGCAGTCTCTGGTATACACCCATCCGGCGGGAGTGGTATTATGAGGTGATCATTGTGCGGGTGGAGATCAATGGACAGGATCTGAAAATGGACTGCAAGGAGTACAACTATGACAAGAGCATTGTGGACAGTGGCACCACCAACCTTCGTTTGCCCAAGAAAGTGTTTGAAGCTGCAGTCAAATCCATCAAGGCAGCCTCCTCCACGGAGAAGTTCCCTGATGGTTTCTGGCTAGGAGAGCAGCTGGTGTGCTGGCAAGCAGGCACCACCCCTTGGAACATTTTCCCAGTCATCTCACTCTACCTAATGGGTGAGGTTACCAACCAGTCCTTCCGCATCACCATCCTTCCGCAGCAATACCTGCGGCCAGTGGAAGATGTGGCCACGTCCCAAGACGACTGTTACAAGTTTGCCATCTCACAGTCATCCACGGGCACTGTTATGGGAGCTGTTATCATGGAGGGCTTCTACGTTGTCTTTGATCGGGCCCGAAAACGAATTGGCTTTGCTGTCAGCGCTTGCCATGTGCACGATGAGTTCAGGACGGCAGCGGTGGAAGGCCCTTTTGTCACCTTGGACATGGAAGACTGTGGCTACAACATTCCACAGACAGATGAGTCAACCCTCATGACCATAGCCTATGTCATGGCTGCCATCTGCGCCCTCTTCATGCTGCCACTCTGCCTCATGGTGTGTCAGTGGCGCTGCCTCCGCTGCCTGCGCCAGCAGCATGATGACTTTGCTGATGACATCTCCCTGCTGAAGTGA";
			 $( "#seqTextPaste" ).val(input);
				$("#pasteSeq").dialog("open");

		}
		);
		
$("#seqform").submit(function()
		{
			if(	$("#seqText").val() != "")
			{		
					return true;
			}
			else if($("#filesequence").val() != "")
			{		
					return true;
			}
			else
			{
				alert("No sequence has been chosen.  \n\nPlease choose a sequence.");
				return false;
			}
	
		}
		);
</script>



