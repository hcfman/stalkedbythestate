_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Email
 */
Validator.addValidator( 'Email', function (data) {
	if ( data.name.match(/^\s*$/) ) {
		_ValidateError('name', 'Name cannot be empty');
		return false;
	}
	
	if ( data.description.match(/^\s*$/) ) {
		_ValidateError('description', 'Description cannot be empty');
		return false;
	}
	
	if ( data.mailhost.match(/^\s*$/) ) {
		_ValidateError('mailhost', 'Mail host cannot be empty');
		return false;
	}
	
	if ( !Validator.isNumeric(data.port) ) {
		_ValidateError('port', 'Port must be a number');
		return false;
	}
	
	if ( data.from.match(/^\s*$/) ) {
		_ValidateError('from', 'From address cannot be empty');
		return false;
	}
	
	return true;
});