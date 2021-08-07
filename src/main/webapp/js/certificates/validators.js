_ValidateError = function (name, message) {
	JSPostNotification('OnError', message);
	jQuery('div.ui-dialog-content input[name="' + name + '"]').addClass('validation-error').focus().select();
}

Validator.addValidator( 'certificate', function (certificate) {
		
	if ( !certificate.country.match(/^[A-Z]{2}$/) ) {
		_ValidateError('country', 'Country is invalid');
		return false;
	}
	
	if ( !certificate.state.match(/^[a-z ]+$/i) ) {
		_ValidateError('state', 'State is invalid');
		return false;
	}
	
	if ( !certificate.locality.match(/^[a-z ]+$/i) ) {
		_ValidateError('locality', 'Locatity is invalid');
		return false;
	}
	
	if ( !certificate.organisation.match(/^[a-z ]+$/i) ) {
		_ValidateError('organisation', 'Organisation is invalid');
		return false;
	}
	
	if ( !certificate.organisationalUnit.match(/^[a-z ]+$/i) ) {
		_ValidateError('organisationalUnit', 'Organisation Unit is invalid');
		return false;
	}
	
	if ( Validator.isEmpty(certificate.commonName) ) {
		_ValidateError('commonName', 'Common Name cannot be empty');
		return false;
	}
	
	/*
	if ( !certificate.alias.match(/^\w+$/) ) {
		_ValidateError('alias', 'Alias is invalid');
		return false;
	}
	
	if (!SystemHelper.isValidCertificateAlias(certificate.alias)) {
		_ValidateError('alias', 'This alias is not allowed or already taken');
		return false;
	}
	*/
	if ( !certificate.validity.match(/^\d+$/) ) {
		_ValidateError('validity', 'Validity must be a sequence of digits');
		return false;
	}
	
	return true;
});


Validator.addValidator( 'fetchinfo', function (fetchInfo) {
	
	if ( !fetchInfo.alias.match(/^\w+$/) ) {
		_ValidateError('alias', 'Alias is invalid');
		return false;
	}
	
	if (!SystemHelper.isValidCertificateAlias(fetchInfo.alias)) {
		_ValidateError('alias', 'This alias is not allowed or already taken');
		return false;
	}
	
	if ( Validator.isEmpty(fetchInfo.hostname) ) {
		_ValidateError('hostname', 'Hostname cannot be empty');
		return false;
	}
	
	if ( !Validator.isNumeric(fetchInfo.port) ) {
		_ValidateError('port', 'Port must be a sequence of digits');
		return false;
	}
	
	return true;
});