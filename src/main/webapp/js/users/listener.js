
UsersFormListener = function () {
	
	this.onRoleChanged = function (username) {
		if (username.toLowerCase() == 'admin') {
			JSPostNotification('OnInfo', 'You can\'t change the role of an admin user');
			jQuery('#admin-role').attr('selected', true);
		}
	};
	
	this.shouldAddUser = function (note) {
		var user 		= note.data;
		var validator 	= Validator.getValidator('User');
		
		if (validator.check(user)) {
			
			UsersController.addUser(user);
			
			return true;
		}
		
		return false;
	};
	
	
	this.dialogDidLoad = function (note) {
		if (note.sender.name == 'Edit User') {
			var user = UsersController.getUser(note.data.subject);
			note.data.template.setObject(user);
		}
	};
	
	
	this.dialogDidShow = function (note) {
		if (note.sender.name == 'Edit User') {
			jQuery('#userName').attr('disabled', true);
			
			var user = UsersController.getUser(note.data.subject);

			jQuery('#userRole > option').each( function () {
				if ( user.role == this.value ) {
					this.selected = true;
				}
			});
		}
	};
	
	this.shouldEditUser = function (note) {
		var user 		= note.data;
		var validator 	= Validator.getValidator('User-edit');
		
		if (validator.check(user)) {
			
			UsersController.editUser(user);
			
			return true;
		}
		
		return false;
	};
}
