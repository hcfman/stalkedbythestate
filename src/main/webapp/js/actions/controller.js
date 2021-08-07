
ActionController = function () {
	
	this.inEditMode = false;
	
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    /*
     * Load all the JSON data into the controller.
     */
    this.initialise = function (note) {
    	var data = note.data;
		
		this.dummy = {};
		
		this.actionFieldUsage			= data.fieldUsage;
		this.actionTypeNames			= data.actionTypeNames;
    	this.availableEventNames 		= data.availableEventNames;
    	this.availableProfiles 			= data.availableProfiles;
    	this.x10DeviceTypeMap			= data.x10DeviceTypeMap;
    	this.availableCameras 			= data.availableCameras.sortNumeric();
    	this.availableVideoTypes		= ['MJPEG'];
    	this.availableMethodTypes 		= ['GET', 'POST'];
    	this.availableDirections		= data.availableDirections;
    	this.availableProtocols			= ['HTTP', 'HTTPS'];
    	this.availablePhidgets 			= data.availablePhidgets;
    	this.availableRfxcomCommands	= data.availableRfxcomCommands;
    	this.availableButtonGroups		= data.availableButtonGroups;
    	this.phidgedTypes				= ['On', 'Off', 'Pulse'];
    	this.unitTypes					= ['sec', 'ms'];
    	this.availableFreakNames		= data.availableFreakNames;
    	this.availableRemoteCameras 	= data.availableRemoteCameras;
    	this.builder					= new ActionBuilder();
    	
    	// register events we want to listen to
    	this.registerFormNotificationListener();
        
        this.setActionList(data.actions);
		this.builder.buildActionList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised ActionController');
    };
    
    /**
    	Registers a listener for messages we are interested in.
    	TODO: get rid of unused messages and observers.
    */
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new ActionFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	n.addObserver(a, 'actionTypeChanged'	, 'ActionTypeChanged');
    	n.addObserver(a, 'editAction'			, 'EditAction');
    	n.addObserver(a, 'removeAction'			, 'RemoveAction');
    	
    	// add action dialog events
    	n.addObserver(a, 'didLoadAddAction'			, 'DidLoadAddAction');
    	n.addObserver(a, 'didShowAddAction'			, 'DidShowAddAction');
    	n.addObserver(a, 'shouldCloseAddAction'		, 'ShouldCloseAddAction');
    	n.addObserver(a, 'shouldAddAction'			, 'DialogShouldAddAction');
    	
    	// edit action dialog events
    	n.addObserver(a, 'didLoadEditAction'		, 'DidLoadEditAction');
    	n.addObserver(a, 'didShowEditAction'		, 'DidShowEditAction');
    	n.addObserver(a, 'shouldCloseEditAction'	, 'ShouldCloseEditAction');
    	n.addObserver(a, 'shouldEditAction'			, 'DialogShouldEditAction');
    	
    	// add time dialog events
    	n.addObserver(a, 'didLoadAddTime'			, 'DidLoadAddTime');
    	n.addObserver(a, 'shouldCloseAddTime'		, 'ShouldCloseAddTime');
    	n.addObserver(a, 'shouldAddTime'			, 'DialogShouldAddTime');
    	
    	// add timerange dialog events
    	n.addObserver(a, 'didLoadAddTimeRange'		, 'DidLoadAddTimeRange');
    	n.addObserver(a, 'shouldCloseAddTimeRange'	, 'ShouldCloseAddTimeRange');
    	n.addObserver(a, 'shouldAddTimeRange'		, 'DialogShouldAddTimeRange');
    	
    	// add parameter key-value set
    	n.addObserver(a, 'didLoadAddParameter'		, 'DidLoadAddParameter');
    	n.addObserver(a, 'shouldCloseAddParameter'	, 'ShouldCloseAddParameter');
    	n.addObserver(a, 'shouldAddParameter'		, 'DialogShouldAddParameter');
    	
    	n.addObserver(a, 'phidgetActionTypeChanged'		, 'PhidgetActionTypeChanged');
    	
    	n.addObserver(this, 'dialogShouldCancel', 'DialogShouldCancel');
    	n.addObserver(this, 'dialogShouldAddCounter', 'DialogShouldAddCounter');
    };
    
    this.getAvailableTagNames = function () {
    	var tagsHash = {};
    	var tags 	= [];
    	var actions = Configuration.actionList;
    	
    	// Add profiles
    	for (var i = 0, c= this.availableProfiles.length; i < c; i++ ) {
    		tagsHash[ this.availableProfiles[i] ] = true;
    	}
    	
    	// Add tagNames
    	for ( var i = 0, c = actions.length; i < c; i++ ) {
    		var action = actions[i];
    		if (action.actionType == ActionType.ADD_TAG) {
    			tagsHash[ action.tagName ] = true;
    		}
    	}
    	
    	for (var tagName in tagsHash) {
	    	tags.push(tagName);
    	}
    	
    	return tags.sort();
    };
    
    this.getDeletableTagNames = function () {
    	var tagsHash = {};
    	var tags 	= [];
    	var actions = Configuration.actionList;
    	
    	// Add tagNames
    	for ( var i = 0, c = actions.length; i < c; i++ ) {
    		var action = actions[i];
    		if (action.actionType == ActionType.ADD_TAG) {
    			tagsHash[ action.tagName ] = true;
    		}
    	}
    	
    	for (var tagName in tagsHash) {
	    	tags.push(tagName);
    	}
    	
    	return tags.sort();
    };
    
    this.getAvailableFreakNames = function () {
    	return ActionController.availableFreakNames;
    }
    
    this.getAvailableRfxcomCommands = function() {
    	return ActionController.availableRfxcomCommands;
    }
    
    this.getAvailableButtonGroups = function() {
    	return ActionController.availableButtonGroups;
    }
    
    this.getAvailableActionNames = function () {
    	var actionsHash = {};
    	var names 	= [];
    	var actions = Configuration.actionList;
    	
    	// Add actionNames
    	for ( var i = 0, c = actions.length; i < c; i++ ) {
    		var action = actions[i];
			actionsHash[ action.name ] = true;
    	}
    	
    	for (var actionName in actionsHash) {
	    	names.push(actionName);
    	}
    	
    	return names.sort();
    };
    
    
    this.setActionList = function (actions) {
    	Configuration.actionList = actions;
    };
    
    this.getActionList = function () {
    	return Configuration.actionList;
    };
    
    this.getActionByName = function (name) {
    	var actions = Configuration.actionList;
    	var action;
    	for (var i = 0, c = actions.length; i < c; i++) {
    		action = actions[i];
    		if ( action.name == name ) {
    			return action;
    		}
    	}
    	console.warn( 'Action with name \'' + name + '\' not found' );
    	return undefined;
    };
    
    this.removeActionByName = function (name) {
    	var actions = Configuration.actionList;
    	var action;
    	for (var i = 0, c = actions.length; i < c; i++) {
    		action = actions[i];
    		if ( action.name == name ) {
    			Configuration.actionList.remove(i);
    			console.log( 'Removed action with name: ' + name );
    			break;
    		}
    	}
    };
    
    this.replaceActionByName = function (name, newAction) {
    	var actions = Configuration.actionList;
    	var action;
    	for (var i = 0, c = actions.length; i < c; i++) {
    		action = actions[i];
    		if ( action.name == name ) {
    			Configuration.actionList[i] = newAction;
    			console.log( 'Replaced action with name: ' + name );
    			return i;
    		}
    	}
    };
    
    this.getActionNames = function () {
    	var actions = Configuration.actionList;
    	var names = [];
    	for (var i = 0, c = actions.length; i < c; i++) {
    		action = actions[i];
    		names.push(action.name);
    	}
    	return names;
    };
    
    this.setDummyActionByName = function (name) {
    	var action = this.getActionByName(name);
    	if (action) {
    		this.dummy = action;
    		return action;
    	}else{
    		console.warn( 'Failed to set edited action\nCould not find ' + name);
    	}
    	return undefined;
    };
    
    this.cleanupTemporaryObjects = function (name) {
    	var skip = ['AddTime', 'AddTimeRange'];
    	if ( !skip.hasValue(name) ) {
    		this.dummy = {};
    	}
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
    	
    	this.inEditMode = false;
    	
    	var event = 'ShouldCancel' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    };
    
    this.dialogShouldAddCounter = function( note ) {
    	var eventCounter = {count: note.data.count, withinSeconds: note.data.withinSeconds};
    	ActionController.dummy.eventCounter = eventCounter;
    	ActionController.builder.buildCounter(eventCounter);
    };
	
    
    /*
     * Save actions
     */
    this.save = function () {
    	ShowActivity(true, 'Saving actions...');
    	
    	var data = Configuration.toJSON( this.getActionList() );
    	
    	jQuery.ajax( {
            url : 'actions',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save actions<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved actions');
    			// TODO: do stuff with response...
    		}
        });
    };
    
    
	// kickstart!
    this.load();
};


// singleton
ActionController = new ActionController();