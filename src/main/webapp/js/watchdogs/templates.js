SharedTemplates.setTemplate('watchdog-list-empty', '\
	<tr>\
	<td><center>There are no combination events defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('watchdog-list-row', '\
	<tr>\
	<td>%{name}</td>\
	<td style="text-align:right"><input type="button" name="Edit Watchdog" value="Edit" onclick="\
		JSPostNotification(\'EditWatchdog\', this, {name:\'%{name}\'});">\
	<input title="Remove Combination Events" type="button" value="-" onclick="JSPostNotification(\'RemoveWatchdog\', this, {name:\'%{name}\'});"></td>\
	</tr>\
');