/*
 * Main event listener for ActionController
 * TODO: get rid of unused methods
 */
PhidgetFormListener = function () {
	
	this.didLoadAddPhidget = function (note) {
		
		var tmpl = note.data.template;
		tmpl.set('ports', PhidgetController.builder.buildPhidgetInputPorts(
				PhidgetController.initialPorts, PhidgetController.initialPorts, [], []));
	};
	
	this.shouldAddPhidget = function (note) {
		
		var phidgetData = note.data;
		
		if (PhidgetHelper.validate(phidgetData)) {
			var phidget = PhidgetHelper.makePhidget(phidgetData);
			PhidgetController.phidgets.push(phidget);
			PhidgetController.builder.buildPhidgetList();
		}else{
			return false; //prevent closing dialog
		}
	}
	
	this.editPhidget = function (note) {
		var subject = PhidgetController.getPhidgetByName(note.data.name);
		JSDialog.openDialog('jsp/content/forms/phidget-new.html', note.sender, subject, {buttonName:'Save'});
	};
	
	this.didLoadEditPhidget = function (note) {
		var phidget = note.data.subject;
		
		var tmpl = note.data.template;
		tmpl.setObject(phidget);
		tmpl.set('ports', PhidgetController.builder.buildPhidgetInputPorts(
				phidget.initialInputState, phidget.initialOutputState, phidget.onTriggerEventNames, phidget.offTriggerEventNames));
	};
	
	this.didShowEditPhidget = function (note) {
		jQuery('input#phidgetName').attr('disabled', true);
		jQuery('select#portSize > option[value="' + note.data.subject.portSize + '"]').attr('selected', true);
	};
	
	this.shouldEditPhidget = function (note) {
		var phidgetData = note.data;
		
		if (PhidgetHelper.validate(phidgetData)) {
			var phidget = PhidgetHelper.makePhidget(phidgetData);
			PhidgetController.replacePhidget(phidget);
			PhidgetController.builder.buildPhidgetList();
		}else{
			return false; //prevent closing dialog
		}
	}
	
	this.removePhidget = function (note) {
		var name = note.data.name;
		var list = PhidgetController.getPhidgetList();
    	
    	for (var i = 0, c = list.length; i < c; i++) {
    		var phidget = list[i];

    		if (!phidget) {
    			continue;
    		}
    		
    		if (phidget.name == name) {
    			list.remove(i);
    		}
    	}
		PhidgetController.builder.buildPhidgetList();
	};
};