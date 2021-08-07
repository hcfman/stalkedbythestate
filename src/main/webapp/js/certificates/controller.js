CertificateController = function () {
    this.load = function () {
    	JSNotificationCenter.addObserver(this, 'initialiseSection', 'LoadedJSON');
    	
    	JSDialog.setController(this);
    	
    	this.builder = new CertificateBuilder();
		this.registerFormNotificationListener();
    };
    
    this.initialiseSection = function (note) {
    	var data = note.data;
    	
    	if (data.certificate) {
    		this.initCertificates(data);
    	}
    };
    
    this.registerFormNotificationListener = function () {
    	var a = this.listener = new CertificateFormListener();
    	var n = JSNotificationCenter;
    	
    	n.addObserver(a, 'shouldGenerateCertificate', 'DialogShouldGenerateCertificate');
    	n.addObserver(a, 'shouldFetchCertificate', 'DialogShouldFetchCertificate');
    	n.addObserver(a, 'shouldImportCertificate', 'DialogShouldImportOwnCertificate');
    	
    	n.addObserver(a, 'shouldImportCertificateInKeystore'  , 'DialogShouldImportCertificateInKeystore');
    	n.addObserver(a, 'shouldImportCertificateInTruststore', 'DialogShouldImportCertificateInTruststore');
    	
    	
    	n.addObserver(a, 'dialogDidLoad', 'DialogDidLoad');
    	n.addObserver(this, 'dialogDidShow', 'DialogDidShow');
    	
    };
    
    this.dialogDidShow = function (note) {
    	if (note.sender.name == 'ImportOwnCertificate') {
    		jQuery('#cert-alias').val('sbts').attr('disabled', true);
    	}
    };
    
    this.showCertificateInChainAt = function (index) {
    	this.builder.buildOwnCertificate( this.certificateData.certificate.certificateChain[index] );
    	return false;
    };
    
    /*
     * Starters
     */
    this.initCertificates = function (data) {
    	this.certificateData = data;
    	this.builder.buildOwnCertificateChain(data.certificate.certificateChain);
    	this.builder.buildCertificateList('keystore', data.keystore);
    	this.builder.buildCertificateList('truststore', data.truststore);
    };
    
    /*
     * 
     */
    this.generateCertificate = function (certificate) {
    	ShowActivity(true, 'Generating certificate...');
    	
    	var data = Configuration.toJSON( certificate );
    	
    	jQuery.ajax( {
            url : 'gencert',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to generate certificate<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true) {
            		//CertificateController.sections['certificates'].certificate = response.certificate;
            		//CertificateController.builder.buildOwnCertificate(response.certificate);
            		location.reload();
            	}else{
            		JSPostNotification('OnError', 'Failed to generate the certificate' );
            	}
            	
            	ShowActivity(false);
    		}
        });
    };
    
    this.importCertificate = function (certificate) {
    	ShowActivity(true, 'Importing certificate...');
    	
    	// delete newlines in front and place one at end.
		var tmp = note.data;
		tmp.content = tmp.content.replace(/^\s+/, '').replace(/\s+$/, '\n');
		
    	var data = Configuration.toJSON( {certificate:certificate} );
    	
    	jQuery.ajax( {
            url : 'importowncert',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to import certificate<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true) {
            		location.reload();
            	}else{
            		JSPostNotification('OnError', 'Failed to import the certificate' );
            	}
            	
            	ShowActivity(false);
    		}
        });
    };
    
    this.saveCertificateList = function () {
    	ShowActivity(true, 'Saving certificates...');
    	
    	var stores = {
    		keystore:this.certificateData.keystore,
    		truststore:this.certificateData.truststore
    	};
    	
    	var data = Configuration.toJSON( stores );
    	
    	jQuery.ajax( {
            url : 'editcerts',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save certificates<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == false) {
            		JSPostNotification('OnError', 'Failed to save the certificates. Try reloading the page if this error keeps occurring.' );
            	}else{
            		JSPostNotification('OnInfo', 'Saved the certificates' );
            	}
            	
            	ShowActivity(false);
    		}
        });
    };
    
    this.fetchCertificate = function (fetchInfo) {
    	ShowActivity(true, 'Fetching certificate...');
    	
    	var data = Configuration.toJSON( fetchInfo );
    	
    	jQuery.ajax( {
            url : 'installcert',
            type : 'POST',
            data : data,
            contentType: 'application/json',
            dataType: 'json',
            error: function (xhr) {
            	ShowActivity(false);
            	
            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to Fetching the certificate<br>Reason: ' + reason );
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true && response.certList) {
            		CertificateController.sections['certificates'].certList = response.certList;
            		CertificateController.builder.buildCertificateList(response.certList);
            	}else{
            		JSPostNotification('OnError', 'Failed fetching the certificate' );
            	}
            	
            	ShowActivity(false);
    		}
        });
    };
    
    this.load();
};


// singleton
CertificateController = new CertificateController();