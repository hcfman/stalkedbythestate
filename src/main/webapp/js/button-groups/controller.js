ButtonGroupController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    /**
    	Load all the JSON data into the controller.
    */
    this.initialise = function (note) {
    	var data = note.data;
		
		this.buttonGroupList		= data.buttonGroups;
    	this.builder		= new ButtonGroupBuilder();
    	
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildButtonGroupList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised ButtonGroupController');
    };
    
    /**
    	Registers a listener for messages we are interested in.
    	TODO: get rid of unused messages and observers.
    */
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new ButtonGroupFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	
    	// add action dialog events
    	n.addObserver(a, 'didLoadAddButtonGroup'			, 'DidLoadAddActionButtonGroup');
    	n.addObserver(a, 'didShowAddButtonGroup'			, 'DidShowAddButtonGroup');
    	n.addObserver(a, 'shouldCloseAddButtonGroup'		, 'ShouldCloseAddButtonGroup');
    	n.addObserver(a, 'shouldAddButtonGroup'			, 'DialogShouldAddButtonGroup');
    	
    	n.addObserver(a, 'shouldAddButton'			, 'DialogShouldAddButton');
    	
    	n.addObserver(a, 'removeButtonGroup', 'RemoveButtonGroup');
    	n.addObserver(a, 'didLoadEditButton', 'DidLoadEditButton');
    	n.addObserver(a, 'didShowEditButton', 'DidShowEditButton');
    	n.addObserver(a, 'shouldEditButton', 'DialogShouldEditButton');
    };
    
    this.getButtonGroupList = function () {
    	return this.buttonGroupList;
    };
    
    this.getButtonInGroup = function (groupName, buttonName) {
    	var list = this.getButtonGroupList();
    	var group = list[groupName];
    	
    	for (var i = 0, c = group.length; i < c; i++) {
    		var item = group[i];
    		if (!item) {
    			continue;
    		}
    		if (item.eventName == buttonName) {
    			return item;
    		}
    	}
    	return undefined;
    };
    
    this.removeButtonByName = function (groupName, buttonName) {
    	var list = this.getButtonGroupList();
    	var group = list[groupName];
    	
    	for (var i = 0, c = group.length; i < c; i++) {
    		var item = group[i];
    		if (!item) {
    			continue;
    		}
    		if (item.eventName == buttonName) {
    			group.remove(i);
    		}
    	}
    };
    
    this.removeButtonGroupByName = function (groupName) {
    	var list = this.getButtonGroupList();
    	delete list[groupName];
    };
    
    this.replaceButtonInGroup = function (groupName, button) {
    	var list = this.getButtonGroupList();
    	var group = list[groupName];
    	
    	for (var i = 0, c = group.length; i < c; i++) {
    		var item = group[i];
    		if (!item) {
    			continue;
    		}
    		if (item.eventName == button.eventName) {
    			group[i] = button;
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
    };
    
    this.dialogDidShow = function ( note ) {
       	var event = 'DidShow' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    };
    
    this.dialogShouldCancel = function ( note ) {
    	var event = 'ShouldCancel' + note.data.name;
    	return JSPostNotification(event, note.sender, note.data);
    };
	
    /*
     * Save actions
     */
    this.save = function () {
    	ShowActivity(true, 'Saving Button Groups...');
    	
    	var data = Configuration.toJSON( this.getButtonGroupList() );
    	
    	jQuery.ajax( {
            url : 'buttongroups',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save button groups<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved button groups');
    			// TODO: do stuff with response...
    		}
        });
    };
    
	// kickstart!
    this.load();
};



ButtonGroupController = new ButtonGroupController();