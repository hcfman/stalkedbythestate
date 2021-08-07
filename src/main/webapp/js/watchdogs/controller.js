WatchdogController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    /* 
     * Load all the JSON data into the controller. 
     */
    this.initialise = function (note) {
    	var data = note.data;
		
    	this.availableEventNames = note.data.availableEventNames;
    	this.watchdogs	= note.data.watchdogMap;
    	this.builder	= new WatchdogBuilder();
    	
    	this.initialPorts = [false, false, false, false, false, false, false, false];
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildWatchdogList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised WatchdogController');
    };
    
    /*
     * Registers a listener for messages we are interested in.
     * TODO: get rid of unused messages and observers. 
     */
    this.registerFormNotificationListener = function () {
    	var a = new WatchdogFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	// add camera dialog events
    	n.addObserver(a, 'didLoadAddWatchdog'		, 'DidLoadAddWatchdog');
    	n.addObserver(a, 'didShowAddWatchdog'		, 'DidShowAddWatchdog');
    	n.addObserver(a, 'shouldCloseAddWatchdog'	, 'ShouldCloseAddWatchdog');
    	n.addObserver(a, 'shouldAddWatchdog'			, 'DialogShouldAddWatchdog');
    	
    	// edit camera dialog events
    	n.addObserver(a, 'didLoadEditWatchdog'		, 'DidLoadEditWatchdog');
    	n.addObserver(a, 'didShowEditWatchdog'		, 'DidShowEditWatchdog');
    	n.addObserver(a, 'shouldCloseEditWatchdog'	, 'ShouldCloseEditWatchdog');
    	n.addObserver(a, 'shouldEditWatchdog'		, 'DialogShouldEditWatchdog');
    	
    	n.addObserver(a, 'editWatchdog', 'EditWatchdog');
    	n.addObserver(a, 'removeWatchdog', 'RemoveWatchdog');
    };
    
    
    this.getWatchdogList = function () {
    	return this.watchdogs;
    };
    
    this.getWatchdogByName = function (name) {
    	var list = this.getWatchdogList();
    	
    	for (var watchdogName in list) {
    		var watchdog = list[watchdogName];

    		if (!watchdog) {
    			continue;
    		}
    		//alert(watchdog.name + ' = ' + name);
    		if (watchdogName == name) {
    			return watchdog;
    		}
    	}
    	
    	return undefined;
    };
    
    // JSDialog message responders. Most methods won't process the message but forward it
    // to more specific observers that can handle the message.
    this.dialogDidLoad = function ( note ) {
    	var t = note.data.template;
    	t.setObject(this);
    	
    	var event = 'DidLoad' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    };
    
    this.dialogDidShow = function ( note ) {
       	var event = 'DidShow' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    };
    
    this.dialogShouldCancel = function ( note ) {
    	var event = 'ShouldCancel' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    };
	
    this.save = function () {
		ShowActivity(true, 'Saving watchdog events...');
    	
    	var data = Configuration.toJSON( this.watchdogs );
    	
    	// Convert to a list
    	var watchdogsList = [];
    	for (var watchdog in this.watchdogs) {
	    	watchdogsList.push(this.watchdogs[watchdog]);
    	}
    	
    	jQuery.ajax( {
            url : 'watchdogs',
            type : 'POST',
            data : Configuration.toJSON(watchdogsList),
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save watchdog events<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved watchdog events');
    		}
        });
    };
    
	// kickstart!
    this.load();
};


// singleton
WatchdogController = new WatchdogController();