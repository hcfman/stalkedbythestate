SystemStatusController = function () {
    this.load = function () {
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    this.initialise = function (note) {
    	var data = note.data;
    	this.builder	= new SystemStatusBuilder();
		JSDialog.setController(this);
        console.log('Initialised System Status Controller');
        
        this.buildStatus(data);
    };
    
    this.buildStatus = function (data) {
    	var b = this.builder;
    	b.buildDiskStatus(data.diskUp, data.diskMessage);
    	b.buildRfxcomStatus(data.rfxcomStatus);
    	b.buildPhidgetStatus(data.phidgetStatus);
    	b.buildCameraStatus(data.cameraStatus);
    };
    
    this.load();
};


// singleton
SystemStatusController = new SystemStatusController();