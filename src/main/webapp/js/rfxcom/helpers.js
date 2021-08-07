RfxcomHelper = function() {
	this.validate = function(command) {
		var validator = Validator.getValidator('rfxcom');

		jQuery('div.ui-dialog-content input').removeClass('validation-error');

		return validator.check(command);
	};

	showHide = function() {
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

			if (RfxcomController.dummy.list[i].operator != 'RANGE') {
				jQuery('#value_2_' + i).hide();
			}
		}
	}

	this.removeInputListRow = function(index) {
		RfxcomController.dummy.list.remove(index);
		var tmpl = RfxcomController.builder.buildRfxcomInputTable();
		jQuery('#widget').html(tmpl);
		showHide();
	}

	this.removeOutputListRow = function(index) {
		RfxcomController.dummy.list.remove(index);
		var tmpl = RfxcomController.builder.buildRfxcomOutputTable();
		jQuery('#widget').html(tmpl);
		jQuery('#value2').attr('disabled', true);
		jQuery('#value2').val("00");
		showHide();
	}

	this.addInputListRow = function() {
		RfxcomController.dummy.list.push({
			mask : "FF",
			operator : "EQ",
			value1 : "00"
		});
		var tmpl = RfxcomController.builder.buildRfxcomInputTable();
		jQuery('#widget').html(tmpl);

		showHide();
	}

	this.addOutputListRow = function() {
		RfxcomController.dummy.list.push({
			value1 : "00"
		});
		var tmpl = RfxcomController.builder.buildRfxcomOutputTable();
		jQuery('#widget').html(tmpl);
		jQuery('#value2').attr('disabled', true);

		showHide();
	}

	this.outputValueChanged = function(obj, index) {
		RfxcomController.dummy.list[index].value1 = obj.value
	}

	this.inputMaskChanged = function(obj, index) {
		RfxcomController.dummy.list[index].mask = obj.value
	}

	this.inputOperatorChanged = function(obj, index) {
		RfxcomController.dummy.list[index].operator = obj.value;
		if (obj.value == 'RANGE') {
			RfxcomController.dummy.list[index].value2 = '00';
			jQuery('#value_2_' + index).val('00');
			jQuery('#value_2_' + index).show();
		} else {
			jQuery('#value_2_' + index).hide();
		}
	}

	this.inputValue1Changed = function(obj, index) {
		RfxcomController.dummy.list[index].value1 = obj.value
	}

	this.inputValue2Changed = function(obj, index) {
		RfxcomController.dummy.list[index].value2 = obj.value
	}

};

RfxcomHelper = new RfxcomHelper();