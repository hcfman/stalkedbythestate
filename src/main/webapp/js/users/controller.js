UsersController = function() {
	this.load = function() {
		// respond to the 'LoadedJSON' message with 'initialise' method
		JSNotificationCenter.addObserver(this, 'initialise', 'LoadedJSON');
	};

	/**
	 * Load all the JSON data into the controller.
	 */
	this.initialise = function(note) {
		var data = note.data;

		this.data = data;
		this.builder = new UsersBuilder();
		this.listener = new UsersFormListener();

		this.registerFormNotificationListener();

		this.builder.buildUsers(data.users);

		console.log('Initialised UsersController');
	};

	this.registerFormNotificationListener = function() {
		var a = this.listener;
		var n = JSNotificationCenter;

		n.addObserver(a, 'shouldAddUser', 'DialogShouldAddUser');
		n.addObserver(a, 'shouldEditUser', 'DialogShouldEditUser');

		n.addObserver(a, 'dialogDidLoad', 'DialogDidLoad');
		n.addObserver(a, 'dialogDidShow', 'DialogDidShow');

	};

	this.addUser = function(user) {
		delete user.password2;

		this.data.users.push(user);
		this.builder.buildUsers(this.data.users);
	};

	this.deleteUser = function(name) {
		if (name.toLowerCase() == 'admin') {
			JSPostNotification('OnError', 'Admin can\'t be deleted');
			return;
		}

		var users = this.data.users;

		for ( var i = 0, c = users.length; i < c; i++) {
			var user = users[i];

			if (user && user.name == name) {
				this.data.users.remove(i);
			}
		}

		this.builder.buildUsers(this.data.users);
	};

	this.editUser = function(user) {
		var users = this.data.users;

		for ( var i = 0, c = users.length; i < c; i++) {
			var u = users[i];

			if (u && u.name == user.name) {
				this.data.users[i] = user;
			}
		}

		this.builder.buildUsers(this.data.users);
	};

	this.getUser = function(name) {
		var users = this.data.users;

		for ( var i = 0, c = users.length; i < c; i++) {
			var user = users[i];

			if (user && user.name == name) {
				return user;
			}
		}
		return undefined;
	};

	this.save = function() {

		if (!confirm('Saving the users will restart the system. Still wan\'t to save?')) {
			return;
		}

		ShowActivity(true, 'Saving users...');

		var data = Configuration.toJSON(this.data.users);

		jQuery.ajax({
			url : 'users',
			type : 'POST',
			data : data,
			contentType : 'application/json',
			dataType : 'json',
			error : function(xhr) {
				ShowActivity(false);

				var reason = xhr.status + ' '
						+ xhr.statusText.toUpperCaseFirst();
				JSPostNotification('OnError',
						'Failed to save users<br>Reason: ' + reason);

				Reboot('Restarting the system', 30);
			},
			success : function(response) {
				ShowActivity(false);

				if (response.result === true) {
					Reboot('Restarting the system', 30);
					JSPostNotification('OnInfo', 'Saved users');
				} else {
					JSPostNotification('OnError', response.messages
							.join('<br>'));
				}
			}
		});
	};

	// kickstart!
	this.load();
};

UsersController = new UsersController();