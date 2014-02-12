<%@ include file="/WEB-INF/include.jsp"%>
<h1>Off Target List for ${sevenmer}. Matching on ${searchStrand}</h1>
<div class="row-fluid">
<div class="span12">
<h2>About</h2>
  <p>This is a list of probable off-target genes
    rank-ordered by individual transcript Probability of Off-Target
    Score (tPOTS). tPOTS is calculated based on the number and type of
    seed matches found in that transcript. The number of 8mer, 7mer-M8,
    7mer-1A and 6mer sites are also shown. The entire list of
    off-targets can be exported to a CSV file to analyze as a simple
    spreadsheet.</p>

<div class="button">
<a class="btn" href="offTargetSearchExport.html?7mer=${sevenmer}&type=a&format=csv">Export to CSV</a>
</div>
<br/>
<p>Species: ${species}</p>
<table id="offtargettable" class="table table-striped table-bordered" >
<thead>
<tr>
<th>
Gene Symbol
</th>
<th>
TPOTS
</th>
<th>
8mer
</th>
<th>
7mer-M8
</th>
<th>
7mer-1A
</th>
<th>
6mer
</th>
</tr>
</thead>
<tbody>
<c:forEach items="${results}" var="resultArray">
<tr>

<%-- <td><a href="http://www.ncbi.nlm.nih.gov/gene/${resultArray[0]}">${resultArray[0]}</a></td>  --%>
<td>${resultArray[0]}</td>
<td>${resultArray[1]}</td>
<td>${resultArray[2]}</td>
<td>${resultArray[3]}</td>
<td>${resultArray[4]}</td>
<td>${resultArray[5]}</td>


</tr>


</c:forEach>
</tbody>
</table>
</div>
</div>

<script type="text/javascript">

function getBrowser() {

	  var sBrowser = navigator.userAgent;

	  if (sBrowser.toLowerCase().indexOf('msie') > -1) return 'ie';

	  else if (sBrowser.toLowerCase().indexOf('firefox') > -1) return 'firefox';

	  else return 'mozilla';

	}

// Init the table var oTable = $('#example').dataTable)()





</script>
