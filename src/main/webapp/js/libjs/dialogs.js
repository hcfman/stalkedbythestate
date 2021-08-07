/**
	Dialogs
*/

JSDialog 				= {_controller:null};
JSDialog.setController	= function (controller) {
	if (controller) {
		this._registerController(controller);
	}else{
		console.warn( 'Nothing to set' );
	}
};

/**
	Registers the controller for dialog events.
*/
JSDialog._registerController = function (controller) {
	// first remove controller 
	if (this._controller) {
		this._unregisterController(this._controller);
	}
	
	JSNotificationCenter.addObserver(controller, 'dialogDidLoad'		, 'DialogDidLoad');
	JSNotificationCenter.addObserver(controller, 'dialogDidShow'		, 'DialogDidShow');
	JSNotificationCenter.addObserver(controller, 'dialogShouldClose'	, 'DialogShouldClose');
	this._controller = controller;
};
/**
	Unregisters the controller for dialog events.
*/
JSDialog._unregisterController = function (controller) {
	JSNotificationCenter.removeObserver(controller, 'DialogDidLoad');
	JSNotificationCenter.removeObserver(controller, 'DialogDidShow');
	JSNotificationCenter.removeObserver(controller, 'DialogShouldClose');
};

JSDialog.openDialog = function (url, sender, subject, options) {

	var defaultOptions = {
		'buttonName': sender.name,
		'width'		: '50%',
		'position'	: ['center', 50],
		'modal'		: true
	};
	
	if (!options) {
		options = defaultOptions;
	}else{
		options = JSObject.merge( defaultOptions, options );
	}
	
	//JSObject.alertObject(options);
	
	var div = jQuery('<div></div>');
	
	jQuery('body').append(div);
	ShowActivity(true, 'Loading dialog: ' + sender.name);
	console.log('Sender name = ' + sender.name);
	console.log('subject = ' + subject);
	
	jQuery.ajax({
		url:url,
		cache:false,
		success: function (responseText) {
			ShowActivity(false);
		
			JSDialog._loadedData(responseText, div, sender, subject, options);
			console.log('Opened dialog \'' + sender.name + '\' (' + url + ')');
		}
	});
};

/**
	Processes the dialogs content and displays the dialog.
*/
JSDialog._loadedData = function (data, element, sender, subject, options) {
	var tmpl = new JSTemplate(data);
	if (!tmpl.isEmpty) {
		var name = sender.name.toClassified();
		
		JSPostNotification('DialogDidLoad', sender, {name:name, template:tmpl, subject:subject});
		
		var buttons = {};
		var buttonName = options.buttonName;
		
		// add the action button
		buttons[buttonName] = function () {
			var firstForm	= jQuery('form', element).first();
			var formData 	= FormHelper.getData( firstForm );
			var event		= 'DialogShould' + name;
			var result		= JSPostNotification(event, sender, formData);
			
			if (result === true) {
				jQuery(this).dialog('close').remove();
			}
		};
			
		buttons.Cancel = function () {
			var result = JSPostNotification('DialogShouldCancel', sender, {name:name, subject:subject});
			if (result !== false) {
				jQuery(this).dialog('close').remove();
			}
		};
		
		element.html( tmpl.render() );
		element.dialog({buttons:buttons, width:options.width, position:options.position, modal:options.modal});
		
		JSPostNotification('DialogDidShow', sender, {name:name, subject:subject});
	}else{
		element.remove();
	}
};