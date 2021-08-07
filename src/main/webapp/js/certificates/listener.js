CertificateFormListener = function () {
	
	this.dialogDidLoad = function (note) {
		var tmpl = note.data.template;
		
    	if (note.sender.name == 'GenerateCSR') {
    		tmpl.set('csr', 'Failed to generate the CSR');
    		
    		jQuery.ajax( {
                url : 'csrgetjson',
                type : 'GET',
                contentType: 'application/json',
                dataType: 'json',
                async:false,
                error: function (xhr) {
                	ShowActivity(false);

                	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
                	JSPostNotification('OnError', 'Failed to generate CSR<br>Reason: ' + reason );
                	
        			// TODO: do stuff with response...
                },
                success: function (response) {
                	if (response.result == true && response.csr) {
                		tmpl.set('csr', response.csr);
                	}else{
                		JSPostNotification('OnError', response.messages.join('<br>') );
                	}
                	
                	ShowActivity(false);
        		}
            });
    	}else if (note.sender.name == 'ExportCertificate' || note.sender.name == 'ExportOwnCertificate') {
    		console.log(note);
    		tmpl.set('content', note.data.subject.csr);
    	}else if (note.sender.name == 'ViewCertificate') {
    		note.data.template.setObject(note.data.subject);
    	}
    };
    
	this.shouldGenerateCertificate = function (note) {
		var certificate = note.data;
		
		if (CertificateHelper.validateCertificate(certificate)) {
			CertificateController.generateCertificate(certificate);
		}else{
			return false;
		}
	};
	
	this.shouldFetchCertificate = function (note) {
		var fetchInfo = note.data;
		
		if (CertificateHelper.validateFetchInfo(fetchInfo)) {
			CertificateController.fetchCertificate(fetchInfo);
		}else{
			return false;
		}
	};
	
	this.shouldImportCertificate = function (note) {
		var content = note.data.content || '';
		if (content != '') {
			CertificateController.importCertificate(note.data);
			return true;
		}else{
			JSPostNotification('OnError', 'Certificate got no content');
		}
		
		return false;
	};
	
	this.removeCertificate = function (store, alias) {
		var list = CertificateController.certificateData[store];
		
		for (var i = 0, c = list.length; i < c; i++) {
			if (list[i] != undefined && list[i].alias == alias) {
				list.remove(i);
			}
		}
		
		CertificateController.builder.buildCertificateList(store, list);
	};
	
	this.exportCertificate = function (store, alias, sender) {
		var storeName = store.toUpperCaseFirst();
		
		ShowActivity(true, storeName + ' is exporting certificate...');
		
    	jQuery.ajax( {
            url : 'export' + store + 'cert',
            type : 'POST',
            data : {"alias": alias},
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to export certificate in ' + storeName + '<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true) {
            		JSDialog.openDialog('jsp/content/forms/certificate-exportcert.html', sender, response, {buttonName:'OK'});
            	}else{
            		JSPostNotification('OnError', response.messages.join('<br>') );
            	}
            	
            	ShowActivity(false);
    		}
        });
	};
	
	this.viewOwnCertificate = function (sender) {
		var certificate = CertificateController.sections['certificates'].certificate;
		JSDialog.openDialog('jsp/content/forms/certificate-view.html', sender, certificate, {buttonName:'OK'});
	};
	
	this.viewCertificate = function (store, alias, sender) {
		var certificate = {};
		
		var list = CertificateController.certificateData[store];
		
		for (var i = 0, c = list.length; i < c; i++) {
			if (list[i] != undefined && list[i].alias == alias) {
				certificate = JSObject.clone(list[i]);
				
				var owner = SharedTemplates.getTemplate('certificate-section');
				owner.setObject(list[i].owner);
				certificate.owner = owner.render();
				
				var issuer = SharedTemplates.getTemplate('certificate-section');
				issuer.setObject(list[i].issuer);
				certificate.issuer = issuer.render();
			}
		}
		
		JSDialog.openDialog('jsp/content/forms/certificate-view.html', sender, certificate, {buttonName:'OK'});
	};
	
	this.shouldImportCertificateInKeystore = function (note) {
		ShowActivity(true, 'Keystore is importing certificate...');
    	
		if ( !CertificateHelper.isValidCertificateAlias(note.data.alias, 'keystore') ) {
			JSPostNotification('OnError', 'Certificate got invalid alias. Choose another name.');
			ShowActivity(false);
			return false;
		}
		
		// delete newlines in front and place one at end.
		var tmp = note.data;
		tmp.content = tmp.content.replace(/^\s+/, '').replace(/\s+$/, '\n');
		
    	var data = Configuration.toJSON( tmp );
    	
    	jQuery.ajax( {
            url : 'importkeystore',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to import certificate in keystore<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true) {
            		location.reload();
            	}else{
            		JSPostNotification('OnError', response.messages.join('<br>') );
            	}
            	
            	ShowActivity(false);
    		}
        });
	};
	
	this.shouldImportCertificateInTruststore = function (note) {
		ShowActivity(true, 'Truststore is importing certificate...');
    	
		if ( !CertificateHelper.isValidCertificateAlias(note.data.alias, 'truststore') ) {
			JSPostNotification('OnError', 'Certificate got invalid alias. Choose another name.');
			ShowActivity(false);
			return false;
		}
		
		// delete newlines in front and place one at end.
		var tmp = note.data;
		tmp.content = tmp.content.replace(/^\s+/, '').replace(/\s+$/, '\n');
		
    	var data = Configuration.toJSON( note.data );
    	
    	jQuery.ajax( {
            url : 'importtruststore',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to import certificate in truststore<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true) {
            		location.reload();
            	}else{
            		JSPostNotification('OnError', response.messages.join('<br>') );
            	}
            	
            	ShowActivity(false);
    		}
        });
	};
};