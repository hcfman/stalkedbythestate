SystemSettingsHelper = function() {

};

SystemSettingsHelper = new SystemSettingsHelper();

CheckUpdates = function() {
	this.name = "Check Updates";

	this.dialogDidShow = function(note) {
		jQuery("#version").html(note.sender.checkUpdatesJSON.version);
		var tmpl = new JSTemplate(
				'%{foreach, var:description, repeatString:<li>%value</li>}');
		tmpl.set("description", note.sender.checkUpdatesJSON.description);
		jQuery("#updateDescription").append(tmpl.render());
	};

	this.setCheckUpdatesJSON = function(checkUpdatesJSON) {
		this.checkUpdatesJSON = checkUpdatesJSON;
		return this.checkUpdatesJSON;
	};

	this.downloadAndInstall = function(message, seconds, url) {
		jQuery(
				'<div id="_downloadProgress"><p>' + message
						+ '</p><div id="downloadProgress"></div><br></div>')
				.dialog({
					modal : true,
					resizable : false,
					dialogClass : 'rebootDialog'
				}).find('#downloadProgress').progressbar({
					value : 0
				});

		if (seconds > 0) {
			var now = new Date().getTime() / 1000;
			var end = now + seconds;

			window._rebootInterval = window.setInterval(
					function() {
						var tNow = new Date().getTime() / 1000;
						var cur = end - tNow;
						var val = 100 - (100 / seconds) * cur;

						jQuery('#downloadProgress').progressbar('option',
								'value', val);

						if (val > 99) {
							if (url == undefined) {
								location.reload();
							} else {
								location.assign(url);
							}
						}
					}, 1000);
		}
	};

	this.downloadUpdates = function() {
		if (confirm('Download and installed update\nAre you sure?')) {
			this.downloadAndInstall(' ', 0);

			jQuery('#downloadProgress').prepend(
					'<span id="progressValue">0%</span>');
			jQuery.get('downloadupdates', this._progressRequest);
		}
	};

	this._progressRequest = function() {

		jQuery
				.ajax({

					timeout : 30000,
					cache : false,
					url : 'dprogress',
					type : 'GET',

					contentType : 'application/json',
					dataType : 'json',

					error : function(xhr, status, message) {
						if (status == 'timeout') {
							CheckUpdates._progressRequest();
						}
					},

					success : function(response) {
						if (response != null) {
							var value = response.progress;
							var label = response.message;
							var cont = response.cont;

							jQuery('#downloadProgress').progressbar('option',
									'value', value);
							jQuery('#downloadProgress').prev().text(label);
							jQuery('#progressValue').text(
									(parseInt(value) || 0) + '%');

							if (cont === true) {
								CheckUpdates._progressRequest();
							} else if (value < 100) {
								jQuery('#_downloadProgress').remove();
								alert(label
										+ '\n\nSee "System Log" for details');
								// location.assign('systemlog');
							} else { // DONE!
								jQuery('#_downloadProgress').remove();
								CheckUpdates.rebootSystem();

								// Now reboot
							}
						} else {
							CheckUpdates._progressRequest();
						}
					}
				});
	};

	this.shouldUpdate = function(note) {
		this.downloadUpdates();
	};

	this.rebootSystem = function() {
		jQuery.ajax({
			cache : false,
			url : 'reboot',
			type : 'GET',

			contentType : 'application/json',
			dataType : 'json'
		});

		Reboot('Rebooting the system', 120, '.');
	};

	JSNotificationCenter.addObserver(this, 'shouldUpdate',
			'DialogShouldCheckUpdates');
};

CheckUpdates = new CheckUpdates();
