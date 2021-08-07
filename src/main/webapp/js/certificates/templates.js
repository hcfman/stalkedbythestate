SharedTemplates.setTemplate('certificate-empty', '\
		<tr>\
		<td><center>You have no own certificate yet.</center></td>\
		</tr>\
');

SharedTemplates.setTemplate('certificate', '\
		<fieldset>\
			<legend>Owner</legend>\
			%{owner}\
		</fieldset>\
		<fieldset>\
			<legend>Issuer</legend>\
			%{issuer}\
		</fieldset>\
		<fieldset>\
			<legend>Certificate</legend>\
			<dl>\
				<dt>Fingerprint</dt> <dd><pre style="margin-left:3px">%{fingerprint}</pre></dd>\
				<dt>Valid from</dt> <dd>&nbsp;%{validFrom}</dd>\
				<dt>Valid to</dt> <dd>&nbsp;%{validTo}</dd>\
				<dt>Key Algorithm</dt> <dd>&nbsp;%{keyAlg}</dd>\
				<dt>Key Size</dt> <dd>&nbsp;%{keySize}</dd>\
				<dt>Serial</dt> <dd>&nbsp;%{serial}</dd>\
			</dl>\
		</fieldset>\
');
        	
SharedTemplates.setTemplate('certificate-section', '\
		<dl>\
		<dt>Common Name</dt> <dd>&nbsp;%{commonName}</dd>\
		<dt>Organisational Unit</dt> <dd>&nbsp;%{organisationalUnit}</dd>\
		<dt>Organisation</dt> <dd>&nbsp;%{organisation}</dd>\
		<dt>State</dt> <dd>&nbsp;%{state}</dd>\
		<dt>Locality</dt> <dd>&nbsp;%{locality}</dd>\
		<dt>Country</dt> <dd>&nbsp;%{country}</dd>\
		</dl>\
');

SharedTemplates.setTemplate('chained-certificate', '\
		<li class="chainedCertificate" title="%{index}"><span>%{commonName}</span></li>\
');

SharedTemplates.setTemplate('certificates-list-empty', '\
	<tr>\
	<td><center>There are currently no other certificates.</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('certificates-list-row', '\
	<tr>\
	<td>%{alias}</td>\
	<td>\
		<p style="margin-top:0;"><u>%{commonName}</u></p>\
		<pre>%{fingerprint}</pre>\
	</td>\
	<td>\
		<input type="button" onclick="CertificateController.listener.exportCertificate(\'%{store}\', \'%{alias}\', this)" value="Export" name="ExportCertificate">\
		<input type="button" onclick="CertificateController.listener.viewCertificate(\'%{store}\', \'%{alias}\', this)" value="View" name="ViewCertificate">\
		<input type="button" onclick="CertificateController.listener.removeCertificate(\'%{store}\', \'%{alias}\', this)" value="-" name="RemoveCertificate"></td>\
	</tr>\
');


