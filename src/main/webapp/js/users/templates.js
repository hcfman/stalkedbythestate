SharedTemplates.setTemplate('user-row', '\
<tr>\
<td>%{name}</td>\
<td>%{role}</td>\
<td>\
	<input type="button" onclick="JSDialog.openDialog(\'jsp/content/forms/user-new.html\', this, \'%{name}\', {buttonName:\'Save\'})" value="edit" name="Edit User">\
	<input type="button" onclick="UsersController.deleteUser(\'%{name}\')" value="-">\
</td>\
</tr>');