<h1>Actions</h1>

<form action="" method="post">
	<fieldset>
		
		<table class="wrapper">
			<tbody>
				<tr>
					<td>&nbsp;</td>
					<td style="text-align:right">
					
					<input type="button" name="Add action" onclick="JSDialog.openDialog('jsp/content/forms/action-new.html', this, null, {buttonName:'Save'})" value="Add action">
					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<!-- Action list -->
						<table class="data" cellspacing="0px">
							<thead>
								<tr>
									<th>Guest</th>
									<th>Event</th>
									<th>Type</th>
									<th>Name</th>
									<th>Description</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
								</tr>
							</thead>
							<tbody id="actionList"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
		
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="ActionController.save()">
	</fieldset>
</form>

</div>

<div class="hr"></div>

</div>
</div>
</div>


<jsp:include page="components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/actions/templates.js"></script>
<script type="text/javascript" src="js/actions/helpers.js"></script>
<script type="text/javascript" src="js/actions/listener.js"></script>
<script type="text/javascript" src="js/actions/builder.js"></script>
<script type="text/javascript" src="js/actions/controller.js"></script>
<script type="text/javascript" src="js/actions/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('actionsgetjson');
	} catch (e) {
		console.error( e );
	}
</script>
