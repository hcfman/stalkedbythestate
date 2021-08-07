<h1>User Management</h1>
<form id="smsForm">
	<fieldset>
		
		<input style="float:right" type="button" name="Add User" onclick="JSDialog.openDialog('jsp/content/forms/user-new.html', this, null, {buttonName:'Save'})" value="Add User">
		
		<table class="data">
			<thead>
				<tr>
					<th>Name</th>
					<th>Role</th>
				</tr>
			</thead>
			<tbody id="users"></tbody>
		</table>
		
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="UsersController.save()">
	</fieldset>
</form>


<jsp:include page="/jsp/content/components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/users/templates.js"></script>
<script type="text/javascript" src="js/users/builder.js"></script>
<script type="text/javascript" src="js/users/listener.js"></script>
<script type="text/javascript" src="js/users/controller.js"></script>
<script type="text/javascript" src="js/users/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('usersgetjson');
	} catch (e) {
		console.error( e );
	}
</script>
