_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
};
/*
 * Function with checks that are common for all actions
 */
_DefaultActionValidation = function (action) {
	if ( action.name.match(/^\s*$/) ) {
		_ValidateError('name', 'Action name cannot be empty');
		return false;
	}
	
	if ( !ActionController.inEditMode && ActionController.getActionNames().hasValue(action.name) ) {
		_ValidateError('name', 'Action name is not unique');
		return false;
	}
	
	if ( action.description.match(/^\s*$/) ) {
		_ValidateError('description', 'Action description cannot be empty');
		return false;
	}
	
	if ( !action.delay.match(/^\d+$/) ) {
		ActionHelper.displayAdvanced( function () {
			_ValidateError('delay', 'Action delay must be a number');
		});
		return false;
	}
	
	if ( !action.hysteresis.match(/^\d+$/) ) {
		ActionHelper.displayAdvanced( function () {
			_ValidateError('hysteresis', 'Action hysteresis must be a number');
		});
		return false;
	}
	
	return true;
};


/*
 * Validate VIDEO action
 */
Validator.addValidator( 'VIDEO', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	return true;
});


/*
 * Validate EMAIL action
 */
Validator.addValidator( 'EMAIL', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	if ( !Validator.isEmail( action.to )) {
		_ValidateError('to', 'Not a valid e-mailaddress');
		return false;
	}
	
	return true;
});


/*
 * Validate PHIDGET_OUTPUT action
 */
Validator.addValidator( 'PHIDGET OUTPUT', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	if ( action.phidgetName.match( /^\s*$/ )) {
		_ValidateError('phidgetName', 'Phidgetname cannot be empty');
		return false;
	}
	
	if ( !Validator.isNumeric( action.port )) {
		_ValidateError('port', 'Port must be a number');
		return false;
	}
	
	var isPulse = action.phidgetActionType.toLowerCase() == 'pulse';
	if ( isPulse ) {
		if (!action.pulseTrain.match( /^\d+(?:,\d+)*$/ )) {
			_ValidateError('pulseTrain', 'PulseTrain must be a comma separated sequence of digits');
			return false;
		}
		
		if (!action.pulseTrain.match( /^\d+(?:,\d+,\d+)*$/ )) {
			_ValidateError('pulseTrain', 'PulseTrain must be an odd number of digit group');
			return false;
		}
	}
	
	return true;
});


/*
 * Validate PHIDGET_OUTPUT action
 */
Validator.addValidator( 'RFXCOM', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	if ( action.rfxcomCommand.match( /^\s*$/ )) {
		_ValidateError('rfxcomCommand', 'Command cannot be empty');
		return false;
	}
	
	return true;
});


/*
 * Validate ADD_TAG action
 */
Validator.addValidator( 'ADD TAG', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	if ( action.tagName.match(/^\s+$/) ) {
		_ValidateError('tagName', 'You must enter a tagname');
		return false;
	}
	
	if ( !Validator.isNumeric(action.validFor) ) {
		_ValidateError('validFor', 'ValidFor must be a number');
		return false;
	}
	
	return true;
});


/*
 * Validate DELETE_TAG action
 */
Validator.addValidator( 'DELETE TAG', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	return true;
});


/*
 * Validate SEND_HTTP action
 */
Validator.addValidator( 'SEND HTTP', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	return true;
});


/*
 * Validate REMOTE_VIDEO action
 */
Validator.addValidator( 'REMOTE VIDEO', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	return true;
});


/*
 * Validate DELETE_ACTION action
 */
Validator.addValidator( 'CANCEL ACTION', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	return true;
});


/*
 * Validate WEB_PREFIX action
 */
Validator.addValidator( 'WEB PREFIX', function (action) {
	if ( !_DefaultActionValidation(action) ) {
		return false;
	}
	
	return true;
});