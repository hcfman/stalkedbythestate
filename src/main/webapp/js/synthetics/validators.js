_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Button
 */
Validator.addValidator( 'Synthetic', function (synthetic) {
	if (synthetic.result.match(/^\s*$/)) {
		_ValidateError('result', 'Result cannot be empty');
		return false;
	}
	
	if ( !Validator.isNumeric(synthetic.withinSeconds) ) {
		_ValidateError('withinSeconds', 'Within seconds must be sequence of digits');
		return false;
	}
	console.log(synthetic);
	if ( synthetic.triggerEventNames.length < 2 ) {
		_ValidateError('triggerEventNames', 'You must at least select two events');
		return false;
	}
	
	return true;
});