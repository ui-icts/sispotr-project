<%@ include file="/WEB-INF/include.jsp"%>

<div class="container">

<br/>
<div class="span3">
</div>
<div class="well span6">

<h2>Please login</h2>
<form name="f" id="loginForm" action="<c:url value='j_spring_security_check'/>"
	method="POST">
<fieldset style="padding-left:20px">

<label for="username">Username:</label>
<input type="text" id="username" name="j_username" size="20" /><br />
<br/>
<label for="password">Password:</label> 
<input	id="password" type="password" name="j_password" size="20" />
<div class="error">

<c:if test="${not empty login_error}">
<center>
Your login attempt was not successful, try	again. <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />.
</center> 
</c:if>

</div>
<br/>

<input class="btn btn-large btn-primary"  type="submit" value="Sign-In"/>
 or <a  href="register.html">Register for an Account</a>
</fieldset>
</form>
</div>
</div>
<div class="span3">
</div>