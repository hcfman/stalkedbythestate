</head>
<body onunload="return JSPostNotification('ShouldExitPage', this);">

<div id="loading">Loading...</div>
<div id="message-container"></div>
<div id="error-container"></div>

<div id="frame">
<div id="content-frame">

<div id="container">
<div id="logo"><img alt="Stalked by the state" src="images/sbts.jpg" onclick="location.assign('.');"></div>
<div id="top-header">
	
	<img alt="Stalked by the state" src="images/sbts-title.jpg">
	
	<div id="head-bar">
		<div class="button status" onclick="location.assign('systemstatus');">System Status</div>
		<div class="button status" onclick="location.assign('systemlog');">System Log</div>
		<div class="button status" onclick="location.assign('rfxcomlog');">RFXCOM Log</div>
		<div class="button status" onclick="location.assign('phidgetlog');">Phidget Log</div>
		<div class="button status" onclick="location.assign('actionlog');">Action Log</div>
	</div>
	
	<div class="hr" style="padding-top:10px;"></div>
</div>

<div id="main">

<div id="menus">

<div id="mm_home" class="menu-header" onclick="location.assign('./');">Home</div>
<div id="mm_actions" class="menu-header" onclick="location.assign('actions');">Actions</div>
<div id="mm_schedules" class="menu-header" onclick="location.assign('schedules');">Scheduled Actions</div>
<div id="mm_profiles" class="menu-header" onclick="location.assign('profiles');">Profiles</div>
<div id="mm_buttongroups" class="menu-header" onclick="location.assign('buttongroups');">Button Groups</div>

<div id="mm_cameras" class="menu-header" onclick="location.assign('cameras');">Cameras</div>
<div id="mm_rfxcom" class="menu-header" onclick="location.assign('rfxcom');">RFXCOM</div>
<div id="mm_phidgets" class="menu-header" onclick="location.assign('phidgets');">Phidgets</div>
<div id="mm_freaks" class="menu-header" onclick="location.assign('freaks');">Linked Freaks</div>

<div id="mm_synthetics" class="menu-header" onclick="location.assign('synthetics');">Combination Events</div>
<div id="mm_watchdogs" class="menu-header" onclick="location.assign('watchdogs');">Watchdogs</div>


<div id="mm_email" class="menu-header" onclick="location.assign('email');">E-mail</div>

<div id="mm_maintenance" class="menu-header" onclick="location.assign('maintenance');">Maintenance</div>
<div id="mm_system" class="menu-header" onclick="location.assign('system');">System</div>
<div id="mm_users" class="menu-header" onclick="location.assign('users');">Users</div>
<div id="mm_links" class="menu-header" onclick="location.assign('links');">Links</div>

</div>

<script type="text/javascript">
	/**
		This script should be above all other scripts.
	*/
	
	// set to "false" to suppress console messages generated by the configuration
	Debug = true;
</script>

<div id="content">
