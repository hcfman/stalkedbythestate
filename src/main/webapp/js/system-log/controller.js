SystemLogController = function () {
    this.load = function () {
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    this.initialise = function (note) {
    	var data = note.data;
		JSDialog.setController(this);
        console.log('Initialised System Log Controller');
        
        var lines = data.log.split('\n');
        lines.reverse();
        
        jQuery('#log').text(lines.join('\n'));
    };
    
    this.load();
};


// singleton
SystemLogController = new SystemLogController();