
<h1>Combination Events</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add Synthetic" onclick="JSDialog.openDialog('jsp/content/forms/synthetic-new.html', this, null, {buttonName:'Save'})" value="Add Combination Events">
					
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
							<tbody id="syntheticList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="SyntheticController.save()">
	</fieldset>
</form>


<jsp:include page="components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/synthetics/templates.js"></script>
<script type="text/javascript" src="js/synthetics/helpers.js"></script>
<script type="text/javascript" src="js/synthetics/listener.js"></script>
<script type="text/javascript" src="js/synthetics/builder.js"></script>
<script type="text/javascript" src="js/synthetics/controller.js"></script>
<script type="text/javascript" src="js/synthetics/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('syntheticsgetjson');
	} catch (e) {
		console.error( e );
	}
</script>