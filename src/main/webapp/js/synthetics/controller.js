SyntheticController = function () {
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
    	this.synthetics	= note.data.syntheticMap;
    	this.builder	= new SyntheticBuilder();
    	
    	this.initialPorts = [false, false, false, false, false, false, false, false];
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildSyntheticList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised SyntheticController');
    };
    
    /*
     * Registers a listener for messages we are interested in.
     * TODO: get rid of unused messages and observers. 
     */
    this.registerFormNotificationListener = function () {
    	var a = new SyntheticFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	// add camera dialog events
    	n.addObserver(a, 'didLoadAddSynthetic'		, 'DidLoadAddSynthetic');
    	n.addObserver(a, 'didShowAddSynthetic'		, 'DidShowAddSynthetic');
    	n.addObserver(a, 'shouldCloseAddSynthetic'	, 'ShouldCloseAddSynthetic');
    	n.addObserver(a, 'shouldAddSynthetic'			, 'DialogShouldAddSynthetic');
    	
    	// edit camera dialog events
    	n.addObserver(a, 'didLoadEditSynthetic'		, 'DidLoadEditSynthetic');
    	n.addObserver(a, 'didShowEditSynthetic'		, 'DidShowEditSynthetic');
    	n.addObserver(a, 'shouldCloseEditSynthetic'	, 'ShouldCloseEditSynthetic');
    	n.addObserver(a, 'shouldEditSynthetic'		, 'DialogShouldEditSynthetic');
    	
    	n.addObserver(a, 'editSynthetic', 'EditSynthetic');
    	n.addObserver(a, 'removeSynthetic', 'RemoveSynthetic');
    };
    
    
    this.getSyntheticList = function () {
    	return this.synthetics;
    };
    
    this.getSyntheticByName = function (name) {
    	var list = this.getSyntheticList();
    	
    	for (var syntheticName in list) {
    		var synthetic = list[syntheticName];

    		if (!synthetic) {
    			continue;
    		}
    		//alert(synthetic.name + ' = ' + name);
    		if (syntheticName == name) {
    			return synthetic;
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
		ShowActivity(true, 'Saving combination events...');
    	
    	var data = Configuration.toJSON( this.synthetics );
    	
    	// Convert to a list
    	var syntheticsList = [];
    	for (var synth in this.synthetics) {
	    	syntheticsList.push(this.synthetics[synth]);
    	}
    	
    	jQuery.ajax( {
            url : 'synthetics',
            type : 'POST',
            data : Configuration.toJSON(syntheticsList),
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save combination events<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved combination events');
    		}
        });
    };
    
	// kickstart!
    this.load();
};


// singleton
SyntheticController = new SyntheticController();