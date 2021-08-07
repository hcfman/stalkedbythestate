FreakController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    }
    
    /**
    	Load all the JSON data into the controller.
    */
    this.initialise = function (note) {
    	var data = note.data;
		
		this.freakList		= data.freaks;
    	this.builder		= new FreakBuilder();
    	
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildFreakList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised FreakController');
    }
    
    /**
    	Registers a listener for messages we are interested in.
    	TODO: get rid of unused messages and observers.
    */
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new FreakFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	
    	// add action dialog events
    	n.addObserver(a, 'didLoadAddFreak'			, 'DidLoadAddActionFreak');
    	n.addObserver(a, 'didShowAddFreak'			, 'DidShowAddFreak');
    	n.addObserver(a, 'shouldCloseAddFreak'		, 'ShouldCloseAddFreak');
    	n.addObserver(a, 'shouldAddFreak'			, 'DialogShouldAddFreak');
    	
    	// edit action dialog events
    	n.addObserver(a, 'editFreak'			, 'EditFreak');
    	
    	n.addObserver(a, 'didLoadEditFreak'			, 'DidLoadEditFreak');
    	n.addObserver(a, 'didShowEditFreak'			, 'DidShowEditFreak');
    	n.addObserver(a, 'shouldCloseEditFreak'		, 'ShouldCloseEditFreak');
    	n.addObserver(a, 'shouldEditFreak'			, 'DialogShouldEditFreak');
    	
    	n.addObserver(a, 'removeFreak', 'RemoveFreak');
    }
    
    this.getFreakList = function () {
    	return this.freakList;
    }
    
    this.removeFreakByName = function (name) {
    	var list = this.getFreakList();
    	var subjects = list.find( function (key, value) { return value.name == name; } );
		list.remove( subjects.first().key );
    }
    
    this.getFreakByName = function (name) {
    	var list = this.getFreakList();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var freak = list[i];

    		if (!freak) {
    			continue;
    		}
    		//alert(freak.name + ' = ' + name);
    		if (freak.name == name) {
    			return freak;
    		}
    	}
    	
    	return undefined;
    };
    
    this.replaceFreak = function (f) {
    	var list = this.getFreakList();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var freak = list[i];

    		if (!freak) {
    			continue;
    		}
    		//alert(freak.name + ' = ' + name);
    		if (freak.name == f.name) {
    			list[i] = f;
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
		ShowActivity(true, 'Saving freaks...');
    	
    	var data = Configuration.toJSON( this.freakList );
    	
    	jQuery.ajax( {
            url : 'freaks',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save freaks<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved freaks');
    		}
        });
    };
    
	// kickstart!
    this.load();
}



FreakController = new FreakController();