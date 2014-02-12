<%@ include file="/WEB-INF/include.jsp"  %>
   
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid" >
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#">siSPOTR</a>
          
 
          
          <div class="btn-group pull-right">
           
  		<sec:authorize	access="!isAuthenticated()">
  		<a class="btn" href="<c:url value="/register.html" />">Register</a>
      		<a class="btn btn-info" href="<c:url value="/login.html" />">Sign-In</a>
      </sec:authorize>
      <sec:authorize	access="isAuthenticated()">
       <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
             <i class="icon-user"> </i>
      ${username}
      <span class="caret"></span>
            </a>
                        <ul class="dropdown-menu">
     <li><a href="<c:url value="/j_spring_security_logout"/>">	Sign Out    	</a></li>
            </ul>
    
             
      </sec:authorize>
            
          </div>
          <div class="nav-collapse">
<ul  class="nav">
<!-- <li class="nav-header">Information</li> -->
<li><a href="<c:url value="/index.html" />">Overview</a></li>
<li><a href="<c:url value="/tools.html" />">About the Tools</a></li>
<li><a href="<c:url value="/help.html" />">FAQ</a></li>
<!-- <li class="nav-header">Tools</li> -->
<li><a href="<c:url value="/tools/sispotr/design.html" />">Designer</a></li>

<li><a href="<c:url value="/tools/lookup/evaluate.html" />">POTS Lookup</a></li>

</ul>
          </div>
        </div>
      </div>
    </div>
