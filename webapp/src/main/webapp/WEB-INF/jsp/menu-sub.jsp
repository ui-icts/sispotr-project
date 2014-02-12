<%@ include file="/WEB-INF/include.jsp"  %>
<style>
.nav-list li a{
padding-top:10px;
padding-bottom:10px;
}
</style>
<div class="sidebar-nav">
<ul  class="nav nav-list">
<li class="nav-header">Tools</li>

<li><a href="<c:url value="/tools/sispotr/design.html" />"><i class="icon-plus-sign"></i> Designer</a>

<li><a href="<c:url value="/tools/lookup/evaluate.html" />"><i class="icon-info-sign"></i> POTS Lookup</a></li>
<li class="nav-header">Results</li>

<c:if test="${fn:length(seqManager.seqs)>0}">
<c:if test="${fn:length(seqManager.sirnaXmerList)>0}">
<li><a href="<c:url value="/tools/sispotr/displayResults.html?type=sirna" />"><i class="icon-tasks"></i> <strong>si</strong>RNA Results</a></li>
</c:if>
<c:if test="${fn:length(seqManager.shrnaXmerList)>0}">
<li><a href="<c:url value="/tools/sispotr/displayResults.html?type=shrna" />"><i class="icon-tasks"></i> <strong>sh</strong>RNA Results</a></li>
</c:if>

<li>
<a href="<c:url value="/tools/sispotr/" />reset.html" ><i class="icon-refresh"></i> Clear results</a>
</li>

</c:if>
<c:if test="${fn:length(seqManager.seqs)==0}">
no processed results
</c:if>




<sec:authorize ifAnyGranted="10 ">

<li class="nav-header">Admin</li>

<li><a href="<c:url value="/safeseed/sitecontent/list.html" />">Site Content</a></li>
<li><a href="<c:url value="/safeseed/person/list.html" />" >User List</a></li>
<li><a href="<c:url value="/ictsmonitoring" />" >System Monitor</a></li>
<%-- <li><a href="<c:url value="/safeseed/seq/collection.html" />">My Collection</a></li> --%>
<%-- <li><a href="<c:url value="/safeseed/queue/list.html" />">Process Queue</a></li> --%>

</sec:authorize>


    </ul>
</div>
