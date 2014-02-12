<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SystemSettings</h1>
<div class="box">
<h2>${systemSettings.systemSettings}</h2>
<form:form method="post" commandName="systemsettings" action="save.html" ><fieldset>
 
 
 <form:hidden path="systemSettings" />
 
 
 <label for="value">Value</label>
 <form:input path="value"   /><br/>
 
 
 <label for="enabled">Enabled</label>
 <form:input path="enabled"   /><br/>
 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
