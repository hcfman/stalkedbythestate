<h1>E-mail Provider</h1>
<form id="emailForm">
	<fieldset>
		
		<table id="email" class="wrapper"></table>
		<div class="seperator"></div>
		<input type="button" class="submit" value="Save" onclick="EmailController.save()">
	</fieldset>
</form>


<jsp:include page="/jsp/content/components/scripts.jsp"  />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/email/templates.js"></script>
<script type="text/javascript" src="js/email/controller.js"></script>
<script type="text/javascript" src="js/email/validators.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('emailgetjson');
	} catch (e) {
		console.error( e );
	}
</script>