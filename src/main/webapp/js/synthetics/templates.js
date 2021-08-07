SharedTemplates.setTemplate('synthetic-list-empty', '\
	<tr>\
	<td><center>There are no combination events defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('synthetic-list-row', '\
	<tr>\
	<td>%{name}</td>\
	<td style="text-align:right"><input type="button" name="Edit Synthetic" value="Edit" onclick="\
		JSPostNotification(\'EditSynthetic\', this, {name:\'%{name}\'});">\
	<input title="Remove Combination Events" type="button" value="-" onclick="JSPostNotification(\'RemoveSynthetic\', this, {name:\'%{name}\'});"></td>\
	</tr>\
');