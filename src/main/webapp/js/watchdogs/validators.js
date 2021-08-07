_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

/*
 * Validate Button
 */
Validator.addValidator( 'Watchdog', function (watchdog) {
	if (watchdog.result.match(/^\s*$/)) {
		_ValidateError('result', 'Result cannot be empty');
		return false;
	}
	
	if ( !Validator.isNumeric(watchdog.withinSeconds) ) {
		_ValidateError('withinSeconds', 'Within seconds must be sequence of digits');
		return false;
	}
	console.log(watchdog);
	if ( watchdog.triggerEventNames.length < 1 ) {
		_ValidateError('triggerEventNames', 'You must select at least one events');
		return false;
	}
	
	return true;
});