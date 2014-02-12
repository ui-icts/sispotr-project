<%@ include file="/WEB-INF/include.jsp"  %>
<h1>About the tools</h1>
   <div  class="row-fluid">
  <div class="span11">
  
   ${siteContentMap['toolinfo.sirna']}
 <sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=toolinfo.sirna">edit</a>
</sec:authorize>
  

</div>
</div>

<br/>

   <div  class="row-fluid">
  <div class="span11">
     ${siteContentMap['toolinfo.sirnalookup']}
 <sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=toolinfo.sirnalookup">edit</a>
</sec:authorize>
   
</div>
   </div>

  
   <br/>
   <div  class="row-fluid">

  <div class="span11 ">
  <br/>
  <br/>
<div class="well">

     ${siteContentMap['toolinfo.addinfo']}
 <sec:authorize ifAnyGranted="10 ">
<a href="<c:url value="/"/>/safeseed/sitecontent/editByLabel.html?label=toolinfo.addinfo">edit</a>
</sec:authorize>
     
   </div>
  </div>
  </div>

    
	
<script>
  $(function() {
    $( ".tabs" ).tabs();
  });
  </script>
  