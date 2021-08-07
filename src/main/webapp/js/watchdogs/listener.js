/*
 * Main event listener for ActionController
 * TODO: get rid of unused methods
 */
WatchdogFormListener = function() {

	this.didLoadAddWatchdog = function(note) {

		var tmpl = note.data.template;
		tmpl.set('availableEventNames', WatchdogController.availableEventNames);
	};

	this.shouldAddWatchdog = function(note) {

		var watchdogData = note.data;

		if (watchdogData.triggerEventNames == null) {
			watchdogData.triggerEventNames = [];
		}

		if (WatchdogHelper.validate(watchdogData)) {

			var watchdog = watchdogData;
			WatchdogController.watchdogs[watchdog.result] = watchdog;
			WatchdogController.builder.buildWatchdogList();
		} else {
			return false; // prevent closing dialog
		}
	}

	this.editWatchdog = function(note) {
		var subject = WatchdogController.getWatchdogByName(note.data.name);
		JSDialog.openDialog('jsp/content/forms/watchdog-new.html',
				note.sender, subject, {
					buttonName : 'Save'
				});
	};

	this.didLoadEditWatchdog = function(note) {
		var watchdog = note.data.subject;

		var tmpl = note.data.template;
		tmpl.set('availableEventNames', WatchdogController.availableEventNames);
		tmpl.setObject(watchdog);
	};

	this.didShowEditWatchdog = function(note) {
		jQuery('input#watchdogName').attr('disabled', true);
		jQuery('select#eventNames > option').each(function() {

			if (note.data.subject.triggerEventNames.hasValue(this.value)) {
				this.selected = true;
			}

		});
	};

	this.shouldEditWatchdog = function(note) {
		this.shouldAddWatchdog(note);
	}

	this.removeWatchdog = function(note) {
		var name = note.data.name;
		delete WatchdogController.watchdogs[name];
		WatchdogController.builder.buildWatchdogList();
	};
};