SharedTemplates.setTemplate('camera-list-empty', '\
	<tr>\
	<td><center>There are no cameras defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('camera-list-row', '\
	<tr>\
	<td class="center">%{index}</td>\
	<td>%{name}</td>\
	<td>%{description}</td>\
	<td class="center">%{priority}</td>\
	<td class="center"><img alt="%{isCacheAllowed}" src="images/%{isCacheAllowed}"></td>\
	<td class="center" width="50px"><img alt="%{isGuest}" src="images/%{guestImage}"></td>\
	<td style="text-align:right"><input class="osx" type="button" name="Edit Camera" value="Edit" onclick="\
		JSPostNotification(\'EditCamera\', this, {index:\'%{index}\'});"></td>\
	<td style="text-align:right"><input title="Remove Camera" type="button" value="-" onclick="JSPostNotification(\'RemoveCamera\', this, {name:\'%{name}\'});"></td>\
	</tr>\
');