CameraController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    /* 
     * Load all the JSON data into the controller. 
     */
    this.initialise = function (note) {
    	var data = note.data;
		
    	this.cameraList = data.cameras;
    	this.builder	= new CameraBuilder();
    	
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildCameraList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised CameraController');
    };
    
    /*
     * Registers a listener for messages we are interested in.
     * TODO: get rid of unused messages and observers. 
     */
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new CameraFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	
    	// add camera dialog events
    	n.addObserver(a, 'didLoadAddCamera'		, 'DidLoadAddActionCamera');
    	n.addObserver(a, 'didShowAddCamera'		, 'DidShowAddCamera');
    	n.addObserver(a, 'shouldCloseAddCamera'	, 'ShouldCloseAddCamera');
    	n.addObserver(a, 'shouldAddCamera'		, 'DialogShouldAddCamera');
    	
    	// edit camera dialog events
    	n.addObserver(a, 'didLoadEditCamera'		, 'DidLoadEditCamera');
    	n.addObserver(a, 'didShowEditCamera'		, 'DidShowEditCamera');
    	n.addObserver(a, 'shouldCloseEditCamera'	, 'ShouldCloseEditCamera');
    	n.addObserver(a, 'shouldEditCamera'			, 'DialogShouldEditCamera');
    	
    	n.addObserver(a, 'editCamera'			, 'EditCamera');
    	
    	n.addObserver(a, 'removeCamera'			, 'RemoveCamera');
    };
    
    this.getCameraList = function () {
    	return this.cameraList;
    };
    
    this.getCameraByIndex = function (index) {
    	try {
    		var list = this.getCameraList();
    		var subjects = list.find( function (key, value) { return value.index == index; } );
    		return subjects.first().value;
    	} catch (e) {
    		return undefined;
    	}
    };
    
    this.getCameraByName = function (name) {
    	try {
    		var list = this.getCameraList();
        	var subjects = list.find( function (key, value) { return value.name == name; } );
    		return subjects.first().value;
    	} catch (e) {
    		return undefined;
    	}
    };
    
    this.replaceCamera = function (replacement) {
    	var cameras = this.cameraList;
    	var camera;
    	for (var i = 0, c = cameras.length; i < c; i++) {
    		camera = cameras[i];
    		if ( camera.index == replacement.index ) {
    			CameraController.cameraList[i] = replacement;
    			console.log( 'Replaced camera with index: ' + replacement.index );
    			return i;
    		}
    	}
    };
    
    this.removeCameraByName = function (name) {
    	var list = this.cameraList;//this.getCameraList();
    	var subjects = list.find( function (key, value) { return value.name == name; } );
		list.remove( subjects.first().key );
    };
    
    /*
     * Gets the first available index for a camera in the list.
     */
    this.getFirstAvailableIndex = function () {
    	var cameras = this.getCameraList();
    	var lastIndex = 0;
    	
    	for (var i = 0, c = cameras.length; i < c; i++) {
    		var camera = cameras[i];
    		var index = camera.index;
    		
    		if (lastIndex == index - 1) {
    			// OK, in sequence
    			lastIndex = index;
    			continue;
    		}else{
    			// GAP, fill this index
    			return lastIndex + 1;
    		}
    		// just add to the list of indexes
    	}
    	
    	return lastIndex + 1;
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
		ShowActivity(true, 'Saving cameras...');
    	
    	var data = Configuration.toJSON( this.getCameraList() );
    	
    	jQuery.ajax( {
            url : 'cameras',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save cameras<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved cameras');
    		}
        });
    };
    
	// kickstart!
    this.load();
};


// singleton
CameraController = new CameraController();