<%@ include file="/WEB-INF/include.jsp"%>

<script type="text/javascript" charset="utf-8">
$(document)
.ready(

		function($) {
			
			menuEnabled=true;
			toggleMenu();
			
		}
		);
</script>

<h1>siRNA POTS Lookup</h1>

<div class="btn-group">

<a onclick="toggleMenu();" class="btn" id="menuButton"></a>
<a class="btn btn-info" href="<c:url value="/tools/lookup/evaluate.html" />"><i class="icon-search"></i> Lookup POTS for Different siRNA</a>
</div>
<br/>
<!--<br/>
<br/>
Submitted data...<br/> 
<br/>
<br/>
Radio Value was = ${radioVal }
<br/>
<br/>
<br/>
Textarea Value was =
<pre> 
${textVal }
</pre>-->
<div class="box">
<div class="tabs">
  <ul>
    <li><a href="#results1">Lookup Results</a></li>
       <li><a href="#help1">Help</a></li>
    

  </ul>
  <div id="results1">

       
                           <table class="display"  id="resultsTable" style="display: none">
                                <thead align="center">
                                    <tr>
                                    <th><div class="tool">Off-Target Details <img src="<c:url value='/' />resources/images/tooltip_icon.gif" title="BLAST sequence, POTS score details, off-target list"></div></th>
                                    <th>Input Order</th>
                                    <th><div class="tool">siRNA Input <img src="<c:url value="/" />resources/images/tooltip_icon.gif" title="Your input sequence(s) in DNA format" /></div></th>
                                    <th><div class="tool">POTS <img src="<c:url value="/" />resources/images/tooltip_icon.gif" title="Probability of Off-Targeting Score. Ideal score &lt;=30" /></div></th>
                                    <th><div class="tool">Percentile (%&nbsp;worse) <img src="<c:url value="/" />resources/images/tooltip_icon.gif" title="% of all 7mers with a worse POTS value." /></div></th>
                                    <th><div class="tool">Seed Sequence <img src="<c:url value="/" />resources/images/tooltip_icon.gif" title="Seed sequence for the siRNA used for calculating POTS" /></div></th>
                                    <th><div class="tool">miRNA seed <img src="<c:url value='/' />resources/images/tooltip_icon.gif" title="Shows seed homology to known endogenous microRNAs (miRNA). Recommended to avoid these sequences"></div></th>
                                    <th><div class="tool">miRNA conservation <img src="<c:url value='/' />resources/images/tooltip_icon.gif" title="Conservation representation of matching miRNA. H(uman), M(ouse) and D(og) are species where miRNA is present. Particularly avoid H,M,D miRNA matches" ></div></th>
                                              <th>SPS (&delta;G)
          <a rel="tooltip" data-original-title="Info:" 
          data-content="SPS=Seed Pairing Stability (kcal/mol). 
          More Negative = stronger binding and more potential 
          off-targeting. " id="sps_tip">
          <i class="icon-question-sign"></i></a>
          <script>
          $("#sps_tip").popover({placement:"left"});
          </script>
          </th>
          
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list.evalSIRNAData}" var="row"  varStatus="status">
                           <tr>
                          <td>

<!-- DROP DOWN -->
<!-- see for more info on blast url api: http://www.ncbi.nlm.nih.gov/BLAST/Doc/urlapi.html -->
<div class="btn-group">
    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
<span class="caret"></span>
    </a>
    <ul class="dropdown-menu">
   <li> 
<a class=""  title="BLAST Search 21mer" target="_blank" href="http://blast.ncbi.nlm.nih.gov/Blast.cgi?PAGE_TYPE=BlastSearch&PROG_DEF=blastn&BLAST_PROG_DEF=megaBlast&SHOW_DEFAULTS=on&DATABASE=refseq_rna&QUERY=${row[1]}">Run BLAST search on 21mer</a>
<li>
<a class=""  title="BLAST Search 18mer" target="_blank" href="http://blast.ncbi.nlm.nih.gov/Blast.cgi?PAGE_TYPE=BlastSearch&PROG_DEF=blastn&BLAST_PROG_DEF=megaBlast&SHOW_DEFAULTS=on&DATABASE=refseq_rna&QUERY=${fn:substring(row[1],1,fn:length(row[1])-2)}">
Run BLAST search on 18mer (2-19)</a>
</li>
<li>
<a  class=""  title="POTS Summary" target="_blank" href="../offtarget/potsSummary.html?7mer=${row[4]}&pots=${row[2]}&type=a&species=${species}">Show details of POTS score: ${row[2]}</a>
</li>
<li>
<a class=""   title="Off-Target Search" target="_blank" href="../offtarget/offTargetSearch.html?7mer=${row[4]}&type=a&species=${species}">List off-target hits for ${row[4]}</a>
</li>
    </ul>
    </div>
    </td>
        <td>${row[0]}</td>
        <td>${row[1]}</td>
        <td>${row[2]}</td>
        <td>${row[3]}</td>
        <td>${row[4]}</td>
        <td>${row[5]}</td>
        <td>${row[6]}</td>
        <td>${row[7]}</td>


      </tr>
                                    </c:forEach>
                                </tbody>
                                <c:set var="displayFooter" value="false"/>
                                <c:if test="${displayFooter}">
                                    <tfoot align="center">
                                        <tr>
                                            <c:forEach items="${list.evalHeader}" var="h" varStatus="status">
                                                <th>${h}</th>
                                            </c:forEach>
                                        </tr>
                                    </tfoot>
                                </c:if>
                            </table>
                            <br/>
 </div>
 
  <div id="help1">
   
  <h3>Output</h3>
  <p>
  The output provides the following information:
  </p>
  
   <jsp:include page="../../info/lookupOutputHelpAccordion.jsp"></jsp:include>
  <br/>
  <br/> 

      <h3>Additional Features (Off-Target Details button)</h3>
   These options are available by clicking the "Off-Target Details" button in the first column of the result of interest
    <br/>
     <jsp:include page="../../info/additionalInfoText.jsp"></jsp:include>
     
     </div>
     </div>
     </div>

        <script type="text/javascript" charset="utf-8">
  
        $('#resultsTable').show();

        // Init the table var oTable = $('#example').dataTable)()
        var oTable =
            // the data table element
        $('#resultsTable').dataTable( {
        	"bRetrieve": true,
            "bProcessing": true,                                        // show the processing message           
         //   "bScrollCollapse" : true,
 			"aaSorting": [[ 1, "asc" ]],                                // sor the first col asc on initialization
                                       // use the JQueryUI
			"sPaginationType": "full_numbers",                          // pagination style
			"aoColumns" : [{ "bSortable":false }, null,null,null,null,null,null,null],
//             "bJQueryUI": true,                                          // use the JQueryUI
            "bPaginate": false,
            "fnInitComplete": function(){
              //  this.fnAdjustColumnSizing();
            //    this.fnDraw();
            }
        } );
	$(".tool img[title]").tooltip();
    $(function() {
        $( ".tabs" ).tabs();
      });
                
	</script> 

