<%@ include file="/WEB-INF/include.jsp"%>

<br/>
<div class="centeredContainer" >
<div class="box" style="width:520px;">

<h1>Reviewer Login Page</h1>
<form name="f" action="<c:url value='j_spring_security_check'/>"
	method="POST">
<fieldset style="padding:30px;border:1px solid #bbb;">
<legend style="border:1px solid #ccc;padding:3px;">Please login using the username and password provided to you.</legend>
<div style="padding-left:80px">
<label for="username">Username:</label><br/> <input type="text" id="username" name="j_username" size="20" /><br />
<br/>
<label for="password">Password:</label> 
<br/>
<input	id="password" type="password" name="j_password" size="20" />
<div class="error" style="color:red;">

<c:if test="${not empty login_error}">
<br/>
Your login attempt was not successful, try	again. <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />.
 
</c:if>

</div>

<br/>
<input type="submit" value="Login" />
</div>
</fieldset>
</form>
<div>
<p>
<em>We recommend using one of the following web browsers for the best user experience.</em>
<br/>
<a target="_blank" href="http://www.mozilla.org/en-US/firefox/new/">
<img src="<c:url value='/'/>resources/images/firefox_logo.png" alt="Mozilla Firefox" height="40"/>
</a>

<a target="_blank" href="http://www.google.com/chrome">
<img src="<c:url value='/'/>resources/images/chrome_logo.gif" alt="Google Chrome"/>
</a>
</p>
</div>
</div>
</div>
<script type="text/javascript">
$(function() {
	  $("#username").focus();
	});
<!-- Script for Tracking by Google Analytics -->

var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-9789661-1");
pageTracker._trackPageview();
} catch(err) {}
</script>