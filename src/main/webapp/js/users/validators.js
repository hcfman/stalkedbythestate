_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('input[name="' + name + '"]').addClass('validation-error').focus().select();
}


Validator.addValidator( 'User', function (data) {
	
	if ( data.name.match(/^\s*$/) ) {
		_ValidateError('name', 'Username cannot be empty');
		return false;
	}
	
	var usernameExists = false;
	// check if username exists
	UsersController.data.users.each( function (k, user) {
		if (user.name == data.name) {
			_ValidateError('name', 'Username exists');
			usernameExists = true;
		}
	});
	
	if (usernameExists == true) {
		return false;
	}
	
	
	if ( data.password.match(/^\s*$/) ) {
		_ValidateError('password', 'Password cannot be empty');
		return false;
	}else{
		
		if ( data.password2 != data.password ) {
			_ValidateError('password2', 'Password check is not identical');
			return false;
		}
		
	}
	
	return true;
});


Validator.addValidator( 'User-edit', function (data) {
	
	if ( data.name.match(/^\s*$/) ) {
		_ValidateError('name', 'Username cannot be empty');
		return false;
	}
	
	if ( !data.password.match(/^\s*$/) ) {
		if ( data.password2 != data.password ) {
			_ValidateError('password2', 'Password check is not identical');
			return false;
		}
	}
	
	return true;
});