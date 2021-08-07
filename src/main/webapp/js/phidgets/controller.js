PhidgetController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    /* 
     * Load all the JSON data into the controller. 
     */
    this.initialise = function (note) {
    	var data = note.data;
		
    	this.phidgets	= this.phidgetMapObjectToArray(note.data.phidgetMap);
    	this.builder	= new PhidgetBuilder();
    	
    	this.initialPorts = [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false];
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildPhidgetList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised PhidgetController');
    };
    
    /*
     * Registers a listener for messages we are interested in.
     * TODO: get rid of unused messages and observers. 
     */
    this.registerFormNotificationListener = function () {
    	var a = new PhidgetFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	// add camera dialog events
    	n.addObserver(a, 'didLoadAddPhidget'		, 'DidLoadAddPhidget');
    	n.addObserver(a, 'didShowAddPhidget'		, 'DidShowAddPhidget');
    	n.addObserver(a, 'shouldCloseAddPhidget'	, 'ShouldCloseAddPhidget');
    	n.addObserver(a, 'shouldAddPhidget'			, 'DialogShouldAddPhidget');
    	
    	// edit camera dialog events
    	n.addObserver(a, 'didLoadEditPhidget'		, 'DidLoadEditPhidget');
    	n.addObserver(a, 'didShowEditPhidget'		, 'DidShowEditPhidget');
    	n.addObserver(a, 'shouldCloseEditPhidget'	, 'ShouldCloseEditPhidget');
    	n.addObserver(a, 'shouldEditPhidget'		, 'DialogShouldEditPhidget');
    	
    	n.addObserver(a, 'editPhidget', 'EditPhidget');
    	n.addObserver(a, 'removePhidget', 'RemovePhidget');
    };
    
    this.phidgetMapObjectToArray = function (map) {
    	var list = [];
    	
    	for (var phidgetName in map) {
    		list.push( map[phidgetName] );
    	}
    	
    	return list;
    };
    
    this.getPhidgetList = function () {
    	return this.phidgets;
    };
    
    this.getPhidgetByName = function (name) {
    	var list = this.getPhidgetList();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var phidget = list[i];

    		if (!phidget) {
    			continue;
    		}
    		//alert(phidget.name + ' = ' + name);
    		if (phidget.name == name) {
    			return phidget;
    		}
    	}
    	
    	return undefined;
    };
    
    this.replacePhidget = function (newPhidget) {
    	var list = this.getPhidgetList();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var phidget = list[i];

    		if (!phidget) {
    			continue;
    		}
    		//alert(phidget.name + ' = ' + name);
    		if (newPhidget.name == phidget.name) {
    			list[i] = newPhidget;
    			return i;
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
		ShowActivity(true, 'Saving phidgets...');
    	
    	var data = Configuration.toJSON( this.phidgets );
    	
    	jQuery.ajax( {
            url : 'phidgets',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save phidgets<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved phidgets');
    		}
        });
    };
    
	// kickstart!
    this.load();
};


// singleton
PhidgetController = new PhidgetController();