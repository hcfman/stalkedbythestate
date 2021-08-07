ButtonGroupFormListener = function () {
	this.shouldAddButtonGroup = function (note) {
		var name = note.data.name;
		var groupExists = ButtonGroupController.buttonGroupList[name] != undefined;
		
		if (!groupExists) {
			ButtonGroupController.buttonGroupList[name] = [];
			JSPostNotification('OnInfo', 'Added group: ' + name);
		}else{
			JSPostNotification('OnError', name + ' is already taken');
		}
		
		ButtonGroupController.builder.buildButtonGroupList();
	};
	
	this.removeButton = function (name, buttonName) {
		ButtonGroupController.removeButtonByName(name, buttonName);
		ButtonGroupController.builder.buildButtonGroupList();
	};
	
	this.removeButtonGroup = function (name) {
		ButtonGroupController.removeButtonGroupByName(name);
		ButtonGroupController.builder.buildButtonGroupList();
	};
	
	this.shouldAddButton = function (note) {
		var groupName = note.sender.title;
		
		note.data.guest = note.data.guest == 'true';
		
		if (ButtonGroupHelper.validate(note.data)) {
			ButtonGroupController.buttonGroupList[groupName].push( note.data );
			ButtonGroupController.builder.buildButtonGroupList();
		}else{
			return false;
		}
	};
	
	// called from withing template
	this.editButton = function (groupName, buttonName, sender) {
		var subject = ButtonGroupController.getButtonInGroup(groupName, buttonName);
		sender._groupName = groupName;
		JSDialog.openDialog('jsp/content/forms/button-new.html', sender, subject, {buttonName:'Save'});
	};
	
	this.didLoadEditButton = function (note) {
		var button 	= note.data.subject;
		var tmpl	= note.data.template;
		
		tmpl.set('groupName', note.sender._groupName);
		tmpl.set('eventName', button.eventName);
		tmpl.set('description', button.description);
	};
	
	this.didShowEditButton = function (note) {
		var button 	= note.data.subject;
		var isGuest	= button.guest ? 'true' : 'false';
		jQuery('#newButtonForm input[name="eventName"]').attr('disabled', true);
		jQuery('#newButtonForm select[name="guest"] option').each(function () {
			if (this.value == isGuest) {
				this.selected = true;
			}
		});
	};
	
	this.shouldEditButton = function (note) {
		var button	= note.data;
		var group	= button.groupName;
		
		button.guest = button.guest == 'true';
		
		// not needed anymore
		delete button.groupName;
		
		if (ButtonGroupHelper.validate(button)) {
			ButtonGroupController.replaceButtonInGroup(group, button);
			ButtonGroupController.builder.buildButtonGroupList();
		}else{
			return false;
		}
	};
}