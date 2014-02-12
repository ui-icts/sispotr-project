<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SiteContent</h1>
<div class="box">
<h2>${siteContent.siteContentId}</h2>
<form:form method="post" commandName="sitecontent" action="save.html" ><fieldset>
 
 
 <form:hidden path="siteContentId" />
 
 
 <label for="label">Label</label>
 <form:input path="label"   /><br/>
 
 
 <label for="content">Content</label>
 <form:input path="content"   /><br/>
 
 
 <label></label>
 <input type="submit" value="Save" /><br/>
 
</fieldset>
</form:form></div>
