<h1>Cameras</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add camera" onclick="JSDialog.openDialog('jsp/content/forms/camera-new.html', this, null, {buttonName:'Save'})" value="Add Camera">
					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<!-- Action list -->
						<table class="data" cellspacing="0px">
							<thead>
								<tr>
									<th>Index</th>
									<th>Name</th>
									<th>Description</th>
									<th>Priority</th>
									<th>Caching</th>
									<th>Guest</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="cameraList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="CameraController.save()">
	</fieldset>
</form>


<jsp:include page="/jsp/content/components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/cameras/templates.js"></script>
<script type="text/javascript" src="js/cameras/helpers.js"></script>
<script type="text/javascript" src="js/cameras/listener.js"></script>
<script type="text/javascript" src="js/cameras/builder.js"></script>
<script type="text/javascript" src="js/cameras/controller.js"></script>
<script type="text/javascript" src="js/cameras/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('camerasgetjson');
	} catch (e) {
		console.error( e );
	}
</script>