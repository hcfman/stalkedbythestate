CameraHelper = function () {
	
	this.validate = function (camera) {
		var validator = Validator.getValidator( 'Camera' );
		
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(camera);
	};
	/*
	this.validate = function (camera, options) {
		
		if (camera.name == '') {
			JSPostNotification('OnError', 'Camera name is empty');
			return false;
		}
		
		if (!options || !options.skipUniqueName) {
			if ( CameraController.getCameraByName(camera.name) != undefined) {
				JSPostNotification('OnError', 'Camera name not unique: ' + camera.name);
				return false;
			}
		}
		
		if (camera.description == '') {
			JSPostNotification('OnError', 'Camera must have a description');
			return false;
		}
		
		if ( !camera.url.match(/^(http|https):\/\/.+$/) ) {
			JSPostNotification('OnError', 'Camera url must be a valid url.<br><br>Like:<br>http://www.example.com');
			return false;
		}
		
		return true;
	};*/
};

CameraHelper = new CameraHelper();