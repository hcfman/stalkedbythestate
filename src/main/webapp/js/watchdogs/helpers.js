WatchdogHelper = function () {
	
	this.validate = function (watchdog) {
		
		var validator = Validator.getValidator( 'Watchdog' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(watchdog);
	};
};

WatchdogHelper = new WatchdogHelper();