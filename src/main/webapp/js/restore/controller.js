RestoreController = function () {
	
    this.initialise = function (note) {
    	this.registerFormNotificationListener();
		JSDialog.setController(this);
    };
    
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new RestoreListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'dialogShouldRestore', 'DialogShouldRestore');
    };
    
};

RestoreController = new RestoreController();
RestoreController.initialise();
