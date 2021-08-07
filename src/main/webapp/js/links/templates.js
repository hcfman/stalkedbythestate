SharedTemplates.setTemplate('link', '\
<tr>\
<td><a id="%{uid}" name="%{name}" target="_new" href="%{link}">%{name}</a></td>\
<td><input type="button" value="-" onclick="LinkBuilder.removeLink(\'%{uid}\')"></td>\
</tr>\
');