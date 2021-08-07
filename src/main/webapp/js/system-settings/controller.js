SystemSettingsController = function () {
    this.load = function () {
    	JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
    	JSNotificationCenter.addObserver(this, 'checkUpdates', 'CheckUpdates');
    };
    
    this.initialise = function (note) {
		this.applySettings(note.data);
    };
    
    this.checkUpdates = function() {
    	ShowActivity(true, 'Check for updates...');
    	jQuery.ajax( {
            url : 'checkupdates',
            type : 'POST',
            data : {'timestamp': new Date().getTime()},
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
//            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
//            	JSPostNotification('OnError', 'Failed to save actions<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true && response.description) {
            		CheckUpdates.setCheckUpdatesJSON(response);
            		JSDialog.setController(CheckUpdates);
					                	JSDialog.openDialog(
							'jsp/content/forms/updates-available.html',
							CheckUpdates, null, {
								buttonName : 'Update',
								width : '300px'
							});
            	}else{
            		JSPostNotification('OnError', response.messages.join('<br>') );
            	}

    			ShowActivity(false);
//    			JSPostNotification('OnInfo', 'Saved actions');
    			// TODO: do stuff with response...
    		}
        });

    	
    };
    
    this.applySettings = function (settings) {
    	// Timezone
    	jQuery('#timeZone > option').each( function () {
    		if (this.value == settings.dateTime.timeZone) {
    			this.selected = true;
    		}
    	});
    	
    	// Date & Time
    	jQuery('#manualDate').val(settings.dateTime.date);
    	jQuery('#timeHour > option').each( function () {
    		if (this.value == settings.dateTime.timeHour) {
    			this.selected = true;
    		}
    	});
    	
    	jQuery('#timeMinute > option').each( function () {
    		if (this.value == settings.dateTime.timeMinute) {
    			this.selected = true;
    		}
    	});
    	
    	// NTP
    	if (settings.dateTime.useNtp) {
    		jQuery('#manualTime').attr('checked', false);
    		jQuery('#dynamicTime').attr('checked', true);
    	}else{
    		jQuery('#manualTime').attr('checked', true);
    		jQuery('#dynamicTime').attr('checked', false);
    	}
    	
    	jQuery('#ntpServer').val(settings.dateTime.ntpServer);
    	
    	// Network
    	jQuery('#dhcp').attr('checked', settings.network.dhcp);
    	
    	jQuery('#hostname').val(settings.network.hostname);
    	
    	jQuery('#address').val(settings.network.address);
    	jQuery('#mask').val(settings.network.mask);
    	jQuery('#defaultRoute').val(settings.network.defaultRoute);
    	
    	// Nameservers
    	jQuery('#nameServer1').val(settings.network.nameServer1);
    	jQuery('#nameServer2').val(settings.network.nameServer2);
    	jQuery('#nameServer3').val(settings.network.nameServer3);
    	
    	// Protocol Descriptor
    	jQuery('#protocolDescriptor > option').each( function () {
    		if (this.value == settings.network.protocolDescriptor) {
    			this.selected = true;
    		}
    	});
    	
    	jQuery('#httpPort').val(settings.network.httpPort);
    	jQuery('#httpsPort').val(settings.network.httpsPort);
    	
    	// Preferences
    	var prefs = settings.preferences;

    	console.log(prefs);

    	jQuery('#webPrefix').val(prefs.webPrefix);
		jQuery('#x10Delay').val(prefs.x10Delay);
		jQuery('#connectTimeout').val(prefs.connectTimeout);
		jQuery('#freeSpace').val(prefs.freeSpace);
		jQuery('#daysJpeg').val(prefs.daysJpeg);
		jQuery('#cleanRate').val(prefs.cleanRate);

		jQuery('#phonehomeUrl').val(prefs.phonehomeUrl);
		
    };
    
    
    this.saveDateTimeSettings = function () {
    	ShowActivity(true, 'Saving Date &amp; Time...');
    	
    	var settings = {
    			timeZone	:jQuery('#timeZone').val(),
    			date		:jQuery('#manualDate').val(),
    			timeHour	:jQuery('#timeHour').val(),
    			timeMinute	:jQuery('#timeMinute').val(),
    			useNtp		:jQuery('#dynamicTime').attr('checked'),
    			ntpServer	:jQuery('#ntpServer').val()
    	};
    	var data = Configuration.toJSON( settings );
    	
    	jQuery.ajax( {
            url : 'datetime',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save date &amp; time<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			
    			if (response.result === true) {
    				Reboot('Restarting the system', 30);
    				JSPostNotification('OnInfo', 'Saved date &amp; time');
    			}else{
    				JSPostNotification('OnError', response.messages.join('<br>') );
    			}
    		}
        });
    };
    
    this.saveNetworkSettings = function () {
    	ShowActivity(true, 'Saving network settings...');
    	
    	var settings = {
    			dhcp				:jQuery('#dhcp').attr('checked'),
    			address				:jQuery('#address').val(),
    			mask				:jQuery('#mask').val(),
    			defaultRoute		:jQuery('#defaultRoute').val(),
    			nameServer1			:jQuery('#nameServer1').val(),
    			nameServer2			:jQuery('#nameServer2').val(),
    			nameServer3			:jQuery('#nameServer3').val(),
    			protocolDescriptor	:jQuery('#protocolDescriptor').val(),
    			httpPort			:jQuery('#httpPort').val(),
    			httpsPort			:jQuery('#httpsPort').val(),
    			hostname			:jQuery('#hostname').val()
    	};
    	var data = Configuration.toJSON( settings );
    	
    	jQuery.ajax( {
            url : 'network',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save network settings<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			
    			if (response.result === true) {
    				Reboot('Restarting the system', 30);
    				JSPostNotification('OnInfo', 'Saved network settings');
    			}else{
    				JSPostNotification('OnError', response.messages.join('<br>') );
    			}
    		}
        });
    };
    
    
    this.savePreferences = function () {
    	ShowActivity(true, 'Saving preferences...');
    	
    	var settings = {
    			webPrefix		:jQuery('#webPrefix').val(),
    			x10Delay		:jQuery('#x10Delay').val(),
    			connectTimeout	:jQuery('#connectTimeout').val(),
    			freeSpace		:jQuery('#freeSpace').val(),
    			daysJpeg		:jQuery('#daysJpeg').val(),
    			cleanRate		:jQuery('#cleanRate').val(),
    			phonehomeUrl	:jQuery('#phonehomeUrl').val()
    	};
    	
    	var data = Configuration.toJSON( settings );
    	
    	jQuery.ajax( {
            url : 'preferences',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save preferences<br>Reason: ' + reason );
            },
            success: function (response) {
    			ShowActivity(false);
    			JSPostNotification('OnInfo', 'Saved preferences');
    		}
        });
    };
    
    this.load();
};


// singleton
SystemSettingsController = new SystemSettingsController();