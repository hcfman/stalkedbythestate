CertificateBuilder = function () {
	this.buildOwnCertificate = function (certificate) {
		var tmpl, html = '';
		
		if (!certificate) {
			tmpl = SharedTemplates.getTemplate('certificate-empty');
			html = tmpl.render();
		}else{
			tmpl = SharedTemplates.getTemplate('certificate');
			tmpl.setObject(certificate);
			
			var owner = SharedTemplates.getTemplate('certificate-section');
			owner.setObject(certificate.owner);
			tmpl.set('owner', owner.render());
			
			var issuer = SharedTemplates.getTemplate('certificate-section');
			issuer.setObject(certificate.issuer);
			tmpl.set('issuer', issuer.render());
			
			html = tmpl.render();
		}
		
		jQuery('#certificateInfo').html(html);
	};
	
	
	this.buildOwnCertificateChain = function (certificateChain) {
		var tmpl, html = jQuery('#ownCertificateChain ul');
		
		if (!certificateChain) {
			tmpl = SharedTemplates.getTemplate('certificate-empty');
			html = tmpl.render();
		}else{
			tmpl = SharedTemplates.getTemplate('chained-certificate');
			
			for (var i = 0, c = certificateChain.length; i < c; i++) {
				tmpl.set('commonName', certificateChain[i].owner.commonName );
				tmpl.set('index', i );

				var li = jQuery(tmpl.render());
				li.click(function (e) {
					CertificateController.showCertificateInChainAt(this.title);
					e.stopImmediatePropagation();
				});
				
				html.append( li );
				
				html = html.find('li');
				tmpl.reset();
			}
		}
		
		jQuery('#ownCertificateChain li').last().click();
		jQuery('#certGenButton').val( certificateChain ? 'Re-generate' : 'Generate');
	};
	
	this.buildCertificateList = function (id, list) {
		
		var tmpl, html = '';
		
		if (list.length < 1) {
			tmpl = SharedTemplates.getTemplate('certificates-list-empty');
			html = tmpl.render();
		}else{
			tmpl = SharedTemplates.getTemplate('certificates-list-row');
			
			for (var i = 0, c = list.length; i < c; i++) {
				var cert = list[i];
				
				tmpl.setObject(cert);
				tmpl.set('commonName', cert.owner.commonName ? cert.owner.commonName : '?');
				tmpl.set('store', id);
				html += tmpl.render();
				tmpl.reset();
			}
		}
		
		
		jQuery('#' + id + '-list').html(html);
	};
};