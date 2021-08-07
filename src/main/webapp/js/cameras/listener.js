/*
 * Main event listener for ActionController
 * TODO: get rid of unused methods
 */
CameraFormListener = function () {
	
	this.protocolChanged = function (select) {
		if (select.value == 'HTTPS') {
			jQuery('.VerifyHostnameAttribute').show();
		}else{
			jQuery('.VerifyHostnameAttribute').hide();
			jQuery('.VerifyHostnameAttribute input').attr('checked', false);
		}
	};
	
	this.shouldAddCamera = function (note) {
		var camera = note.data;
		camera.cachingAllowed = camera.cachingAllowed == 'true' ? true : false;
		camera.guest = camera.guest == 'true' ? true : false;
		camera.index = CameraController.getFirstAvailableIndex();
		
		if ( CameraHelper.validate(camera) ) {
			CameraController.cameraList.push(camera);
			CameraController.builder.buildCameraList();
		}else{
			return false;
		}
	};
	
	this.removeCamera = function (note) {
		CameraController.removeCameraByName( note.data.name );
		CameraController.builder.buildCameraList();
	};
	
	/*
	 * Because editCamera is called from a template using the JSPostNotification
	 * we must setup the dialog ourself. This gives us a chance to provide the camera data. 
	 */
	this.editCamera = function (note) {
		var index = note.data.index;
		var camera = CameraController.getCameraByIndex(index);
		JSDialog.openDialog('jsp/content/forms/camera-new.html', note.sender, camera, {buttonName:'Save'});
	};
	
	/*
	 * The dialog for editing the camera is loaded, so we can put data in place.
	 */
	this.didLoadEditCamera = function (note) {
		var camera = note.data.subject;
		var template = note.data.template;
		
		template.setObject(camera);
	};
	/*
	 * On dialog display all templates are rendered.
	 * This is the ideal time to set some more complex values.
	 */
	this.didShowEditCamera = function (note) {
		var camera = note.data.subject;
		
		jQuery('#cameraProtocol > option').each( function () {
			if ( camera.protocol && camera.protocol == this.value ) {
				this.selected = true;
				CameraController.listener.protocolChanged( document.getElementById('cameraProtocol') );
				
				jQuery('.VerifyHostnameAttribute input').attr('checked', !!camera.verifyHostname);
			}
		});
		
		jQuery('#cameraCachingAllowed > option').each( function () {
			if ( camera.cachingAllowed && this.value == 'true' ) {
				this.selected = true;
			}
		});
		
		jQuery('#cameraGuest > option').each( function () {
			if ( camera.guest && this.value == 'true' ) {
				this.selected = true;
			}
		});
	};
	
	this.shouldEditCamera = function (note) {
		var data = note.data;
		var options = {skipUniqueName:true};
		
		// validate the data provided by the dialogs form
		if ( CameraHelper.validate(data, options) ) {
			
			// convert string to boolean value
			data.cachingAllowed = data.cachingAllowed == 'true';
			data.guest 			= data.guest == 'true';
			
			CameraController.replaceCamera(data);
			CameraController.builder.buildCameraList();
		}else{
			// return false to prevent closing the dialog
			return false;
		}
	};
};
