/**
	Simple Notifications
	
	Creating the possibility to send messages to observers.
*/


JSNotification = function (name, sender, data) {
	this.name	= name;
	this.sender = sender 	|| window;
	this.data	= data		|| {};
}

JSNotificationCenter = function () {
	this._observers = {};
	
	this.addObserver = function (obj, callback, eventName) {
		if (!obj || !callback || !eventName) {
			console.warn('Object not added as observer');
			return;
		}

		if (!this._observers[eventName]) {
			this._observers[eventName] = [];
		}
		
		this._observers[eventName].push( {object:obj, callback:callback} );
	}
	
	this.removeObserver = function (obj, eventName) {
		if (!obj || !eventName) {
			console.warn('Object not removed from observers');
			return;
		}
		
		var observers = this._observers[eventName];
		if (!observers) {
			return;
		}
		
		for (var i = 0, c = observers.length; i < c; i++) {
			var observer = observers[i];
			if (observer.object === obj) {
				observers.remove(i);
				break;
			}
		}
	}
	
	this.postNotification = function (notification) {
		if (!notification || !notification.name) {
			console.warn('Notification not posted to observers');
			return;
		}
		
		var event 		= notification.name;
		var observers	= this._observers[event];
		var returnValue	= true;
		
		console.log('Calling observers for ' + event);
		
		if (observers && observers.length > 0) {
			for (var i = 0, c = observers.length; i < c; i++) {
				var observer 	= observers[i];
				var method		= observer.object[observer.callback];
				if (method) {
					var result = method.call(observer.object, notification);
					// break the loop when a observers method returns FALSE
					if (result === false) {
						returnValue = false;
						break;
					}
				}else{
					console.warn( 'Skipped notification\nMethod ' + observer.callback + ' does not exists on observer' );
				}
			}
		}
		
		return returnValue;
	}
}

// singleton :-)
JSNotificationCenter = new JSNotificationCenter();

// shorthand function to post a notification
JSPostNotification = function (event, sender, data) {
	var note = new JSNotification(event, sender, data);
	return JSNotificationCenter.postNotification( note );
}