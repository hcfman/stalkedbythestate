/**
	Responsable for building form parts on demand
*/
UsersBuilder = function () {
	
	this.buildUsers = function (userList) {
		var html = '';
		var tmpl = SharedTemplates.getTemplate('user-row');

		for (var i = 0, c = userList.length; i < c; i++) {
			
			var user = userList[i];
			if ( typeof(user) == 'object' ) {
				tmpl.setObject(user);
				html += tmpl.render();
				tmpl.reset();
			}
			
		}
		
		jQuery('tbody#users').html(html);
		
	};
	
};