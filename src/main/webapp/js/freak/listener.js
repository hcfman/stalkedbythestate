FreakFormListener = function () {
	this.protocolChanged = function (select) {
		if (select.value == 'HTTPS') {
			jQuery('.VerifyHostnameAttribute').show();
		}else{
			jQuery('.VerifyHostnameAttribute').hide();
			jQuery('.VerifyHostnameAttribute input').attr('checked', false);
		}
	};
	
	this.shouldAddFreak = function (note) {
		var freakData = note.data;
		
		if (FreakHelper.validate(freakData)) {
			
			var freak = freakData;
			FreakController.freakList.push(freak);
			FreakController.builder.buildFreakList();
		}else{
			return false; //prevent closing dialog
		}
	}
	
	this.editFreak = function (note) {
		var subject = FreakController.getFreakByName(note.data.name);
		JSDialog.openDialog('jsp/content/forms/freak-new.html', note.sender, subject, {buttonName:'Save'});
	};
	
	this.didLoadEditFreak = function (note) {
		console.log(note);
		var tmpl = note.data.template;
		tmpl.setObject(note.data.subject);
	}
	
	this.didShowEditFreak = function (note) {
		jQuery('#freakName').attr('disabled', true);
		jQuery('#freakGuest').attr('checked', note.data.subject.guest);
		jQuery('#freakProtocol > option').each( function () {
			if (this.value == note.data.subject.protocol) {
				this.selected = true;
				FreakController.listener.protocolChanged( document.getElementById('freakProtocol') );
				jQuery('.VerifyHostnameAttribute input').attr('checked', !!note.data.subject.verifyHostname);
			}
		});
	}
	
	this.shouldEditFreak = function (note) {
		var freakData = note.data;
		
		if (FreakHelper.validate(freakData)) {
			
			var freak = freakData;
			FreakController.replaceFreak(freak);
			FreakController.builder.buildFreakList();
		}else{
			return false; //prevent closing dialog
		}
	}
	
	this.removeFreak = function (note) {
		FreakController.removeFreakByName(note.data.name);
		FreakController.builder.buildFreakList();
	}
}