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
<td><a href="edit.html?systemSetting=${systemSetting.systemSetting}">${systemSetting.systemSetting}</a></td>
<td>${systemSetting.value}</td>
<td>${systemSetting.enabled}</td>
<td><a href="edit.html?systemSetting=${systemSetting.systemSetting}">edit</a> <a href="show.html?systemSetting=${systemSetting.systemSetting}">view</a> <a href="delete.html?systemSetting=${systemSetting.systemSetting}">delete</a></td> </tr>
</c:forEach>
</tbody>
</table>
