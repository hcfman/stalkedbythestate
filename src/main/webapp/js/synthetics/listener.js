/*
 * Main event listener for ActionController
 * TODO: get rid of unused methods
 */
SyntheticFormListener = function () {
	
	this.didLoadAddSynthetic = function (note) {
		
		var tmpl = note.data.template;
		tmpl.set('availableEventNames', SyntheticController.availableEventNames);
	};
	
	this.shouldAddSynthetic = function (note) {
		
		var syntheticData = note.data;
		
		if (syntheticData.triggerEventNames == null) {
			syntheticData.triggerEventNames = [];
		}
		
		if (SyntheticHelper.validate(syntheticData)) {
			
			var synthetic = syntheticData;
			SyntheticController.synthetics[synthetic.result] = synthetic;
			SyntheticController.builder.buildSyntheticList();
		}else{
			return false; //prevent closing dialog
		}
	}
	
	this.editSynthetic = function (note) {
		var subject = SyntheticController.getSyntheticByName(note.data.name);
		JSDialog.openDialog('jsp/content/forms/synthetic-new.html', note.sender, subject, {buttonName:'Save'});
	};
	
	this.didLoadEditSynthetic = function (note) {
		var synthetic = note.data.subject;
		
		var tmpl = note.data.template;
		tmpl.set('availableEventNames', SyntheticController.availableEventNames);
		tmpl.setObject(synthetic);
	};
	
	this.didShowEditSynthetic = function (note) {
		jQuery('input#syntheticName').attr('disabled', true);
		jQuery('select#eventNames > option').each(function () {
			
			if ( note.data.subject.triggerEventNames.hasValue(this.value) ) {
				this.selected = true;
			}
			
		});
	};
	
	this.shouldEditSynthetic = function (note) {
		this.shouldAddSynthetic(note);
	}
	
	this.removeSynthetic = function (note) {
		var name = note.data.name;
		delete SyntheticController.synthetics[name];
		SyntheticController.builder.buildSyntheticList();
	};
};