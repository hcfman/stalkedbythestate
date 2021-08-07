RfxcomBuilder = function () {
	this.buildCommandList = function () {
		var list = '';
		var tmpl;
		var commands = RfxcomController.getCommandList();
		
		// render all actions
		if ( commands.length > 0 ) {
			tmpl = SharedTemplates.getTemplate('rfxcomCommand-list-row');
				
			for (var i = 0, c = commands.length; i < c; i++) {
				var command = commands[i];
				
				if (!command) {
					console.warn( 'rfxcomCommand not defined but in rfxcomCommandList' );
					continue;
				}
				
				tmpl.setObject(command);
				
				list += tmpl.render();
				
				tmpl.reset();
			}
		// show a message for empty list
		}else{
			tmpl = SharedTemplates.getTemplate('rfxcomCommand-list-empty');
			list += tmpl.render();
		}
		jQuery('#rfxcomCommandList').html(list);
	}
	
	this.buildRfxcomInputTable = function () {
		var list = '';
			
		var inputWidgetTmpl = SharedTemplates.getTemplate('inputWidget');
		inputWidgetTmpl.reset();

		for (var i = 0, c = RfxcomController.dummy.list.length; i < c; i++) {
			var tmpl;
			var mask = RfxcomController.dummy.list[i].mask;
			var operator = RfxcomController.dummy.list[i].operator;
			if (operator == 'RANGE') {
				tmpl = SharedTemplates.getTemplate('rfxcom-input-row2');
				tmpl.set('value2', RfxcomController.dummy.list[i].value2);
			} else {
				tmpl = SharedTemplates.getTemplate('rfxcom-input-row2');
				tmpl.set('value2', '00');
			}
			tmpl.set('value1', RfxcomController.dummy.list[i].value1);
			tmpl.set('index', i);
			tmpl.set('mask', mask);
			
			if (i == 0) {
				tmpl.set('fieldUsage', 'Type');
			} else if (i == 1) {
				tmpl.set('fieldUsage', 'Sub&nbsp;type');
			} else if (i ==2) {
				tmpl.set('fieldUsage', 'Seq&nbsp;nbr');
			} else {
				tmpl.set('fieldUsage', '');
			}
			
			list += tmpl.render();
			
			tmpl.reset();
		}
		
		inputWidgetTmpl.set('inputWidgetRows', list);
		
		return inputWidgetTmpl.render();
	};

	this.buildRfxcomOutputTable = function () {
		var list = '';
			
		var outputWidgetTmpl = SharedTemplates.getTemplate('outputWidget');
		outputWidgetTmpl.reset();

		for (var i = 0, c = RfxcomController.dummy.list.length; i < c; i++) {
			var tmpl;
			tmpl = SharedTemplates.getTemplate('rfxcom-output-row');
			tmpl.set('value', RfxcomController.dummy.list[i].value1);
			tmpl.set('index', i);
			
			if (i == 0) {
				tmpl.set('fieldUsage', 'Type');
			} else if (i == 1) {
				tmpl.set('fieldUsage', 'Sub type');
			} else if (i ==2) {
				tmpl.set('fieldUsage', 'Seq nbr');
			} else {
				tmpl.set('fieldUsage', '');
			}
			
			list += tmpl.render();
			
			tmpl.reset();
		}
		
		outputWidgetTmpl.set('outputWidgetRows', list);
		
		return outputWidgetTmpl.render();
	};

}