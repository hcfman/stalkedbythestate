<h1>Button Groups</h1>
<form>
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add Button Group" onclick="JSDialog.openDialog('jsp/content/forms/buttongroup-new.html', this, null, {buttonName:'Save'})" value="Add Button Group">
					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<!-- Action list -->
						<table class="data" cellspacing="0px">
							<thead>
								<tr>
									<th>Group</th>
									<th>Buttons</th>
									<th>Url</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="buttonGroupList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="ButtonGroupController.save()">
	</fieldset>
</form>

<jsp:include page="/jsp/content/components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/button-groups/templates.js"></script>
<script type="text/javascript" src="js/button-groups/helpers.js"></script>
<script type="text/javascript" src="js/button-groups/listener.js"></script>
<script type="text/javascript" src="js/button-groups/builder.js"></script>
<script type="text/javascript" src="js/button-groups/controller.js"></script>
<script type="text/javascript" src="js/button-groups/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('buttonsgetjson');
	} catch (e) {
		console.error( e );
	}
</script>