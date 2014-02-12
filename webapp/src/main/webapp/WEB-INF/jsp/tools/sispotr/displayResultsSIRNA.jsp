<%@ include file="/WEB-INF/include.jsp"%>

<script type="text/javascript">
	$(function() {
		$("#tabs").tabs({
			spinner : "Retrieving data..."

		});
		$("#tabs").tabs("select",+"${seqIndex}");
		
		$("#seq").dialog({
			autoOpen : false,
			height : 500,
			width : 500
		});

	});


	function loadTab(aid,length) {
		var params = {
			aid : aid,
			type: 'sirna',
			length: length
		};

		$.ajax({
			type : "GET",
			dataType : "html",
			url : "loadTab.html",
			data : params,
			cache : false,
			success : function(data) {

				$("#tabcontents_" + aid).html(data);

			}
		});

			

	}
	
	function loadCommonResults() {
		var params = {
			
		};

		$.ajax({
			type : "GET",
			dataType : "html",
			url : "commonResults.html",
			data : params,
			cache : false,
			success : function(data) {

				$("#commonOutput").html(data);
			

			}
		});
	}

	
	function recompute() 
	{

				var accession = $('#accession').val();
				var lengthOfXmers = $("#lengthOfXmers");
				var gcMin = $("#gcMin");
				var gcMax = $("#gcMax");
				var gcRequired = $("#gcRequired");
				var numberOfReturnElements = $("#numberOfReturnElements");
				var ensureBestPots = $('input:radio[name=ensureBestPots]:checked');
				var gAt2 = $("input:radio[name=gAt2]:checked");
				var gAt3 = $('input:radio[name=gAt3]:checked');
				var pol3 = $('#pol3').is(':checked');
				
				

				//			var params = "aid="+accession;
				//			params += "&lengthOfXmers="+lengthOfXmers.val();
				//			params += "&gcMin="+gcMin.val();
				//			params += "&gcMax="+gcMax.val();
				//			params += "&gcRequired="+gcRequired.val();
				//			params += "&numberOfReturnElements="+numberOfReturnElements.val();
				//			params += "&ensureBestPots="+ensureBestPots.val();
				//			params += "&gAt2="+gAt2.val();
				//		params += "&gAt3="+gAt3.val();

				var params = {
					aid : accession,
					lengthOfXmers : lengthOfXmers.val(),
					gcMin : gcMin.val(),
					gcMax : gcMax.val(),
					gcRequired : gcRequired.val(),
					numberOfReturnElements : numberOfReturnElements.val(),
					ensureBestPots : ensureBestPots.val(),
					gAt2 : gAt2.val(),
					gAt3 : gAt3.val(),
					pol3 : pol3
				};

				//alert("recomputing..." + params);
				$("#recomputeButton").attr("disabled", true);
				$("#statusMessage").html("Loading....");
				$.ajax({
					type : "GET",
					dataType : "html",
					url : "updateTabData.html",
					cache : false,
					data : params,
					success : function(data) {
					//	alert("loading tab");
						loadTab(accession,lengthOfXmers.val());
						

					}
				});

			}



	//after page finished loading
	$(document)
			.ready(

					function($) {
						
						menuEnabled=true;
						toggleMenu();
						
						$("#tabs").show();
						$("#parameters").show();
						
						$('#gcMin').spinner({
							step : 1,
							min : 0,
							max : 100
						});
						$('#gcMax').spinner({
							step : 1,
							min : 0,
							max : 100
						});
						$('#gcRequired').spinner({
							step : 1,
							min : 0,
							max : 2
						});
						$('input:radio[name=gAt2]').val([ "true" ]);
						$('input:radio[name=gAt3]').val([ "true" ]);
						$('input:radio[name=ensureBestPots]').val([ 'false' ]);

						if (jQuery.browser.msie) {
							jQuery('span.ui-spinner').css('vertical-align','middle').css('top', '-6px');
						}
						
						
						//  $('#lengthOfXmers').spinner({step: 1, min: 19, max: 30 });

						$( "#paramsDialog" ).dialog({
							autoOpen: false,
							height: 600,
							width: 450,
							modal: true,
							buttons: {
								"Recompute": function() {
									    recompute();

										$( this ).dialog( "close" );
								
								},
								"Cancel ": function() {
									$( this ).dialog( "close" );
								}
							},
							close: function() {
								
							}
						});
						

						

					

					});
	
	
	
	function toggleCol(table, iCol )
	{
		/* Get the DataTables object again - this is not a recreation, just a get of the object */
		var oTable = $(table).dataTable();
		
		var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
		oTable.fnSetColumnVis( iCol, bVis ? false : true );
	}
