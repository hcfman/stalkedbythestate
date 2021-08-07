RfxcomController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    }
    
    /**
    	Load all the JSON data into the controller.
    */
    this.initialise = function (note) {
    	var data = note.data;
    	
    	this.dummy = {};
		
		this.commandList		= data.commandList;
    	this.availableTypes = ['GENERIC INPUT', 'GENERIC OUTPUT'];
    	this.availableOperators = ['EQ', 'GE', 'LE', 'LT', 'RANGE'];
    	this.builder		= new RfxcomBuilder();
    	
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildCommandList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised rfxcomController');
    }
    
    /**
    	Registers a listener for messages we are interested in.
    	TODO: get rid of unused messages and observers.
    */
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new RfxcomFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	
    	// add action dialog events
    	n.addObserver(a, 'didLoadAddDevice'			, 'DidLoadAddDevice');
    	n.addObserver(a, 'didShowAddDevice'			, 'DidShowAddDevice');
    	n.addObserver(a, 'shouldCloseAddDevice'		, 'ShouldCloseAddDevice');
    	n.addObserver(a, 'shouldAddDevice'			, 'DialogShouldAddDevice');
    	
    	n.addObserver(a, 'removeDevice'			, 'RemoveDevice');
    	n.addObserver(a, 'editDevice'			, 'EditDevice');
    	n.addObserver(a, 'shouldEditDevice'			, 'DialogShouldEditDevice');
    	n.addObserver(a, 'didLoadEditDevice', 'DidLoadEditDevice');
    	n.addObserver(a, 'didShowEditDevice', 'DidShowEditDevice');
    }
    
    this.getCommandList = function () {
    	return this.commandList;
    }
    
    this.removeCommandByName = function (name) {
    	var list = this.getCommandList();
    	var subjects = list.find( function (key, value) { return value.name == name; } );
		list.remove( subjects.first().key );
    }
    
    this.getCommandByName = function (name) {
    	try {
    		var list = this.getCommandList();
        	var subjects = list.find( function (key, value) { return value.name == name; } );
    		return subjects.first().value;
    	} catch (e) {
    		return undefined;
    	}
    };
    
    this.replaceCommand = function (replacement) {
    	var commands = this.commandList;
    	var command;
    	
    	for (var i = 0, c = commands.length; i < c; i++) {
    		command = commands[i];
    		
    		if ( command.name == replacement.name ) {
    			RfxcomController.commandList[i] = replacement;
    			return i;
    		}
    	}
    };
    
    // JSDialog message responders. Most methods won't process the message but forward it
    // to more specific observers that can handle the message.
    this.dialogDidLoad = function ( note ) {
    	var t = note.data.template;
    	t.setObject(this);
    	
    	var event = 'DidLoad' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    }
    
    this.dialogDidShow = function ( note ) {
       	var event = 'DidShow' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    }
    
    this.dialogShouldCancel = function ( note ) {
    	var event = 'ShouldCancel' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    }
	
    this.save = function () {
		ShowActivity(true, 'Saving commands...');
    	
    	var data = Configuration.toJSON( this.getCommandList() );
    	
    	jQuery.ajax( {
            url : 'rfxcom',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save commands<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved commands');
    		}
        });
    };
    
	// kickstart!
    this.load();
}

RfxcomController = new RfxcomController();