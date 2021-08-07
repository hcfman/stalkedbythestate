MaintenanceController = function () {
	this.pollInterval = null;
	
	this.formatDisk = function () {
		if ( confirm('You will lose all data on the disk.\nAre you sure?') ) {
			Reboot(' ', 0);
			
			jQuery('#rebootProgress').prepend( '<span id="progressValue">0%</span>' );
			jQuery.get('format', this._progressRequest);
		}
	};
	
	this._progressRequest = function () {
		
		jQuery.ajax( {
			
			timeout	: 30000,
			cache	: false,
            url		: 'fprogress',
            type	: 'GET',
            
            contentType: 'application/json',
            dataType: 'json', 
            
            error: function (xhr, status, message) {
            	if (status == 'timeout') {
            		MaintenanceController._progressRequest();
            	}
            },
            
            success: function (response) {
    			if (response != null) {
    				//TODO: Update stuff
    				var value 	= response.progress;
    				var label 	= response.message;
    				var cont	= response.cont;
    				
    				jQuery('#rebootProgress').progressbar('option', 'value', value);
    				jQuery('#rebootProgress').prev().text( label );
    				jQuery('#progressValue').text( (parseInt(value) || 0) + '%' );
    				
    				if (cont === true) {
    					MaintenanceController._progressRequest();
    				}else if (value < 100){
    					jQuery('#_rebootProgress').remove();
    					alert(label + '\n\nSee "Format Log" for details');
    					location.assign('formatlog');
    				}else{ // DONE!
    					jQuery('#_rebootProgress').remove();
    					alert('Formatting was successful!\n\nSee "Format Log" for details.');
    					location.assign('formatlog');
    				}
    			}else{
    				MaintenanceController._progressRequest();
    			}
    		}
        });
	};
	
	this.rebootSystem = function () {
		if ( confirm('Are you sure you want to reboot the system?') ) {
			
			jQuery.ajax( {
				cache	: false,
	            url		: 'reboot',
	            type	: 'GET',
	            
	            contentType: 'application/json',
	            dataType: 'json'
	        });
			
			Reboot('Rebooting the system', 120, '.');
		}
	};
	
	this.shutdownSystem = function () {
		if ( confirm('Are you sure you want to shutdown the system?') ) {
			
			jQuery.ajax( {
				cache	: false,
	            url		: 'shutdown',
	            type	: 'GET',
	            
	            contentType: 'application/json',
	            dataType: 'json'
	        });
			
			Reboot('Shutting down the system', 30, '.');
		}
	};
};

MaintenanceController = new MaintenanceController();