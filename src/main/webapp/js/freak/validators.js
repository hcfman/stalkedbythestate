_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Button
 */
Validator.addValidator( 'Freak', function (freak) {
	if (freak.name.match(/^\s*$/)) {
		_ValidateError('name', 'Name cannot be empty');
		return false;
	}
	
	if (freak.description.match(/^\s*$/)) {
		_ValidateError('description', 'Description cannot be empty');
		return false;
	}
	
	if (freak.hostname.match(/^\s*$/)) {
		_ValidateError('hostname', 'Hostname cannot be empty');
		return false;
	}
	
	if ( !Validator.isNumeric(freak.port) ) {
		_ValidateError('port', 'Port must be a sequence of digits');
		return false;
	}
	
	return true;
});