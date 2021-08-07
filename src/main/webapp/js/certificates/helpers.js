CertificateHelper = function () {
	this.validateCertificate = function (certificate) {
		var validator = Validator.getValidator('certificate');
		
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
		
		return validator.check(certificate);
	};
	
	this.validateFetchInfo = function (info) {
		var validator = Validator.getValidator('fetchinfo');
		
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
		
		return validator.check(info);
	};
	
	this.isValidCertificateAlias = function (alias, store) {
		var disallowedAliases = CertificateController.certificateData.disallowedAliases;
		
		if (alias.match(/^\s*$/)) {
			return false;
		}
		
		if (disallowedAliases.hasValue(alias)) {
			return false;
		}
		
		var certs = CertificateController.certificateData[store];
		
		for (var i = 0, c = certs.length; i < c; i++) {
			var cert = certs[i];
			if (cert.alias == alias) {
				return false;
			}
		}
		
		return true;
	};
};

CertificateHelper = new CertificateHelper();