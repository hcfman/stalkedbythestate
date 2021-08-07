_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Button
 */
Validator.addValidator( 'Profile', function (profile) {

	if (profile.name.match(/^\s*$/)) {
		_ValidateError('name', 'Name cannot be empty');
		return false;
	}
	
	if (profile.description.match(/^\s*$/)) {
		_ValidateError('description', 'Description cannot be empty');
		return false;
	}
	
	if (profile.tagname.match(/^\s*$/)) {
		_ValidateError('tagname', 'Tagname cannot be empty');
		return false;
	}
	
	var times = ProfileController.dummy.times;
	
	if (times && times.length > 0) {
		//times = ProfileController.dummy.times;
	}else{
		_ValidateError('Add time', 'Please set a time');
		return false;
	}
	return true;
});