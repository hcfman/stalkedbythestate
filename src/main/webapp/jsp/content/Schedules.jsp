<h1>Schedule Actions</h1>
<form>
	<fieldset>
		<input type="button" name="Add Schedule" onclick="JSDialog.openDialog('jsp/content/forms/schedule-new.html', this, null, {buttonName:'Save'})" value="Add Schedule">
		<div id="scheduleContainer"></div>
		
		<hr style="clear:both">
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="ScheduleController.save()">
	</fieldset>
</form>


<jsp:include page="/jsp/content/components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/schedule-actions/templates.js"></script>
<script type="text/javascript" src="js/schedule-actions/builder.js"></script>
<script type="text/javascript" src="js/schedule-actions/helpers.js"></script>
<script type="text/javascript" src="js/schedule-actions/listener.js"></script>
<script type="text/javascript" src="js/schedule-actions/controller.js"></script>
<script type="text/javascript" src="js/schedule-actions/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('schedulesgetjson');
	} catch (e) {
		console.error( e );
	}
</script>