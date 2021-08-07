_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}
/*
 * Function with checks that are common for all actions
 */
_DefaultActionValidation = function (command) {
	if ( command.name.match(/^\s*$/) ) {
		_ValidateError('name', 'Name cannot be empty');
		return false;
	}
	
	if ( command.description.match(/^\s*$/) ) {
		_ValidateError('description', 'Description cannot be empty');
		return false;
	}
	
	/*
	 * TODO: the rfxcom adding/editing should only show onTriggerEventName and 
	 * the offtrigger one when the type is "Pir". Otherwise it should be set 
	 * to the default value of ""
	 */
	if (command.rfxcomType.match(/GENERIC INPUT/i)) {
		if ( command.eventName.match(/^\s*$/) ) {
			_ValidateError('eventName', 'Event Name cannot be empty');
			return false;
		}
	} else {
		command.eventName = "";
	}
	return true;
}


/*
 * Validate RFXCOM Command
 */
Validator.addValidator( 'rfxcom', function (command) {
	if ( !_DefaultActionValidation(command) ) {
		return false;
	}
	
	return true;
});