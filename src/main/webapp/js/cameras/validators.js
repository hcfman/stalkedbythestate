_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}
/*
 * Function with checks that are common for all actions
 */
_DefaultActionValidation = function (camera) {
	if ( camera.name.match(/^\s*$/) ) {
		_ValidateError('name', 'Name cannot be empty');
		return false;
	}
	
	if ( camera.description.match(/^\s*$/) ) {
		_ValidateError('description', 'Description cannot be empty');
		return false;
	}
	
	if ( !Validator.isURL(camera.url) ) {
		_ValidateError('url', 'Not a valid URL. Example: http://www.example.com');
		return false;
	}
	
	if ( camera.username.match(/^\s*$/) ) {
		_ValidateError('username', 'Username cannot be empty');
		return false;
	}
	
	if ( camera.password.match(/^\s*$/) ) {
		_ValidateError('password', 'Password cannot be empty');
		return false;
	}
	
	if ( !Validator.isNumeric( camera.continueSeconds ) ) {
		_ValidateError('continueSeconds', 'ContinueSeconds must be a number');
		return false;
	}
	
	if ( !Validator.isNumeric( camera.bufferSeconds ) ) {
		_ValidateError('bufferSeconds', 'bufferSeconds must be a number');
		return false;
	}
	
	if ( !Validator.isNumeric( camera.priority ) ) {
		_ValidateError('priority', 'Priority must be a number');
		return false;
	}
	
	return true;
}


/*
 * Validate Camera
 */
Validator.addValidator( 'Camera', function (camera) {
	if ( !_DefaultActionValidation(camera) ) {
		return false;
	}
	
	return true;
});