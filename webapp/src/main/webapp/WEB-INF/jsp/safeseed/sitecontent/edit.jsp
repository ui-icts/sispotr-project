<%@ include file="/WEB-INF/include.jsp"  %>

<div class="row-fluid">
<h1>Site Content</h1>
<div class="span11 well">
<form:form method="post" commandName="siteContent" action="save.html" >
<fieldset>
  
 <form:hidden path="siteContentId" />
 
 
 <label for="label">Label</label>
 <form:input path="label"   /><br/>
 
  <label for="page">Path to output page</label>
 <form:input path="page"  /><br/>eg: tools/sispotr/design.html
 
 <label for="content">Content</label>
 <form:textarea path="content" class="input-xlarge" rows="10" /><br/>
 
 
 <label></label>
 <input type="submit" value="Save" class="btn"/><br/>
 
</fieldset>
</form:form>
</div>
</div>
