ConfigLoadJSON = function (file) {
	ShowActivity(true, 'Loading configuration...');
    
    jQuery.get( file, function( data ) {
    	ShowActivity(false);
    	
    	// check for errors
        if (!data.result) {
			if (data.messages) {
				var messages 	= data.messages;
				var text 		= 'Messages:\n';
				
			    for (var i = 0, c = messages.length; i < c; i++) {
			        text += '\n* ' + messages[i];
			    }
			    JSPostNotification('OnError', text);
			}
			
			JSPostNotification('OnError', 'JSON data returned with errors');
			return;
		}
		
        // notify observers that the JSON has been loaded
        var note = new JSNotification('LoadedJSON', this, data);
        JSNotificationCenter.postNotification( note );
    });
};


DayOfWeek = {
	Monday		: 2,
	Tuesday		: 3,
	Wednesday	: 4,
	Thursday	: 5,
	Friday		: 6,
	Saterday	: 7,
	Sunday		: 1,
	
	dayFromString: function (dayString) {
		for (var name in this) {
			if (name == dayString) {
				return this[name];
			}
		}
		return undefined;
	},
	
	stringFromDay: function (dayInteger) {
		for (var name in this) {
			if (this[name] == dayInteger) {
				return name;
			}
		}
		return undefined;
	}
};


ActionType = {};
ActionType.VIDEO 		= 'VIDEO';
ActionType.REMOTE_VIDEO = 'REMOTE VIDEO';
ActionType.EMAIL 		= 'EMAIL'; 
ActionType.X10 			= 'X10';
ActionType.SEND_HTTP 	= 'SEND HTTP';
ActionType.ADD_TAG 		= 'ADD TAG';
ActionType.DELETE_TAG 	= 'DELETE TAG';
ActionType.CANCEL_ACTION = 'CANCEL ACTION';
ActionType.WEB_PREFIX 	= 'WEB PREFIX';
ActionType.PHIDGET_OUTPUT 	= 'PHIDGET OUTPUT';
ActionType.RFXCOM = 'RFXCOM';


AbstractAction = function (aEventName, aName, aDescription) {
	this.actionType 	= ActionType.EMAIL;
	this.eventName 		= aEventName || '';
	this.name			= 'unnamed';
	this.description 	= aDescription || '';
	this.validTimes		= [];
	this.delay			= 0;
	this.delayUnits		= "sec";
	this.hysteresisUnits= "sec";
	this.profiles		= [];
	this.positiveTagNames = [];
	this.negativeTagNames = [];
	this.guest			= false;
	this.hysteresis		= 0;
};


VideoAction = function (aEventName, aDescription) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType = ActionType.VIDEO;
	this.cameraSet 	= [];
};

RemoteVideoAction = function (aEventName, aDescription) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType = ActionType.REMOTE_VIDEO;
	this.cameraSet 	= [];
	this.freakName	= '';
};

EmailAction = function (aEventName, aDescription, aEmail) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType = ActionType.EMAIL;
	this.cameraSet 	= [];
	this.to			= aEmail;
	this.responseGroup = '';
	this.videoType	= 'MJPEG';
};


AddTagAction = function (aEventName, aDescription) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType 	= ActionType.ADD_TAG;
	this.tagName		= '';
	this.validFor		= '0';
};

DeleteTagAction = function () {
	AbstractAction.call();
	this.actionType 	= ActionType.DELETE_TAG;
	this.tagName		= '';
};


X10Action = function (aEventName, aDescription, aHouseCode, aUnitCode) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType 	= ActionType.X10;
	this.houseCode		= aHouseCode;
	this.unitCode		= aUnitCode;
	this.direction		= 'On';
	this.duration		= 0;
};


HttpAction = function (aEventName, aDescription) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType 	= ActionType.SEND_HTTP;
	this.url			= 'http://www.example.com';
	this.methodType		= 'GET';
	this.username		= '';
	this.password		= '';
	this.parameters		= [];
};

CancelAction = function (aEventName, aDescription) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType 	= ActionType.CANCEL_ACTION;
	this.parameters		= [];
	this.actionName		= '';
};

WebPrefix = function (aEventName, aDescription) {
	AbstractAction.call(this, aEventName, aDescription);
	this.actionType 	= ActionType.WEB_PREFIX;
	this.parameters		= [];
	this.prefix			= '';
};

PhidgetOutputAction = function () {
	AbstractAction.call(this);
	this.actionType 	= ActionType.PHIDGET_OUTPUT;
	this.phidgetName = '';
	this.phidgetActionType = '';
	this.port = 0;
	this.pulseTrain = '';
};

RfxcomAction = function() {
	AbstractAction.call(this);
	this.actionType = ActionType.RFXCOM;
	this.rfxcomCommand = '';
};

TimeSpec = function () {
	this.times 	= [];
	this.dows 	= [];
};


TimeRange = function () {
	this.startHour 	= 0;
	this.startMin	= 0;
	this.startSec	= 0;
	this.endHour	= 0;
	this.endMin		= 0;
	this.endSec		= 0;
};


Configuration = {
	version		: 1.0,
	actionList	: [],
	toJSON		: function (someData) {
		var json = '';
		var data = someData;
		
		try {
			if (JSON.stringify) {
				try {
					json = JSON.stringify(data);
				} catch (e) {
					console.error( 'Failed to convert config to JSON\n' + e );
				}
			}else if (JSON.parse) {
				try {
					json = JSON.parse(data);
				} catch (e) {
					console.error( 'Failed to convert config to JSON\n' + e );
				}
			}
		} catch (e) {
			
			alert('Cannot use JSON in this browser');
			
		}
			
		return json;
	}
};


ShowActivity = function ( isLoading, text ) {
	text = text || 'Loading...';
	
	var e = jQuery('#loading');
	
	if (isLoading) {
		e.html(text);
		e.css('display', 'block');
	}else{
		window.setTimeout( function () {
			e.fadeOut(1000);
		}, 500);
	}
};

Reboot = function (message, seconds, url) {
	jQuery('<div id="_rebootProgress"><p>' + message + '</p><div id="rebootProgress"></div><br></div>').dialog({
		modal:true,
		resizable:false,
		dialogClass:'rebootDialog'
	}).find('#rebootProgress').progressbar({value:0});
	
	if (seconds > 0) {
		var now = new Date().getTime() / 1000;
		var end = now + seconds;
		
		window._rebootInterval = window.setInterval(function () {
			var tNow = new Date().getTime() / 1000;
			var cur = end  - tNow;
			var val = 100 - (100 / seconds) * cur;
				
			jQuery('#rebootProgress').progressbar('option', 'value', val);
			
			if (val > 99) {
				if (url == undefined) {
					location.reload();
				}else{
					location.assign(url);
				}
			}
		}, 1000);
	}
};

Shutdown = function (message, seconds, url) {
	jQuery('<div id="_rebootProgress"><p>' + message + '</p><div id="rebootProgress"></div><br></div>').dialog({
		modal:true,
		resizable:false,
		dialogClass:'rebootDialog'
	}).find('#rebootProgress').progressbar({value:0});
	
	if (seconds > 0) {
		var now = new Date().getTime() / 1000;
		var end = now + seconds;
		
		window._rebootInterval = window.setInterval(function () {
			var tNow = new Date().getTime() / 1000;
			var cur = end  - tNow;
			var val = 100 - (100 / seconds) * cur;
				
			jQuery('#rebootProgress').progressbar('option', 'value', val);
			
			if (val > 99) {
				if (url == undefined) {
					location.reload();
				}else{
					location.assign(url);
				}
			}
		}, 1000);
	}
};
