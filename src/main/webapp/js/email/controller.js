EmailController = function () {
    this.load = function () {
    	// respond to the 'LoadedJSON' message with 'initialise' method
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    };
    
    /**
    	Load all the JSON data into the controller.
    */
    this.initialise = function (note) {
    	var data = note.data;

		if ( SharedTemplates.hasTemplate('mail-provider') ) {
			var tmpl = SharedTemplates.getTemplate('mail-provider');
			tmpl.setObject(data);
			
			jQuery('#email').html( tmpl.render() );
		}
		
		this.applySettings(note.data);
        console.log('Initialised EmailController');
    };
    
    this.applySettings = function (settings) {
    	jQuery('#encType > option').each( function () {
    		if (this.value == settings.encType) {
    			this.selected = true;
    		}
    	});
    };
    
    this.save = function () {
    	ShowActivity(true, 'Saving email...');
    	
    	var form	= jQuery('#emailForm').first();
		var formData 	= FormHelper.getData( form );
		
		var validator = Validator.getValidator( 'Email' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		if ( !validator.check(formData) ) {
			return false;
		}
		
    	var data = Configuration.toJSON( formData );
    	
    	jQuery.ajax( {
            url : 'email',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save email<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved email');
    		}
        });
    };
	
	// kickstart!
    this.load();
};



EmailController = new EmailController();