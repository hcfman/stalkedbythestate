SystemStatusBuilder = function () {
	
	this.buildDiskStatus = function (up, message) {
		var className = up ? 'yes' : 'no';
		var button = up ? 'Up' : 'Down';
		var html = '<h2>Disk</h2>\
			<div class="row disk">\
			<span class="status ' + className + '">' + button+ '</span>\
			<span class="text">' + message + '</span>\
			<div class="clear"></div>\
			</div>';
		jQuery('#system-status').append(html);
	};
	
	this.buildRfxcomStatus = function (status) {
		var className = status ? 'yes' : 'no';
		var message = status ? 'Up' : 'Down';
		var html = '<h2>RFXCOM</h2>\
			<div class="row rfxcom">\
			<span class="status ' + className + '">' + message+ '</span>\
			<span class="text">RFXCOM Controller</span>\
			<div class="clear"></div>\
			</div>';
		jQuery('#system-status').append(html);
	};
	
	this.buildPhidgetStatus = function (status) {
		var html = '<h2>Phidgets</h2>';
		
		for (var serial in status) {
			var phidgetStatus = {
				serial:serial,
				status:status[serial]
			};
			html += this.buildPhidgetRow(phidgetStatus);
		};
		
		jQuery('#system-status').append(html);
	};
	
	this.buildPhidgetRow = function (status) {
		var className = status.status ? 'yes' : 'no';
		var message = status.status ? 'Up' : 'Down';
		var html = '<div class="row phidget">\
			<span class="status ' + className + '">' + message+ '</span>\
			<span class="text">Serial: ' + status.serial + '</span>\
			<div class="clear"></div>\
			</div>';
		return html;
	};
	
	this.buildCameraStatus = function (status) {
		var html = '<h2>Cameras</h2>';
		
		for (var i = 0, c = status.length; i < c; i++) {
			var cameraStatus = status[i];
			html += this.buildCameraRow(cameraStatus);
		};
		
		jQuery('#system-status').append(html);
	};
	
	this.buildCameraRow = function (status) {
		var className = status.up ? 'yes' : 'no';
		var message = status.up ? 'Up' : 'Down';
		
		var html = '<div class="row camera">\
			<span class="status ' + className + '">' + message + '</span>\
			<span class="text"><span class="index">#' + status.index + '</span>' + status.name + '</span>\
			<div class="clear"></div>\
			</div>';
		return html;
	};
};