SyntheticHelper = function () {
	
	this.validate = function (synthetic) {
		
		var validator = Validator.getValidator( 'Synthetic' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(synthetic);
	};
};

SyntheticHelper = new SyntheticHelper();