<h1>Phidget Log</h1>

<pre id="log" style="font-family:monospace;"></pre>

<jsp:include page="/jsp/content/components/scripts.jsp" />

<!-- Loading system log scripts -->
<script type="text/javascript" src="js/phidget-log/controller.js"></script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('phidgetloggetjson');
	} catch (e) {
		console.error( e );
	}
</script>