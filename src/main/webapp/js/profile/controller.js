ProfileController = function () {
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
    	
		this.profileList	= data.profilesJSON;
    	this.builder		= new ProfileBuilder();
    	
    	// register events we want to listen to
    	this.registerFormNotificationListener();
		this.builder.buildProfileList();
		
		// tell dialogs that this controller wants to listens to it's messages
		JSDialog.setController(this);
		
        console.log('Initialised ProfileController');
    }
    
    this.registerFormNotificationListener = function () {
    	var a = new ProfileFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	n.addObserver(a, 'didLoadAddProfile'		, 'DidLoadAddProfile');
    	n.addObserver(a, 'didShowAddProfile'		, 'DidShowAddProfile');
    	n.addObserver(a, 'shouldCloseAddProfile'	, 'ShouldCloseAddProfile');
    	n.addObserver(a, 'shouldAddProfile'			, 'DialogShouldAddProfile');
    	
    	n.addObserver(a, 'didLoadEditProfile'		, 'DidLoadEditProfile');
    	n.addObserver(a, 'didShowEditProfile'		, 'DidShowEditProfile');
    	n.addObserver(a, 'shouldCloseEditProfile'	, 'ShouldCloseEditProfile');
    	n.addObserver(a, 'shouldEditProfile'		, 'DialogShouldEditProfile');
    	
    	n.addObserver(a, 'editProfile', 'EditProfile');
    	n.addObserver(a, 'removeProfile', 'RemoveProfile');
    	
    	// add time dialog events
    	n.addObserver(a, 'didLoadAddTime'			, 'DidLoadAddTime');
    	n.addObserver(a, 'shouldCloseAddTime'		, 'ShouldCloseAddTime');
    	n.addObserver(a, 'shouldAddTime'			, 'DialogShouldAddTime');
    	
    	// add timerange dialog events
    	n.addObserver(a, 'didLoadAddTimeRange'		, 'DidLoadAddTimeRange');
    	n.addObserver(a, 'shouldCloseAddTimeRange'	, 'ShouldCloseAddTimeRange');
    	n.addObserver(a, 'shouldAddTimeRange'		, 'DialogShouldAddTimeRange');
    	
    	n.addObserver(this, 'dialogShouldCancel', 'DialogShouldCancel');
    	n.addObserver(a, 'shouldCancelAddProfile', 'ShouldCancelAddProfile');
    	n.addObserver(a, 'shouldCancelAddTime', 'ShouldCancelAddTime');
    	
    	n.addObserver(a, 'shouldCancelEditProfile', 'ShouldCancelEditProfile');
    };
    
    this.getProfileList = function () {
    	return this.profileList;
    }
    
    this.getProfileByName = function (name) {
    	var list = this.profileList;
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var profile = list[i];

    		if (!profile) {
    			continue;
    		}
    		
    		if (profile.name == name) {
    			return profile;
    		}
    	}
    	
    	return undefined;
    }
    
    this.replaceProfile = function (prof) {
    	var list = this.profileList;
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var profile = list[i];

    		if (!profile) {
    			continue;
    		}
    		
    		if (profile.name == prof.name) {
    			list[i] = prof;
    		}
    	}
    	
    	return undefined;
    }
    
   this.removeProfileByName = function (name) {
    	var list = this.getProfileList();
    	var subjects = list.find( function (key, value) { return value.name == name; } );
		list.remove( subjects.first().key );
    }
    
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
    	ShowActivity(true, 'Saving profiles...');
    	
    	var data = Configuration.toJSON( this.profileList );
    	
    	jQuery.ajax( {
            url : 'profiles',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save profiles<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved profiles');
    			// TODO: do stuff with response...
    		}
        });
    };
    
	// kickstart!
    this.load();
}



ProfileController = new ProfileController();