<h1>Profiles</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add Profile" onclick="JSDialog.openDialog('jsp/content/forms/profile-new.html', this, null, {buttonName:'Save'})" value="Add Profile">
					
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
									<th>Tag Name</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="profileList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="ProfileController.save()">
	</fieldset>
</form>


<jsp:include page="components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/profile/templates.js"></script>
<script type="text/javascript" src="js/profile/helpers.js"></script>
<script type="text/javascript" src="js/profile/listener.js"></script>
<script type="text/javascript" src="js/profile/builder.js"></script>
<script type="text/javascript" src="js/profile/controller.js"></script>
<script type="text/javascript" src="js/profile/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('profilesgetjson');
	} catch (e) {
		console.error( e );
	}
</script>