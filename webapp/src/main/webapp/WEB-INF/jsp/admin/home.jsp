<%@ include file="/WEB-INF/include.jsp"  %>

<div class="box">
<ul id="mainmenu" >
<li><a>Admin Menu</a><ul>
<li><a href="<c:url value="/safeseed/person/list.html" />" >Person List</a></li>
<li><a href="<c:url value="/safeseed/sitecontent/list.html" />">List Site Content</a></li>
<li><a href="<c:url value="/safeseed/seq/list.html" />" >Seq List</a></li>
<li><a href="<c:url value="/safeseed/frag/list.html" />" >Frag List</a></li>
<li><a href="<c:url value="/safeseed/systemsettings/list.html" />" >SystemSettings List</a></li>
<li><a href="<c:url value="/safeseed/activeseq/list.html" />" >ActiveSeq List</a></li>
<li><a href="<c:url value="/safeseed/audit/list.html" />" >Audit List</a></li>
</ul></li></ul>
</div>