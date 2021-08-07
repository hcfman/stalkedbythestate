SyntheticBuilder = function () {
	this.buildSyntheticList = function () {
		
		var list = '';
		var tmpl;
		var synthetics = SyntheticController.getSyntheticList();
		
		var count = 0;
			
		tmpl = SharedTemplates.getTemplate('synthetic-list-row');
				
		for (var name in synthetics) {
			var synthetic = synthetics[name];
			
			if (!synthetic) {
				console.warn( 'Synthetic not defined but in list' );
				continue;
			}
			
			tmpl.set('name', name);
			
			list += tmpl.render();
			
			tmpl.reset();
			count++;
		}
		// show a message for empty list
		if (count == 0) {
			tmpl = SharedTemplates.getTemplate('synthetic-list-empty');
			list = tmpl.render();
		}
		
		jQuery('#syntheticList').html(list);
		
	};
};