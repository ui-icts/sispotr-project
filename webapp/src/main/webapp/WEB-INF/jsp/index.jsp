<%@ include file="/WEB-INF/include.jsp"%>
 <div  class="row-fluid">
  <div class="span12">
  <h1>Program Overview</h1>
  </div>
  </div>
  
   <div  class="row-fluid">
  <div class="well span6">
  ${siteContentMap['index.main']}
  
<sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=index.main">edit</a>
</sec:authorize>

   </div>
   <div class="well span5">
 ${siteContentMap['index.side']}
 <sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=index.side">edit</a>
</sec:authorize>
    </div>
  </div>
  

   
  