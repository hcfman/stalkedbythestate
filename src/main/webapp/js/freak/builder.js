FreakBuilder = function () {
	this.buildFreakList = function () {
		var list = '';
		var tmpl;
		var freaks = FreakController.getFreakList();
		
		// render all actions
		if ( freaks.length > 0 ) {
			tmpl = SharedTemplates.getTemplate('freak-list-row');
				
			for (var i = 0, c = freaks.length; i < c; i++) {
				var freak = freaks[i];
				
				if (!freak) {
					console.warn( 'Freak not defined but in freakList' );
					continue;
				}
				
				tmpl.setObject(freak);
				tmpl.set('guestText', freak.guest ? '<img alt="yes" src="images/check.png">' : '');
				list += tmpl.render();
				
				tmpl.reset();
			}
		// show a message for empty list
		}else{
			tmpl = SharedTemplates.getTemplate('freak-list-empty');
			list += tmpl.render();
		}
		
		jQuery('#freakList').html(list);
	}
}