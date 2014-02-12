<%@ include file="/WEB-INF/include.jsp"%>
<h1>Sequence Search</h1>
<div class="box">
Original Sequence:<br/>
${searchseq_full}<br/><br/>
Search Sequence:<br/>
${ss.searchSeq}<br/><br/>

<br/>
<br/>

Results:
<div id="results">
loading
</div>

</div>

<script>

function loadResults(seq)
{
	var params ={searchSeq:seq};
	
	$.ajax({
		type : "GET",
		dataType : "html",
		url : "loadSearchResults.html",
		data : params,
		cache: false, 
		success : function(data) {
		

			$("#results").html(data);


		}
	});
	
}

setTimeout(
		function(){loadResults("${ss.searchSeq}");}
		,1000);
</script>