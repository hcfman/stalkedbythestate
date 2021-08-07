ScheduleController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    }
    
    /**
    	Load all the JSON data into the controller.
    */
    this.initialise = function (note) {
    	this.schedules = note.data.schedules;
    	
    	this.registerFormNotificationListener();
    	
    	this.builder = new ScheduleBuilder();
    	this.builder.buildSchedules();
    	
    	JSDialog.setController(this);
    	
        console.log('Initialised ScheduleController');
    }
	
    this.registerFormNotificationListener = function () {
    	var a = new ScheduleFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldExitPage'	, 'ShouldExitPage');
    	
    	// add camera dialog events
    	n.addObserver(a, 'didLoadAddSchedule'		, 'DidLoadAddSchedule');
    	n.addObserver(a, 'didShowAddSchedule'		, 'DidShowAddSchedule');
    	n.addObserver(a, 'shouldCloseAddSchedule'	, 'ShouldCloseAddSchedule');
    	n.addObserver(a, 'shouldAddSchedule'		, 'DialogShouldAddSchedule');
    	
    	// edit camera dialog events
    	n.addObserver(a, 'didLoadEditSchedule'		, 'DidLoadEditSchedule');
    	n.addObserver(a, 'didShowEditSchedule'		, 'DidShowEditSchedule');
    	n.addObserver(a, 'shouldCloseEditSchedule'	, 'ShouldCloseEditSchedule');
    	n.addObserver(a, 'shouldEditSchedule'		, 'DialogShouldEditSchedule');
    	
    	n.addObserver(a, 'editSchedule', 'EditSchedule');
    	n.addObserver(a, 'removeSchedule', 'RemoveSchedule');
    };
    
	this.getSchedules = function () {
		return this.schedules;
	};
	
	this.getScheduleByName = function (name) {
    	var list = this.getSchedules();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var schedule = list[i];

    		if (!schedule) {
    			continue;
    		}
    	
    		if (schedule.name == name) {
    			return schedule;
    		}
    	}
    	
    	return undefined;
    };
    
    this.replaceSchedule = function (data) {
    	var list = this.getSchedules();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var schedule = list[i];

    		if (!schedule) {
    			continue;
    		}
    	
    		if (schedule.name == data.name) {
    			this.schedules[i] = data;
    			break;
    		}
    	}
    };
    
    this.removeSchedule = function (data) {
    	var list = this.getSchedules();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var schedule = list[i];

    		if (!schedule) {
    			continue;
    		}
    	
    		if (schedule.name == data.name) {
    			this.schedules.remove(i);
    			break;
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
	
	this.save = function () {
		ShowActivity(true, 'Saving schedules...');
    	
    	var data = Configuration.toJSON( this.schedules );
    	
    	jQuery.ajax( {
            url : 'schedules',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save schedules<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved schedules');
    		}
        });
    };
    
	// kickstart!
    this.load();
}



ScheduleController = new ScheduleController();