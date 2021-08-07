SharedTemplates.setTemplate('freak-list-empty', '\
	<tr>\
	<td><center>There are no Freaks defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('freak-list-row', '\
	<tr>\
	<td>%{name}</td>\
	<td>%{description}</td>\
	<td class="center" width="50px">%{guestText}</td>\
	<td>%{protocol}</td>\
	<td>%{hostname}</td>\
	<td class="center">%{port}</td>\
	<td style="text-align:right"><input type="button" name="Edit Freak" value="Edit" onclick="JSPostNotification(\'EditFreak\', this, {name:\'%{name}\'});"></td>\
	<td style="text-align:right"><input title="Remove Freak" type="button" value="-" onclick="JSPostNotification(\'RemoveFreak\', this, {name:\'%{name}\'});"></td>\
	</tr>\
');