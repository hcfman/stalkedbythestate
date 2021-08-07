_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Button
 */
Validator.addValidator( 'Schedule', function (schedule) {
	if (schedule.name.match(/^\s*$/)) {
		_ValidateError('name', 'Name cannot be empty');
		return false;
	}
	
	if (schedule.eventName.match(/^\s*$/)) {
		_ValidateError('eventName', 'Event name cannot be empty');
		return false;
	}
	
	return true;
});