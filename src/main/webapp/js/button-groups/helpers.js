ButtonGroupHelper = function () {
	this.validate = function (button) {
		var validator = Validator.getValidator( 'Button' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(button);
		
	}
};

ButtonGroupHelper = new ButtonGroupHelper();