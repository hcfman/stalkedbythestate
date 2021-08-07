<h1>RFXCOM Commands</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add Device" onclick="JSDialog.openDialog('jsp/content/forms/rfxcom-new.html', this, null, {buttonName:'Save'})" value="Add RFXCOM Command">
					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<!-- Action list -->
						<table class="data" cellspacing="0px">
							<thead>
								<tr>
									<th>Name</th>
									<th>Description</th>
									<th>Event Name</th>
									<th>Type</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="rfxcomCommandList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="RfxcomController.save()">
	</fieldset>
</form>


<jsp:include page="/jsp/content/components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/rfxcom/templates.js"></script>
<script type="text/javascript" src="js/rfxcom/helpers.js"></script>
<script type="text/javascript" src="js/rfxcom/listener.js"></script>
<script type="text/javascript" src="js/rfxcom/builder.js"></script>
<script type="text/javascript" src="js/rfxcom/controller.js"></script>
<script type="text/javascript" src="js/rfxcom/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('rfxcomgetjson');
	} catch (e) {
		console.error( e );
	}
</script>