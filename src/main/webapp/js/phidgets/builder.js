PhidgetBuilder = function () {
	this.buildPhidgetList = function () {
		
		var list = '';
		var tmpl;
		var phidgets = PhidgetController.getPhidgetList();
		
		var count = 0;
			
		tmpl = SharedTemplates.getTemplate('phidget-list-row');
				
		for (var i = 0, c = phidgets.length; i < c; i++) {
			var phidget = phidgets[i];
			
			if (!phidget) {
				console.warn( 'Phidget not defined but in list' );
				continue;
			}
			
			tmpl.setObject(phidget);
			
			list += tmpl.render();
			
			tmpl.reset();
			count++;
		}
		// show a message for empty list
		if (count == 0) {
			tmpl = SharedTemplates.getTemplate('phidget-list-empty');
			list = tmpl.render();
		}
		
		jQuery('#phidgetList').html(list);
		
	};
	
	
this.buildPhidgetInputPorts = function (inPorts, outPorts, onTriggers, offTriggers) {
		var list = '';
		var tmpl;
			
		tmpl = SharedTemplates.getTemplate('phidget-port-row');
				
		for (var i = 0, c = inPorts.length; i < c; i++) {
			var ip = inPorts[i];
			var op = outPorts[i];
			tmpl.set('index', i);
			tmpl.set('isInputChecked', ip ? 'checked' : '');
			tmpl.set('isOutputChecked', op ? 'checked' : '');
			tmpl.set('onTrigger', onTriggers[i]);
			tmpl.set('offTrigger', offTriggers[i]);
			
			list += tmpl.render();
			
			tmpl.reset();
		}
		
		return '<table>' + list + '</table>';
		
	};
};