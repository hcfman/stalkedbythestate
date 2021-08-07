ButtonGroupBuilder = function () {
	this.buildButtonGroupList = function () {
		var table = '';
		
		var groupTmpl = SharedTemplates.getTemplate('buttongroup-group');
		var buttonTmpl = SharedTemplates.getTemplate('buttongroup-list-row');
		var buttonGroups = ButtonGroupController.getButtonGroupList();
		
		// render all actions
		for (var groupName in buttonGroups) {
			
			var group = buttonGroups[groupName];
			var list = '';
			
			if (!group) {
				continue;
			}
			
			groupTmpl.set('name', groupName);
			groupTmpl.set('buttonsUrl', 'buttons?group=' + groupName);
			
			for (var i = 0, c = group.length; i < c; i++) {
				var button = group[i];
				
				buttonTmpl.setObject(button);
				buttonTmpl.set('isGuest', button.guest ? 'Guest' : '');
				buttonTmpl.set('groupName', groupName);
				
				list += buttonTmpl.render();
				buttonTmpl.reset();
			}
			
			groupTmpl.set('buttons', list);
			table += groupTmpl.render();
			groupTmpl.reset();
		}
		
		//tmpl = SharedTemplates.getTemplate('buttongroup-list-empty');
		//list += tmpl.render();
		
		
		jQuery('#buttonGroupList').html(table);
	}
}