<%@ include file="/WEB-INF/include.jsp"%>
<h1>${species} POTS Summary for ${sevenmer}. </h1>
<style>
span.field 
{ 
font-weight: bold;
font-size: 1.2em;
}
</style>
<div class="grid_6">
<div class="box">
<h2>Details of POTS score</h2>
<fieldset>
<span class="field">Seed Match</span><br/>
${potsSum.heptamer} <br/><br/>
<span class="field">Species</span><br/>
${potsSum.species}
<br/>
<br/>
<span class="field"># Predicted Target Genes</span>
<br/>
${potsSum.totalGenesTargeted}
<br/>
<br/>
<span class="field"># 3'UTRs with 8mer</span>
<br/>
 ${potsSum.genes8Mer}
 <br/>
 <br/>
<span class="field"># 3'UTRs with 7mer-M8</span>
<br/>
 ${potsSum.genes7merM8}
 <br/>
 <br/>
<span class="field"># 3'UTRs with 7mer-1A</span>
<br/>
 ${potsSum.genes7merA1}
 <br/>
 <br/>
<span class="field"># 3'UTRs with 6mer</span>
<br/>
 ${potsSum.genes6mer}
<br/>
<br/>
<span class="field">POTS</span>
<br/>
 ${potsSum.pots}
 <br/>
<br/>
</fieldset>
</div>
</div>
<div class="grid_6">
  <div class="box">
  <h2>About</h2> This provides summary
    information on the total number of predicted off-targets and the
    distribution of the various "site-types" found in those predicted
    targets. The POTS value is calculated by analyzing the potential
    off-target genes for seed matches conforming to known endogenous
    microRNA targeting rules. "8mer" sites tend to be the most potent
    followed by (in decreasing order) 7mer-M8, 7mer-1A and 6mer.</div>

</div>
<script type="text/javascript">
// Init the table var oTable = $('#example').dataTable)()
var oTable =
    // the data table element
$('#offtargettable').dataTable( {
	"bRetrieve": true,
    "bProcessing": true,                                        // show the processing message
                               // inner Y scorll
    "aaSorting": [[ 0, "asc" ]],                                // sort the first col asc on initialization
											// turn off default sorting
  //  "bJQueryUI": true,                                          // use the JQueryUI

    "sPaginationType": "full_numbers" ,                         // pagination style
    //"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]      // length menu
    //"sDom": '<"H"TCfr>t<"F"ip>',                                // saving information plug-in
    //"sDom": 'C<"bJUI">lfrtip',                                  // show/hide columns plug-in
    //"oColVis": {"activate": "mouseover"},
    //"bFilter": true,

    });


</script>
