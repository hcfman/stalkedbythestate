_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Button
 */
Validator.addValidator( 'Phidget', function (phidget) {
	if (phidget.name.match(/^\s*$/)) {
		_ValidateError('name', 'Name cannot be empty');
		return false;
	}
	
	if (phidget.description.match(/^\s*$/)) {
		_ValidateError('description', 'Description cannot be empty');
		return false;
	}
	
	if (!Validator.isNumeric( phidget.serialNumber )) {
		_ValidateError('serialNumber', 'Serial number must be a sequence of digits');
		return false;
	}
	return true;
});