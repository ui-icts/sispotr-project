<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SystemSettings List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SystemSettings</th>
 <th>Value</th>
 <th>Enabled</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${systemSettingsList}" var="systemSettings"  varStatus="status"><tr>
<td><a href="edit.html?systemSettingsId=${systemSettings.systemSettings}">${systemSettings.systemSettings}</a></td>
<td>${systemSettings.value}</td>
<td>${systemSettings.enabled}</td>
<td><a href="edit.html?systemSettingsId=${systemSettings.systemSettings}">edit</a> <a href="show.html?systemSettingsId=${systemSettings.systemSettings}">view</a> <a href="delete.html?systemSettingsId=${systemSettings.systemSettings}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
