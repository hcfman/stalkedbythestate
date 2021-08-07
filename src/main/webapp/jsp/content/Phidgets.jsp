
<h1>Phidgets</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add Phidget" onclick="JSDialog.openDialog('jsp/content/forms/phidget-new.html', this, null, {buttonName:'Save'})" value="Add Phidget">
					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						
						<table class="data" cellspacing="0px">
							<thead>
								<tr>
									<th>Name</th>
									<th>Description</th>
									<th>Serial Number</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="phidgetList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="PhidgetController.save()">
	</fieldset>
</form>


<jsp:include page="components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/phidgets/templates.js"></script>
<script type="text/javascript" src="js/phidgets/helpers.js"></script>
<script type="text/javascript" src="js/phidgets/listener.js"></script>
<script type="text/javascript" src="js/phidgets/builder.js"></script>
<script type="text/javascript" src="js/phidgets/controller.js"></script>
<script type="text/javascript" src="js/phidgets/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('phidgetsgetjson');
	} catch (e) {
		console.error( e );
	}
</script>