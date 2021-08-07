<h1>Certificates</h1>


<div class="system-section">
	<h2>Certificates</h2>
	
	<div class="section-content">
		<div class="section-content-title">Own certificate</div>
		
		<div id="ownCertificateChain">
			<ul>
			</ul>
		</div>
		
		<div id="certificateInfo"></div>
		
		<input id="certGenButton" type="button" value="Generate" name="Generate Certificate" onclick="JSDialog.openDialog('jsp/content/forms/certificate-new.html', this, null, {buttonName:'Generate'})">
		<input type="button" value="Generate CSR" name="GenerateCSR" onclick="JSDialog.openDialog('jsp/content/forms/certificate-csr.html', this, null, {buttonName:'OK'})">
		
		<input type="button" value="Import Own Certificate" name="ImportOwnCertificate" onclick="JSDialog.openDialog('jsp/content/forms/certificate-importcert.html', this, null, {buttonName:'Import'})">
		<input type="button" value="Export Own Certificate" name="ExportOwnCertificate" onclick="CertificateController.listener.exportCertificate('', 'sbts', this)">
	</div>
	
	
	<div class="section-content">
		<div class="section-content-title">Other certificates</div>
		
		<div id="cert-tabs">
			<ul>
				<li><a href="#keystore-1">Keystore</a></li>
				<li><a href="#truststore-2">Truststore</a></li>
			</ul>
			<div id="keystore-1">
				<input style="float:right;" type="button" name="Import Certificate In Keystore" onclick="JSDialog.openDialog('jsp/content/forms/certificate-importcert.html', this, null, {buttonName:'Import'})" value="Import Certificate">
				
				<table class="data">
					<thead>
						<tr>
							<th>Alias</th>
							<th>Info</th>
							<th>&nbsp;</th>
						</tr>
					</thead>
					
					<tbody id="keystore-list"></tbody>
				</table>
			</div>
			
			<div id="truststore-2">
				<input style="float:right;" type="button" name="Import Certificate In Truststore" onclick="JSDialog.openDialog('jsp/content/forms/certificate-importcert.html', this, null, {buttonName:'Import'})" value="Import Certificate">
				
				<table class="data">
					<thead>
						<tr>
							<th>Alias</th>
							<th>Info</th>
							<th>&nbsp;</th>
						</tr>
					</thead>
			
					<tbody id="truststore-list"></tbody>
				</table>
			</div>
		</div>
		
		<input type="button" value="Save Certificates" onclick="CertificateController.saveCertificateList()">
	</div>
</div>

<br>

<jsp:include page="/jsp/content/components/scripts.jsp" />
		
<!-- Loading action scripts -->
<script type="text/javascript" src="js/certificates/templates.js"></script>
<script type="text/javascript" src="js/certificates/builder.js"></script>
<script type="text/javascript" src="js/certificates/helpers.js"></script>
<script type="text/javascript" src="js/certificates/listener.js"></script>
<script type="text/javascript" src="js/certificates/controller.js"></script>
<script type="text/javascript" src="js/certificates/validators.js"></script>


<!-- Start loading the config -->
<script type="text/javascript">
	
	jQuery( "#cert-tabs" ).tabs();

	try {
		ConfigLoadJSON('certificatesgetjson');
	} catch (e) {
		console.error( e );
	}
</script>