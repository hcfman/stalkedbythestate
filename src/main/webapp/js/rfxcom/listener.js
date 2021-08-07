RfxcomFormListener = function() {

	this.rfxcomTypeChanged = function(select) {
		var value = select.value;

		if (value.match(/GENERIC OUTPUT/)) {
			jQuery('#widget').html(RfxcomController.builder
					.buildRfxcomOutputTable());
			jQuery('.eventNameRow').hide();
		} else {
			jQuery('#widget').html(RfxcomController.builder
					.buildRfxcomInputTable());
			jQuery('.eventNameRow').show();
		}
	};

	this.removeDevice = function(note) {
		RfxcomController.removeCommandByName(note.data.name);
		RfxcomController.builder.buildCommandList();
	};

	this.editDevice = function(note) {
		var name = note.data.name;
		var command = RfxcomController.getCommandByName(name);
		JSDialog.openDialog('jsp/content/forms/rfxcom-new.html',
				note.sender, command, {
					buttonName : 'Save'
				});
	};

	this.didLoadEditDevice = function(note) {
		var command = note.data.subject;
		var template = note.data.template;

		RfxcomController.dummy.list = [];
		var listItem = {};
		var len;
		if (command.rfxcomType == 'GENERIC INPUT')
			len = command.packetMask.length;
		else
			len = command.packetValues1.length;

		for ( var i = 0; i < len; i++) {
			if (command.rfxcomType == 'GENERIC INPUT') {
				listItem = {
					mask : command.packetMask[i],
					operator : command.packetOperator[i],
					value1 : command.packetValues1[i],
					value2 : command.packetValues2[i]
				};
			} else {
				listItem = {
					value1 : command.packetValues1[i]
				};

			}
			RfxcomController.dummy.list.push(listItem);
		}

		template.setObject(command);
		if (command.rfxcomType == 'GENERIC INPUT') {
			template.set('widget', RfxcomController.builder
					.buildRfxcomInputTable());
		} else {
			template.set('widget', RfxcomController.builder
					.buildRfxcomOutputTable());
		}
	};

	this.didLoadAddDevice = function(note) {
		RfxcomController.dummy.list = [];
		var template = note.data.template;
		
		template.set('widget', RfxcomController.builder
				.buildRfxcomInputTable());
		jQuery('.eventNameRow').show();
	}

	this.didShowEditDevice = function(note) {
		var command = note.data.subject;
		jQuery('#commandName').attr('disabled', true);
		jQuery('#rfxcomType > option').each(function() {
			if (command.rfxcomType.toLowerCase() == this.value.toLowerCase()) {
				this.selected = true;
			}
		});
		jQuery('#value2').attr('disabled', true);
		if (command.rfxcomType == 'GENERIC OUTPUT') {
			jQuery('.eventNameRow').hide();
		} else {
			jQuery('.eventNameRow').show();
		}

		for ( var i = 0, c = RfxcomController.dummy.list.length; i < c; i++) {
			jQuery('#operator' + i + ' > option').each(function() {
				if (RfxcomController.dummy.list[i].operator == this.value) {
					this.selected = true;
					if (this.value == 'RANGE') {
						jQuery('#value_2_' + i).show();
					} else {
						jQuery('#value_2_' + i).hide();
					}
				}

			});

		}

	};

	this.didShowAddDevice = function(note) {
		this.rfxcomTypeChanged(document.getElementById('rfxcomType'));
	};

	this.shouldAddDevice = function(note) {
		var data = note.data;

		if (RfxcomHelper.validate(data)) {
			// Copy out values that are not there by default
			var newCommand = {
				name : data.name,
				description : data.description,
				rfxcomType : data.rfxcomType
			};

			if (data.rfxcomType == 'GENERIC INPUT') {
				newCommand.eventName = data.eventName;
				newCommand.hysteresis = data.hysteresis;
			}

			newCommand.packetValues1 = [];
			newCommand.packetValues2 = [];
			newCommand.packetMask = [];
			newCommand.packetOperator = [];

			for ( var i = 0, c = RfxcomController.dummy.list.length; i < c; i++) {
				newCommand.packetValues1
						.push(RfxcomController.dummy.list[i].value1);
				newCommand.packetValues2
						.push(RfxcomController.dummy.list[i].value2);
				newCommand.packetMask.push(RfxcomController.dummy.list[i].mask);
				newCommand.packetOperator
						.push(RfxcomController.dummy.list[i].operator);
			}

			RfxcomController.commandList.push(newCommand);
			RfxcomController.builder.buildCommandList();
		} else {
			return false;
		}
	};

	this.shouldEditDevice = function(note) {
		var data = note.data;

		// validate the data provided by the dialogs form
		if (RfxcomHelper.validate(data)) {
			// Copy out values that are not there by default
			var newCommand = {
				name : data.name,
				description : data.description,
				rfxcomType : data.rfxcomType
			};
			
			if (data.rfxcomType == 'GENERIC INPUT') {
				newCommand.eventName = data.eventName;
				newCommand.hysteresis = data.hysteresis;
			}

			newCommand.packetValues1 = [];
			newCommand.packetValues2 = [];
			newCommand.packetMask = [];
			newCommand.packetOperator = [];

			for ( var i = 0, c = RfxcomController.dummy.list.length; i < c; i++) {
				newCommand.packetValues1
						.push(RfxcomController.dummy.list[i].value1);
				newCommand.packetValues2
						.push(RfxcomController.dummy.list[i].value2);
				newCommand.packetMask.push(RfxcomController.dummy.list[i].mask);
				newCommand.packetOperator
						.push(RfxcomController.dummy.list[i].operator);
			}

			RfxcomController.replaceCommand(newCommand);
			RfxcomController.builder.buildCommandList();
		} else {
			// return false to prevent closing the dialog
			return false;
		}
	};
}