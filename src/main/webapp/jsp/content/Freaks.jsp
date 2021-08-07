
<h1>Freaks</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add Freak" onclick="JSDialog.openDialog('jsp/content/forms/freak-new.html', this, null, {buttonName:'Save'})" value="Add Freak">
					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						
						<table class="data" cellspacing="0px">
							<thead>
								<tr>
									<th>Name</th>
									<th>Description</th>
									<th>Guest</th>
									<th>Protocol</th>
									<th>Host</th>
									<th>Port</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="freakList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="FreakController.save()">
	</fieldset>
</form>


<jsp:include page="components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/freak/templates.js"></script>
<script type="text/javascript" src="js/freak/helpers.js"></script>
<script type="text/javascript" src="js/freak/listener.js"></script>
<script type="text/javascript" src="js/freak/builder.js"></script>
<script type="text/javascript" src="js/freak/controller.js"></script>
<script type="text/javascript" src="js/freak/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('freaksgetjson');
	} catch (e) {
		console.error( e );
	}
</script>