</script>
<style>
#tabs
{

clear: both;
overflow:auto; 
overflow: -moz-scrollbars-horizontal; 
overflow-x:scroll; 
overflow-y:hidden; 
}
#innerTabContainer
{

clear: both;
overflow:auto; 
overflow: -moz-scrollbars-horizontal; 
overflow-x:scroll; 
overflow-y:hidden; 
}
</style>

  <div  id="paramsDialog" title="Update parameters"  >
    <p class="validateTips"></p>
    <fieldset style="padding:25px;">
      <input    type="hidden"  name="htmlFormName" value="recompute" /> 
        <input    type="hidden"      name="species"       id="species"       value="" />
        <input    type="hidden"      name="lengthOfXmers"       id="lengthOfXmers"       value="" />
        <input       type="hidden"       name="accession"       id="accession"     value="" />

      <div    class="fieldOptions"    id="parameters"   >
         

        <label for="numberOfReturnElements">Number of Results:</label>
        <br/> 
        <select name="numberOfReturnElements"        id="numberOfReturnElements">
          <option>10</option>
          <option>25</option>
          <option>50</option>
          <option>100</option>
        </select> <br /><br/>
       
          <label for="gcMin">G/C Content% Min</label> <br/>
  
        <input type="text"  name="gcMin"        id="gcMin"      value=""       size="8" />  
          
          <i class="icon-question-sign"    title="Lower limit of G/C content in siRNA. Default=20" ></i>
        <br /> <br />

        
          <label for="gcMax">G/C Content% Max</label> <br/>

        <input       type="text"       name="gcMax"      id="gcMax"       value=""  size="8" />            
         
          <i class="icon-question-sign"     title="Upper limit of G/C content in siRNA. Default=70" ></i>
        



<br/><br/>

         
            <label for="gcRequired">How many G/Cs required at 5' end of Passenger?</label> <br/>
               
    <input       type="text"        name="gcRequired"        id="gcRequired"       value=""      size="8" />       
              <i class="icon-question-sign"    title="Increases stability (decreases loading) of passenger strand. Default=2." ></i>
       
       
      
            <br/>
            <br/>
                 
      <input type="checkbox"  class="checkbox" name="pol3" id="pol3" />
      <label class="checkbox" for="pol3">Design for Pol III expression</label> 
       
       <i class="icon-question-sign"  title="Remove sequences with stretches of four or more T's/U's" ></i>
       
      </div>

    </fieldset>
  </div>
<h1><span class="accentedfont">si</span>RNA Results</h1>
<div >
<div class="btn-group">
<a onclick="toggleMenu();" id="menuButton" class="btn"></a>
<a href="design.html" class="btn btn-info"><i class="icon-plus-sign"></i> Design Another Sequence</a>
</div>
    <div id="statusMessage" style="height: 20px;color:#A00"></div>

     <div id="tabsContainer">
  <div
    id="tabs"
    align="left"
    >
    <ul>
          <c:forEach     items="${seqManager.seqs}"      var="seq" varStatus="status0">
    
      <c:forEach   items="${seq.xmersList}"  var="x" varStatus="status">
        <c:if test="${x.xmerType =='SIRNA'}">
        <li><a       href="#tab_${x.seq.label}"       id="#tab_${x.seq.label}"     onclick="loadTab('${x.seq.label}',${x.length}); $('#accession').val('${x.seq.label}');"><c:out value="${x.seq.label}" /></a>
        </li>
        </c:if>
      </c:forEach>
      </c:forEach>
      <c:if test="${fn:length(seqManager.seqs)>1}">
       <li>
              <a href="#common"     id="#commontab"  onclick="loadCommonResults();" >Common Results</a>
        </li>
      </c:if>
       <li>
              <a href="#help"     id="#helptab"        >Help</a>
        </li>
    </ul>


          <c:forEach
        items="${seqManager.seqs}"
        var="seq"
        varStatus="status0">
    
      <c:forEach
        items="${seq.xmersList}"
        var="x"
        varStatus="status">
        <c:if test="${x.xmerType =='SIRNA'}">
      <div id="tab_${x.seq.label}">
        <div id="tabcontents_${x.seq.label}">Loading...</div>
      </div>
      </c:if>
      
    </c:forEach>
    </c:forEach>
         <c:if test="${fn:length(seqManager.seqs)>1}">
              
      <div id="common">
        <div id="commonOutput">Loading...</div>
      </div>
      </c:if>
      <div id="help">
     ${siteContentMap['resultssirna.help']}
           <sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=resultssirna.help">edit</a>
</sec:authorize>
     <br/>
     <br/>


</div>
      </div>
    
  </div>

</div>




<%-- initialize first tab --%>
  
<c:forEach     items="${seqManager.seqs}"     var="seq"    varStatus="status0">    
  <c:forEach      items="${seq.xmersList}"   var="x"     varStatus="status">
    <c:if test="${status.count == 1}">
      <c:if test="${x.xmerType =='SIRNA'}">
      
        <script type="text/javascript">
			loadTab('${x.seq.label}','${x.length}');
			$('#accession').val('${x.seq.label}');
		</script>
      </c:if>
          
      </c:if>

    
  </c:forEach>
</c:forEach>



<script type="text/javascript">

	$("form").submit(function() {
		alert('tab label ' + $(ui.tab).text());
		return false;
	});

	$("#showSeq").click(function() {
		$("#seq").dialog("open");
	});
	
	$("#commonResultsButton").click(function()	{
				loadCommonResults();
				$("#commonDialog").dialog("open");
		
			}
			);
	

	</script>
<%-- basica form validation --%>
	
<%--if ($("input:first").val() == "correct") {
		        $("span").text("Validated...").show();
		        return true;
		      }
		      $("span").text("Not valid!").show().fadeOut(1000);
		      return false;
		    });--%>

	
