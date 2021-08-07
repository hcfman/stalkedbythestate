FreakHelper = function () {
	
	this.validate = function (freak) {
		
		var validator = Validator.getValidator( 'Freak' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(freak);
	};
};

FreakHelper = new FreakHelper();