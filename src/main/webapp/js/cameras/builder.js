CameraBuilder = function () {
	this.buildCameraList = function () {
		var list = '';
		var tmpl;
		var cameras = CameraController.getCameraList();
		
		// render the cameras
		if ( cameras.length > 0 ) {
			tmpl = SharedTemplates.getTemplate('camera-list-row');
				
			for (var i = 0, c = cameras.length; i < c; i++) {
				var camera = cameras[i];
				
				if (!camera) {
					console.warn( 'Camera not defined but in cameraList' );
					continue;
				}
				
				tmpl.set('isCacheAllowed', camera.cachingAllowed ? 'check.png' : 'false.png');
				tmpl.set('guestImage', camera.guest ? 'check.png' : 'false.png');
				tmpl.setObject(camera);
				
				list += tmpl.render();
				
				tmpl.reset();
			}
		// show a message for empty list
		}else{
			tmpl = SharedTemplates.getTemplate('camera-list-empty');
			list += tmpl.render();
		}
		
		jQuery('#cameraList').html(list);
	};
};