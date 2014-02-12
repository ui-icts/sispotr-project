<%@ include file="/WEB-INF/include.jsp"%>

<div class="container">
<h1>Account Registration</h1>
<div class="row-fluid">
<div class="span6 well" >
<h2>Registration Form</h2>
<br/>
<form name="f"  action="register.html"	method="POST">
<fieldset class="form-horizontal">

<div class="control-group">
<label for="username" class="control-label">Username:</label>
<div class="controls">
<input type="text" class="input-large" id="username" name="username" value="${regusername}"/>
 <p class="help-block"></p>
</div>

</div>

<div class="control-group">
<label for="email" class="control-label">Email Address:</label>
<div class="controls">
<input type="text" class="input-xlarge" id="email" name="email"  value="${regemail}" />
 <p class="help-block"></p>
</div>
</div>

<div class="control-group">
<label for="organization" class="control-label">Organization Name:</label>
<div class="controls">
<input type="text" class="input-xlarge" id="organization" name="organization" value="${regorg}"/>
 <p class="help-block"></p>
</div>
</div>

<div class="control-group">
<label for="organization" class="control-label">Industry:</label>
<div class="controls">
<select  class="select" id="industry" name="industry" >
<option>[Select]</option>
<option>Academia</option>

<option>Bio-Technology</option>
<option>Healthcare</option>
<option>Pharmaceuticals</option>
<option>Other</option>
</select>
 <p class="help-block"></p>
</div>
</div>

<div class="control-group">
<label for="password1" class="control-label">Password:</label>
<div class="controls">
<input type="password" class="input-large" id="password1" name="password1" />
<p class="help-block"></p>
</div>
</div>


<div class="control-group">
<label for="password2" class="control-label"></label>
<div class="controls">
<input type="password" class="input-large" id="password2" name="password2" />
 <p class="help-block"><em>re-enter password</em></p>
</div>
</div>
<br/>


<div class="control-group">

<div class="controls">
<input class="btn btn-large btn-primary" type="submit" value="Register" />
<p class="help-block">
<c:if test="${not empty error}">
<div class="error">
${error}
</div>
</c:if>
</p>
</div>
</div>

</fieldset>
</form>
</div>
<div class="span6 well" >
<h2>Why register?</h2>
<p>
Registration gives will give you access to new and advanced features, some of which are not included in this version but will be in the near future.
</p>
</div>
</div>
</div>
