_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Button
 */
Validator.addValidator( 'Button', function (button) {
	if (button.eventName.match(/^\s*$/)) {
		_ValidateError('eventName', 'Event name cannot be empty');
		return false;
	}
	
	if (button.description.match(/^\s*$/)) {
		_ValidateError('description', 'Description cannot be empty');
		return false;
	}
	
	return true;
});