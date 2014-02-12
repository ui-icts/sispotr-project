<%@ include file="/WEB-INF/include.jsp"  %>
<h1>SystemSetting List</h1>

<div class="button"><a href="add.html">Add</a></div>
<table class="tableData1">
<thead>
<tr>
 <th>SystemSetting</th>
 <th>Value</th>
 <th>Enabled</th>
<th></th></tr>
</thead>
<tbody>
<c:forEach items="${systemSettingList}" var="systemSetting"  varStatus="status"><tr>
<td><a href="edit.html?systemSettingId=${systemSetting.systemSetting}">${systemSetting.systemSetting}</a></td>
<td>${systemSetting.value}</td>
<td>${systemSetting.enabled}</td>
<td><a href="edit.html?systemSettingId=${systemSetting.systemSetting}">edit</a> <a href="show.html?systemSettingId=${systemSetting.systemSetting}">view</a> <a href="delete.html?systemSettingId=${systemSetting.systemSetting}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
