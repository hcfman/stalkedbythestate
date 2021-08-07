PhidgetHelper = function () {
	
	this.validate = function (phidget) {
		
		var validator = Validator.getValidator( 'Phidget' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(phidget);
	};
	
	this.makePhidget = function (data) {
		var phidget = {};
		phidget.name 					= data.name;
		phidget.description 			= data.description;
		phidget.serialNumber			= data.serialNumber;
		phidget.portSize				= data.portSize;
		phidget.initialInputState 		= [];
		phidget.initialOutputState 		= [];
		phidget.onTriggerEventNames 	= [];
		phidget.offTriggerEventNames 	= [];

		// get all i/o port and trigger stuff
		for (var i = 0; i < 16; i++) {
			var inputState 		= data['inputState' + i];
			var outputState 	= data['outputState' + i];
			var onTrigger 		= data['onTrigger' + i];
			var offTrigger 		= data['offTrigger' + i];
			
			phidget.initialInputState[i] 	= inputState;
			phidget.initialOutputState[i] 	= outputState;
			phidget.onTriggerEventNames[i]	= onTrigger;
			phidget.offTriggerEventNames[i] = offTrigger;
		}
		
		return phidget;
	};
};

PhidgetHelper = new PhidgetHelper();