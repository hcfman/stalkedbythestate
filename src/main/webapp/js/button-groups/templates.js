SharedTemplates.setTemplate('buttongroup-list-empty', '\
	<tr>\
	<td><center>There are no groups defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('buttongroup-list-row', '\
	<tr>\
	<td>%{eventName}</td>\
	<td>%{description}</td>\
	<td>%{isGuest}</td>\
	<td>\
		<input type="button" onclick="ButtonGroupController.listener.editButton(\'%{groupName}\', \'%{eventName}\', this)" value="Edit" name="EditButton">\
		<input type="button" onclick="ButtonGroupController.listener.removeButton(\'%{groupName}\', \'%{eventName}\')" value="-"></td>\
	</tr>\
');

SharedTemplates.setTemplate('buttongroup-group', '\
	<tr>\
	<td class="group-name">%{name} <input type="button" onclick="ButtonGroupController.listener.removeButtonGroup(\'%{name}\')" value="-"></td>\
	<td><input type="button" title="%{name}" onclick="JSDialog.openDialog(\'jsp/content/forms/button-new.html\', this, null, {buttonName:\'Add\'})" value="Add Button" name="Add Button">\
		<table class="buttons round">%{buttons}</table></td>\
	<td><a href="%{buttonsUrl}">%{buttonsUrl}</a></td>\
	</tr>\
');