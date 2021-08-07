WatchdogBuilder = function () {
	this.buildWatchdogList = function () {
		
		var list = '';
		var tmpl;
		var watchdogs = WatchdogController.getWatchdogList();
		
		var count = 0;
			
		tmpl = SharedTemplates.getTemplate('watchdog-list-row');
				
		for (var name in watchdogs) {
			var watchdog = watchdogs[name];
			
			if (!watchdog) {
				console.warn( 'Watchdog not defined but in list' );
				continue;
			}
			
			tmpl.set('name', name);
			
			list += tmpl.render();
			
			tmpl.reset();
			count++;
		}
		// show a message for empty list
		if (count == 0) {
			tmpl = SharedTemplates.getTemplate('watchdog-list-empty');
			list = tmpl.render();
		}
		
		jQuery('#watchdogList').html(list);
		
	};
};