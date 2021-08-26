<h1>Maintenance</h1>

<div class="system-section">
	<div class="section-content">
		<h2>Reboot</h2>
		<br> <input type="button" value="Reboot system"
			onclick="MaintenanceController.rebootSystem();">
	</div>
</div>

<div class="system-section">
	<div class="section-content">
		<h2>Restart</h2>
		<br> <input type="button" value="Restart system"
					onclick="MaintenanceController.restartSystem();">
	</div>
</div>

<div class="system-section">
	<div class="section-content">
		<h2>Shutdown cleanly</h2>
		<br> <input type="button" value="Shutdown system"
			onclick="MaintenanceController.shutdownSystem();">
	</div>
</div>

<%--
<div class="system-section">
	<div class="section-content">
		<h2>Formatting</h2>
		<br> <input type="button" value="Format disk"
			onclick="MaintenanceController.formatDisk();">
	</div>
</div>
--%>

<div class="system-section">
	<div class="section-content">
		<h2>Backup</h2>
		<br> <input type="button" value="Backup"
			onclick="JSDialog.openDialog('jsp/content/forms/backup-new.html', {name: 'Backup'}, 'restoreform', {buttonName:'Download'})">
	</div>
</div>

<iframe name="tiframe" id="tiframe" src="about:blank" style="display: none"></iframe>

<div class="system-section">
	<div class="section-content">
		<h2>Restore</h2>
		<br>
		<!-- 
		<input type="button" value="Restore"
			onclick="JSDialog.openDialog('jsp/content/forms/restore-new.html', this, null, {buttonName:'Upload'})">
 -->
		<input type="button" value="Restore"
			onclick="JSDialog.openDialog('jsp/content/forms/restore-new.html', {name: 'Restore'}, 'restoreform', {buttonName:'Upload'})">
	</div>
</div>

<br>

<jsp:include page="/jsp/content/components/scripts.jsp" />

<script type="text/javascript">
	var now = new Date();
	jQuery('#manualDate').val(
			now.getDate() + '-' + now.getMonth() + '-' + now.getFullYear());
	jQuery('#manualTime').val(
			now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds());
</script>

<!-- Loading action scripts -->
<script type="text/javascript" src="js/maintenance/controller.js"></script>
<script type="text/javascript" src="js/restore/listener.js"></script>
<script type="text/javascript" src="js/restore/controller.js"></script>
<script type="text/javascript" src="js/backup/listener.js"></script>
<script type="text/javascript" src="js/backup/controller.js"></script>
