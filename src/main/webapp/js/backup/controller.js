BackupController = function () {
	
    this.initialise = function (note) {
    	this.registerFormNotificationListener();
		JSDialog.setController(this);
    };
    
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new BackupListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'dialogShouldBackup', 'DialogShouldBackup');
    };
    
};

BackupController = new BackupController();
BackupController.initialise();
