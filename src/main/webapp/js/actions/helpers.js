ActionHelper = {};

ActionHelper.toggleAdvanced = function(e, callback) {
	jQuery('fieldset.advanced-option *').css('visibility', 'hidden');
	jQuery('fieldset.advanced-option').slideToggle(function() {

		var self = jQuery(e);

		if (self.text() == 'Show Advanced') {
			self.text('Hide Advanced');
		} else {
			self.text('Show Advanced');
		}

		jQuery('fieldset.advanced-option *').css('visibility', 'visible');

		if (callback) {
			callback();
		}
	});
};

ActionHelper.displayAdvanced = function(callback) {
	var a = jQuery('#advancedOptions');
	if (a.text() == 'Show Advanced') {
		this.toggleAdvanced(a, callback);
	} else {
		callback();
	}
};

ActionHelper.getAttributes = function(aAction) {
	if (!aAction) {
		return;
	}

	var fieldUsage = ActionController.actionFieldUsage;
	var attributes = {};
	var abstractAction = new AbstractAction('');
	var actionType = ('action_' + aAction.actionType).toFlattened()
			.toUpperCase();
	var usage = fieldUsage[actionType];

	if (!usage) {
		console
				.error('No field usage information on actionType: '
						+ actionType);
		return;
	}

	// fix pulsetrain not showing
	if (actionType == 'ACTION_PHIDGET_OUTPUT'
			&& aAction.pulseTrain == undefined) {
		aAction.pulseTrain = '';
	}

	for ( var property in aAction) {
		var value = aAction[property];

		if (value != undefined && value.toString) {
			value = value.toString();
		}
		// should skip all attributes that are NOT defined in AbstractAction
		// and are defined in the actionType's fieldUsage
		if (abstractAction[property] != undefined || !usage.hasValue(property)) {
			continue;
		}

		var tmpl;

		// TODO: maybe find a prettier way to check for special cases like this
		if (actionType == 'ACTION_ADD_TAG'
				&& property.toLowerCase() == 'tagname') {
			tmpl = SharedTemplates.getTemplate('attribute-default');
		} else {

			if (actionType == 'ACTION_REMOTE_VIDEO' && property == 'cameraSet') {
				var freak = aAction.freakName;

				if (freak == ''
						&& ActionController.availableFreakNames.length > 0) {
					freak = ActionController.availableFreakNames[0];
				}

				var remotes = ActionController.availableRemoteCameras[freak];

				tmpl = ActionHelper.getAttributeTemplate(property + '-remote');
				tmpl.set('remoteCameras', remotes);
			} else {
				tmpl = ActionHelper.getAttributeTemplate(property);

			}
		}

		tmpl.setObject(ActionController);
		tmpl.setObject(aAction);
		tmpl.set('name', property.toUpperCaseFirst());
		tmpl.set('property', property);
		tmpl.set('value', value);

		// TODO: make specific to DELETE_TAG action
		tmpl.set('deletableTagNames', ActionController.getDeletableTagNames());

		// TODO: make specific to REMOTE_VIDEO action
		tmpl.set('availableFreakNames', ActionController
				.getAvailableFreakNames());

		if (property == 'actionName') {
			tmpl.set('availableActionNames', ActionController
					.getAvailableActionNames());
		}

		if (property == 'url') {
			tmpl.set('attribs', 'class="UrlAttribute"');
		}

		if (property == 'rfxcomCommand') {
			tmpl.set('availableRfxcomCommands',
					ActionController.getAvailableRfxcomCommands());
		}

		if (property == 'responseGroup') {
			tmpl.set('availableButtonGroups',
					ActionController.getAvailableButtonGroups());
		}

		attributes[property] = tmpl.render();
	}

	return attributes;
};

ActionHelper.getAttributeTemplate = function(attr) {
	var name = 'attribute-' + attr.toLowerCase();
	var tmpl;

	if (SharedTemplates.hasTemplate(name)) {
		tmpl = SharedTemplates.getTemplate(name);
	} else if (SharedTemplates.hasTemplate('attribute-default')) {
		tmpl = SharedTemplates.getTemplate('attribute-default');
	}

	return tmpl;
};

ActionHelper.createActionOfType = function(type) {
	switch (type) {
	case ActionType.VIDEO:
		return new VideoAction();
	case ActionType.REMOTE_VIDEO:
		return new RemoteVideoAction();
	case ActionType.EMAIL:
		return new EmailAction();
	case ActionType.SEND_HTTP:
		return new HttpAction();
	case ActionType.ADD_TAG:
		return new AddTagAction();
	case ActionType.DELETE_TAG:
		return new DeleteTagAction();
	case ActionType.DELETE_TAG:
		var action = new TagAction();
		action.actionType = ActionType.DELETE_TAG;
		return action;
	case ActionType.CANCEL_ACTION:
		return new CancelAction();
	case ActionType.WEB_PREFIX:
		return new WebPrefix();
	case ActionType.PHIDGET_OUTPUT:
		return new PhidgetOutputAction();
	case ActionType.RFXCOM:
		return new RfxcomAction();
	default:
		console.warn('No action of type \'' + type + '\' could be created');
		break;
	}
	return undefined;
};

ActionHelper.validate = function(action, options) {
	var actionType = action.actionType;
	var validator = Validator.getValidator(actionType);

	jQuery('div.ui-dialog-content input').removeClass('validation-error');

	return validator.check(action);
};

ActionHelper.hiliteActionWithIndex = function(index) {
	var row = jQuery('#actionList > tr').eq(index);
	jQuery.scrollTo(row, 800);
	row.effect("highlight", {
		color : '#f7d3f0'
	}, 5000);
};

ActionHelper.removeRow = function(e) {
	jQuery(e).parent().parent().remove();
};

ActionHelper.forceProtocol = function(input) {
	if (!input.value.match(/^https?\:\/\//i)) {
		input.value = 'http://'
				+ input.value.replace(/^ht?t?p?s?\:?\/?\/?/i, '');
	}
};

TimeHelper.removeTime = function(rowElement, index) {
	ActionHelper.removeRow(rowElement);
	ActionController.dummy.times.remove(index);

	JSPostNotification('OnWarning', 'Time removed from action');
};

TimeHelper.removeTimeRange = function(rowElement, index) {
	ActionHelper.removeRow(rowElement);
	ActionController.dummy.ranges.remove(index);

	JSPostNotification('OnWarning', 'Range removed from time');
};