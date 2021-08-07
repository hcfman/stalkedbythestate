
<h1>Watchdog Events</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add Watchdog" onclick="JSDialog.openDialog('jsp/content/forms/watchdog-new.html', this, null, {buttonName:'Save'})" value="Add Watchdog Events">
					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						
						<table class="data" cellspacing="0px">
							<thead>
								<tr>
									<th>Name</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="watchdogList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="WatchdogController.save()">
	</fieldset>
</form>


<jsp:include page="components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/watchdogs/templates.js"></script>
<script type="text/javascript" src="js/watchdogs/helpers.js"></script>
<script type="text/javascript" src="js/watchdogs/listener.js"></script>
<script type="text/javascript" src="js/watchdogs/builder.js"></script>
<script type="text/javascript" src="js/watchdogs/controller.js"></script>
<script type="text/javascript" src="js/watchdogs/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('watchdogsgetjson');
	} catch (e) {
		console.error( e );
	}
</script>