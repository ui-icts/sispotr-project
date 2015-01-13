<%@ include file="/WEB-INF/include.jsp"%>
 
<strong>Query ID:</strong> ${xmer.seq.label} | <strong>Type:</strong> ${xmer.xmerType} | <strong>Species:</strong> ${xmer.seq.species} | <strong>Length:</strong> ${xmer.length} | <strong>G/C required:</strong> ${xmer.gcRequired} | <strong>% G/C</strong> ${xmer.gcMin * 100}-${xmer.gcMax * 100}  | <strong>Designed for Pol III Expression:</strong> ${xmer.pol3}
<br/><br/>


 <div class="modal hide" id="colModal${aid}">
<div class="modal-header">
 <button type="button" class="close" data-dismiss="modal">×</button>
<h3>Columns Visibility</h3>
</div>
<div class="modal-body">
         <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','2');" checked="checked"/>Start Position</label>
          <br/>
         <label> <input type="checkbox"  onclick="toggleCol('#table_${aid}','3');" checked="checked"/>POTS</label>
          <br/> 
          <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','4');" checked="checked"/>Percentile (%&nbsp;worse)</label>
          <br/>
          
          <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','5');" checked="checked"/>Guide (Antisense)</label>
          <br/>
          <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','6');" checked="checked"/>Passenger (Sense)</label>
          <br/>
          <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','7');" checked="checked"/>Seed Sequence</label>
          <br/> 
          <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','8');" checked="checked"/>miRNA seed</label>
          <br/>
          <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','9');" checked="checked"/>miRNA conservation</label>
          <br/>
          <label><input type="checkbox"  onclick="toggleCol('#table_${aid}','10');" checked="checked"/>% G/C</label>
    
    </div>
                <div class="modal-footer">
<a href="#" class="btn" data-dismiss="modal">Close</a>
</div>
          </div>
  <div class="btn-group">
 <a id="updateParamsButton${aid}" class="btn"><i class="icon-edit"></i> Update parameters</a>
 <a class="btn" data-toggle="modal" href="#colModal${aid}" >Hide/Show Columns</a>
<a class="btn" id="export${aid}"><i class="icon-download"></i> Export to CSV</a>
</div>
<br/>       
    
    <br/>
     <sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=resultstable.header">edit header</a>
</sec:authorize>
<table class="display table" id="table_${aid}">
			<thead align="center">
				<tr>
				<th></th>
				<c:if test="${xmer.xmerType == 'SHRNA'}">
				<th>Select</th>
				</c:if>
		  		  ${siteContentMap['resultstable.header']}

				</tr>
			</thead>
			<tbody>
				<c:forEach items="${xmer.data}" var="row" varStatus="status1">
					<tr>
					<td>
<fmt:formatNumber var="pots">${row[2]}</fmt:formatNumber>
<c:if test="${ pots <= 50}">
<i class="icon-ok-circle icon-green" title="POTS Score less than 50"></i>
</c:if>
</td>	
				<c:if test="${xmer.xmerType == 'SHRNA'}">
				<td>
				<button id="add${aid}${row[0]}" type="button" title="add">add</button>
				</td>
				</c:if>
					<td>
					
					
					  <div class="btn-group">
    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
<span class="caret"></span>
    </a>
    <ul class="dropdown-menu">
   <li> <a title="BLAST Search" target="_blank" href="http://blast.ncbi.nlm.nih.gov/Blast.cgi?PAGE_TYPE=BlastSearch&PROG_DEF=blastn&BLAST_PROG_DEF=megaBlast&SHOW_DEFAULTS=on&DATABASE=refseq_rna&QUERY=${row[4]}">Run BLAST search on 21mer</a></li>
<li>
<a  title="BLAST Search 18mer" target="_blank" href="http://blast.ncbi.nlm.nih.gov/Blast.cgi?PAGE_TYPE=BlastSearch&PROG_DEF=blastn&BLAST_PROG_DEF=megaBlast&SHOW_DEFAULTS=on&DATABASE=refseq_rna&QUERY=${fn:substring(row[4],1,fn:length(row[4])-2)}">
Run BLAST search on 18mer (2-19)</a>
</li>
<li>
<!-- see for more info on blast url api: http://www.ncbi.nlm.nih.gov/BLAST/Doc/urlapi.html -->

<a title="POTS Summary" target="_blank" href="${contextPath}tools/offtarget/potsSummary.html?7mer=${row[6]}&pots=${row[2]}&type=a&species=${xmer.seq.species}">Show details of POTS score: ${row[2]}</a>
</li>
<li>

<a  title="Off-Target Search" target="_blank" href="${contextPath}tools/offtarget/offTargetSearch.html?7mer=${row[6]}&type=a&species=${xmer.seq.species}">List off-target hits for ${row[6]}</a>
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
        <td>${row[8]}</td>
        <td>${row[9]}</td>
        <td>${row[10]}</td>


      </tr>
				</c:forEach>
			</tbody>
			<c:set var="displayFooter" value="false" />
			<c:if test="${displayFooter}">
				<tfoot align="center">
					<tr>
						<c:forEach items="${xmer.columns}" var="h" varStatus="status">
							<th>${h}</th>
						</c:forEach>
					</tr>
				</tfoot>
			</c:if>
		</table>
<script type="text/javascript" charset="utf-8">

/*
 * updates parameter dialog values with values from current xmer
 *
 */
$("#updateParamsButton${aid}").click(function(){
			$("#lengthOfXmers").val("${xmer.length}");
			$("#numberOfReturnElements").val("${xmer.numberOfReturnElements}");
			$("#gcMin").val(("${100*xmer.gcMin}"));
			$("#gcMax").val(("${100*xmer.gcMax}"));
			$("#gcRequired").val("${xmer.gcRequired}");
			$("#accession").val("${aid}");
			$("#species").val("${xmer.seq.species}");
			
			if("${xmer.pol3}"=="true"){
				$("#pol3").attr("checked",true);
			}
			else {
				$("#pol3").attr("checked",false);
			}
			
			$("#paramsDialog").dialog("open");
		});


/*
 * Setup export button
 */
$("#export${aid}").click(function(){
		window.location =  'exportTab.html?aid=${aid}&length=${xmer.length}';
});


/*
 * Setup output table
 */
$(".display").dataTable( {
        "bProcessing": true,                                        // show the processing message
        "bRetrieve":true,
        "bScrollCollapse" : true,
        "aaSorting": [[ 2, "asc" ]],                                // sor the first col asc on initialization                                        // use the JQueryUI
        "sPaginationType": "full_numbers",                          // pagination style
		"aoColumns" : [{ "bSortable":false }, null,null,null,null,null,null,null,null,null,null],
		 "oLanguage": {
	         "sSearch": "Refine Results:"
	       }
    } );

	$( "#statusMessage" ).effect( "fade", {}, 300, function(){$("#statusMessage").html("");$("#statusMessage").fadeIn();} );
	
  
    $(".pop-left").popover({placement:"left"});
    $(".pop-right").popover({placement:"right"});
  
	</script>
