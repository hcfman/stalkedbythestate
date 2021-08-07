<h1>Reference Manual</h1>

<ol class="toc">
	<li><a href="#system">System</a></li>
	<li><a href="#users">Users</a></li>
	<li><a href="#cameras">Cameras</a></li>
	<li><a href="#phidgets">Phidgets</a></li>
	<li><a href="#freaks">Linked Freaks</a></li>
	<li><a href="#actions">Actions</a>
		<ol>
			<li><a href="#actions-video">Video</a></li>
			<li><a href="#actions-email">E-mail</a></li>
			<li><a href="#actions-add-tag">Add Tag</a></li>
			<li><a href="#actions-delete-tag">Delete Tag</a></li>
			<li><a href="#actions-send-http">Send HTTP</a></li>
			<li><a href="#actions-remote-video">Remote Video</a></li>
			<li><a href="#actions-cancel-action">Cancel Action</a></li>
			<li><a href="#actions-web-prefix">Web Prefix</a></li>
			<li><a href="#actions-phidget-output">Phidget Output</a></li>
		</ol></li>

	<li><a href="#rfxcom">RFXCOM</a>
		<ol>
			<li><a href="#rfxcom-generic-input">Generic Input</a></li>
			<li><a href="#rfxcom-generic-output">Generic Output</a></li>
			<li><a href="#rfxcom-packet-structure">Packet Structure</a></li>
		</ol>
	<li><a href="#scheduled">Scheduled Actions</a></li>
	<li><a href="#combination">Combination Events</a></li>
	<li><a href="#buttons">Button Groups</a></li>
	<li><a href="#email">E-mail</a></li>
	<li><a href="#profiles">Profiles</a></li>
	<li><a href="#maintenance">Maintenance</a></li>
	<li><a href="#links">Links</a></li>
</ol>

<div id="manual-content">

	<a name="system"></a>
	<h2>System</h2>

	<div class="white">
		<h4>Time &amp; Date</h4>
		<dl>
			<dt>Manual time</dt>
			<dd>Set the timezone, date and time yourself. (We recommend
				setting it to automatic as this the maintains the accuracy of the
				clock to a high accuracy, i.e. it synchronizes to an NTP server)</dd>
			<dt>Timezone</dt>
			<dd>Set the timezone. Select the closest match to your time
				zone.</dd>
			<dt>Date</dt>
			<dd>Set the date. (Set manually first, save. Then switch to
				automatic, save.)</dd>
			<dt>Time</dt>
			<dd>Set the time. (Set manually first, save. Then switch to
				automatic, save.)</dd>
			<dt></dt>
			<dd></dd>
			<dt>Automatic</dt>
			<dd>Configure date automatically.</dd>
			<dt>NTP server</dt>
			<dd>Set the NTP server.</dd>
		</dl>

		<h4>Network</h4>
		<dl>
			<dt>Use DHCP</dt>
			<dd>Enables DHCP (Recommended so as not to lock yourself out if
				you forget your IP address for example. There is no hardware reset
				switch to reset to factory settings).</dd>
			<dt>Hostname</dt>
			<dd>Set the hostname.</dd>
			<dt>Address</dt>
			<dd>Set the IP address.</dd>
			<dt>Mask</dt>
			<dd>Set the mask.</dd>
			<dt>Router</dt>
			<dd>Set router address. (Or default route)</dd>
			<dt></dt>
			<dd></dd>
			<dt>Nameserver #1</dt>
			<dd>Set the first nameserver address if not using DHCP</dd>
			<dt>Nameserver #2</dt>
			<dd>Set the second nameserver address if present and not using
				DHCP</dd>
			<dt>Nameserver #3</dt>
			<dd>Set the third nameserver address if present and not using
				DHCP</dd>
			<dt></dt>
			<dd></dd>
			<dt>Protocol</dt>
			<dd>Set the protocols the system can use. i.e. either HTTP or
				HTTPS. If you intend to use HTTPS, then you should go to the System
				page and then click on Certificate management at the bottom and
				regenerate a certificate as by default they all ship with the same
				certificate</dd>
			<dt>HTTP port</dt>
			<dd>Set the HTTP port (Must be > 1024)</dd>
			<dt>HTTPS port</dt>
			<dd>Set the HTTPS port (Must be > 1024)</dd>
		</dl>

		<h4>Preferences</h4>
		<dl>
			<dt>Web Prefix</dt>
			<dd>The prefix to use in urls. By default this is set to
				nothing, which will work if you click on a video from a browser to
				the SBTS itself but not if this url is present in an SMS alert or
				E-mail alert. In order for the link to work within an SMS or E-mail
				you must set a webprefix value. The webprefix should be the starting
				part of the url including the port number and not ending in a
				trailing slash, for example http://192.168.1.1:8080. The webprefix
				can also be set as action. This allows you to make a button that
				changes the webprefix in the event that you have to access your SBTS
				by a different URL when you are outside of your network versus
				internal to your network.</dd>
			<dt>X10 Delay</dt>
			<dd>Set the X10 delay. This is the time in seconds that must
				expire before the X10 controller will trigger an event from the same
				device. Typically it is set to 60 seconds. You should tie this value
				to the amount of time in seconds that your video clips will be. If
				you set this to a quicker values, say 30 seconds. Then you probably
				don't want to record more than 30 seconds in a row or the video
				clips will overlap in time</dd>
			<dt>Connect Timeout</dt>
			<dd>This is the time in milliseconds after which a connection to
				a camera will timeout. If you are using wifi cameras connection can
				often take a while and you might need to increase this value.</dd>
			<dt>Free Space</dt>
			<dd>Set the amount of free space in megabytes to preserve on the
				disk. You should set this to some largish value, such as 500. The
				way this works is that whenever the disk cleaner program fires it
				deletes video until the amount of free space is greater than or
				equal to the amount you select here. Leaving 500MB or example is a
				good choice because it is unlikely that the video amount between
				clean cycles will be greater than 500MB</dd>
			<dt>Days of JPEG</dt>
			<dd>Set the number of days you want JPEG files to be preserved.
				Note, JPEG files will take about 10x the size of the equivalent WebM
				video. So you can save more video events by letting the system keep
				more WebM video than MJPG video. However.. MJPG video is more
				convenient for viewing on a smart phone so you want to make sure
				that the events you are likely to wish to view on the smart phone
				are present. If you have a 32GB disk then you will probably want to
				set this value to something like "5" for 5 days of mjpg, after which
				if it needs space it will delete MJPG images.</dd>
			<dt>Clean Rate</dt>
			<dd>Set rate at which the system cleans the filesystem to free
				up space in minutes. Usually set to something like 5 for every 5
				minutes.</dd>
			<dt>Phone Home Url</dt>
			<dd>Set the url to phone home to.</dd>
		</dl>

		<h4>Certificates</h4>
		<dl>
			<dt>...</dt>
			<dd>...</dd>
		</dl>
	</div>


	<a name="users"></a>
	<h2>Users</h2>
	<div class="white">
		<p>Manage your users here.</p>
		<p>
			<em>The "admin" user cannot be deleted!</em>
		</p>

		<dl>
			<dt>Name</dt>
			<dd>Set the username.</dd>
			<dt>Password</dt>
			<dd>Set the password for the user.</dd>
			<dt>Role</dt>
			<dd>Set the user role. Only an admin user can perform admin
				functions and even view the menu system. A guest account can trigger
				guest actions or guest enabled cameras.</dd>
		</dl>

		<p>
			<em>Saving the users will result in a system restart.</em>
		</p>
	</div>


	<a name="cameras"></a>
	<h2>Cameras</h2>
	<div class="white">
		<p>Add cameras to the system. Note: You should configure each
			video stream on your camera to send at 5 frames per second. We
			recommend setting up two video streams per camera. One hi resolution
			one for identification and one low resolution one so that you can
			view it quickly on a smartphone for situational awareness or to
			identify activity</p>

		<dl>
			<dt>Name</dt>
			<dd>Name of the camera.</dd>
			<dt>Description</dt>
			<dd>Description of the camera.</dd>
			<dt>Protocol</dt>
			<dd>Protocol to use. (HTTP or HTTPS). Note if you select HTTPS
				then you must select "Validate host" or not depending on whether the
				system should enforce this or not</dd>
			<dt>URL</dt>
			<dd>The url to the video stream.</dd>
			<dt>Username</dt>
			<dd>Username for camera access.</dd>
			<dt>Password</dt>
			<dd>Password for camera access.</dd>
			<dt>Continue seconds</dt>
			<dd>Amount of seconds to capture from the video stream since
				event fired. This typically not be higher that the time in which the
				event can reoccur. Such as 60 seconds (Default for X10 events)</dd>
			<dt>Buffer seconds</dt>
			<dd>Amount of seconds to capture from the video stream before
				event was fired. This allows you to ensure that you can see what
				triggered the event</dd>
			<dt>Priority</dt>
			<dd>Queue priority. "1" has the highest priority. This
				determines which camera to encode to WebM first if there is a
				choice. Note: Newer values always encode before older</dd>
			<dt>Caching allowed</dt>
			<dd>Allow caching or not. This is only present because in some
				cameras a certain HTTP 1.1 value (Pragma: no-cache) was found to
				cause the camera not to function. Selecting caching as true will not
				use this Header value</dd>
			<dt>Guest</dt>
			<dd>Allow guest access.</dd>
		</dl>
	</div>


	<a name="phidgets"></a>
	<h2>Phidgets</h2>
	<div class="white">
		<p>Introduce phidgets to the system.</p>

		<dl>
			<dt>Name</dt>
			<dd>Name of the phidget.</dd>
			<dt>Description</dt>
			<dd>Description of the phidget.</dd>
			<dt>SerialNumber</dt>
			<dd>The serialnumber of the phidget. (Found on a sticker on the
				back of the board)</dd>
			<dt>Ports</dt>
			<dd>Which port to trigger. (0-7)</dd>

			<dl>
				<dt>On/off initial input state</dt>
				<dd>Initial input state. This determines what is an On trigger
					and an Off trigger. (On is the opposite to the initial state).
					Mostly this should be set to "Checked" as you usually trigger by
					pulling the value low</dd>
				<dt>On/off initial output state</dt>
				<dd>Initial output state. The Phidget is set to this state upon
					startup. In you are pulsing this output then after a pulse it comes
					back to the initial state. (So a pulse train should be an odd
					number of numbers in the comma separated list)</dd>
				<dt>OnTrigger</dt>
				<dd>Trigger the message on. For an input, generate an event
					with this name when it's triggered.</dd>
				<dt>OffTrigger</dt>
				<dd>Trigger the message off. For an input, generate an event
					with this name when it's triggered.</dd>
			</dl>
		</dl>
	</div>


	<a name="freaks"></a>
	<h2>Linked Freaks</h2>
	<div class="white">
		<p>Introduce other freaks to the system.</p>

		<dl>
			<dt>Name</dt>
			<dd>Name of the freak.</dd>
			<dt>Description</dt>
			<dd>Description of the freak.</dd>
			<dt>Protocol</dt>
			<dd>Protocol to use. (HTTP or HTTPS)</dd>
			<dt>Hostname</dt>
			<dd>The hostname of this freak.</dd>
			<dt>Port</dt>
			<dd>On which port to access the freak.</dd>
			<dt>Username</dt>
			<dd>Username for freak access.</dd>
			<dt>Password</dt>
			<dd>Password for freak access.</dd>
			<dt>Guest</dt>
			<dd>You only have guest access to this freak if set to guest</dd>
		</dl>
	</div>


	<a name="x10"></a>
	<h2>X10</h2>
	<div class="white">
		<p>Introduce X10 devices to the system.</p>

		<dl>
			<dt>Name</dt>
			<dd>Name of the device.</dd>
			<dt>Description</dt>
			<dd>Description of the device.</dd>
			<dt>Type</dt>
			<dd>What type of action should the device perform. Either it's
				used to turn things on or off, or it is a PIR sensor or an Alarm.</dd>
			<dt>HouseCode</dt>
			<dd>Which housecode to use for this device. (A-M)</dd>
			<dt>UnitCode</dt>
			<dd>Which unitcode to use for this device.(1-15)</dd>
		</dl>
	</div>


	<a name="actions"></a>
	<h2>Actions</h2>
	<div class="white">
		<p>Add actions to the system.</p>

		<h4>Shared action properties</h4>
		<p>These properties are available for all action types.</p>
		<dl>
			<dt>Name</dt>
			<dd>Name of the device.</dd>
			<dt>Description</dt>
			<dd>Description of the device.</dd>
			<dt>Allow guest</dt>
			<dd>This action may be executed by an event that a guest freak
				triggered</dd>
			<dt>Start delay</dt>
			<dd>This causes the start of this action to be delayed by the
				specified number of seconds if triggered.</dd>
			<dt>Delay between events</dt>
			<dd>Don't trigger this event more than 1 time in this amount of
				seconds</dd>
			<dt>Time</dt>
			<dd>Add a time constraint. It's better to use profiles, but if
				you like you can add a time constraint to an individual action. The
				time specified in the constraint must then match for the action to
				be executed. Note: These are very flexible and you can add multiple
				time range components to each constraint. And indeed, multiple
				constraints to each action.</dd>
			<dt>Counter</dt>
			<dd>A counter contains a count and a time in seconds. If you add
				a counter then the action will be executed only if it is triggered
				count times within the time period specified. Note, in order to
				trigger the counter the action must pass all of the other
				constraints that apply for the action first.</dd>
			<dt>Profiles</dt>
			<dd>If there are profiles selected for this action, then this
				action will only be executed of the state that the time constraint
				specified by the profile is currently active.</dd>
			<dt>Required tags</dt>
			<dd>If there are any values selected in this list, then at least
				one of them must be present (Tags must be active) for this action to
				be allowed to execute. Note: A disallowed tag overrides a required
				tag. If the mode beside the field is set to And, then all of the tags selected must be present</dd>
			<dt>Disallowed tags</dt>
			<dd>If there are any tags specified in the list, then none of
				the tags or profiles that are associated with the tag must be active
				at this time for the action to be executed. Note: By using these
				values in combination with other actions that add tags or pull them
				it's possible to specify very special behaviour. Such as only
				triggering an event if someone walks past a row of sensors from a
				specific direction. See the website for more examples.</dd>
		</dl>

		<p>
			<em>See below which properties are associated with what action
				type.</em>
		</p>

		<hr>

		<a name="actions-video"></a>
		<h3>Video</h3>
		<p>Record video from a camera's stream when the event is
			triggered.</p>
		<dl>
			<dt>List of cameras</dt>
			<dd>Select which cameras will be triggered by the event</dd>
		</dl>

		<a name="actions-email"></a>
		<h3>E-mail</h3>
		<p>Send an E-mail alert. If there are cameras selected, then
			include links to the video that is associated with this event</p>
		<dl>
			<dt>List of cameras</dt>
			<dd>Select which cameras links to include in the E-mail</dd>
			<dt>To</dt>
			<dd>The address to send the E-mail to</dd>
			<dt>Video Type</dt>
			<dd>Which type of video to link to in the message (We recommend
				MJPG since it's available very quickly). use PJPG (Pseudo JPG, just
				a name we made up) for phones that do not support MJPG, such as
				Android phones, or some very old phones. Video from this is
				simulated and uses JavaScript to emulate MJPG</dd>
		</dl>

		<a name="actions-x10"></a>
		<h3>X10</h3>
		<p>Send signals through X10 when an event is triggered.</p>
		<dl>
			<dt>House Code</dt>
			<dd>House code of the device.</dd>
			<dt>Unit Code</dt>
			<dd>Unit code of the device.</dd>
			<dt>Direction</dt>
			<dd>Either On or Off for an X10 appliance. You can specify Dim or Bright
				for a lamp module and it sends a 30% brightness decrease or increase.</dd>
		</dl>

		<a name="actions-add-tag"></a>
		<h3>Add Tag</h3>
		<p>Adds tag.</p>
		<dl>
			<dt>TagName</dt>
			<dd>Name of the tag to add. Tags can be viewed as "state" which
				affects whether or not other actions will be executed</dd>
			<dt>ValidFor</dt>
			<dd>Determines how for long the tag is valid in seconds</dd>
		</dl>

		<a name="actions-delete-tag"></a>
		<h3>Delete Tag</h3>
		<p>Deletes a tag.</p>
		<dl>
			<dt>List of tags</dt>
			<dd>Delete the tag selected from the currently active list. Can
				be used to cancel state. Such as "I'm home now"</dd>
		</dl>

		<a name="actions-send-http"></a>
		<h3>Send HTTP</h3>
		<p>Sends a HTTP request when an event is triggered.</p>
		<dl>
			<dt>Url</dt>
			<dd>URL of that location you want to send the request to</dd>
			<dt>Method type</dt>
			<dd>GET or POST operation</dd>
			<dt>Username</dt>
			<dd>If authentication is needed</dd>
			<dt>Password</dt>
			<dd>Password for authentication</dd>
			<dt>Parameters</dt>
			<dd>Key/Value pairs to be send along to the URL</dd>
		</dl>

		<a name="actions-remote-video"></a>
		<h3>Remote Video</h3>
		<p>Triggers remote video</p>
		<dl>
			<dt>List of remote cameras</dt>
			<dd>Trigger these cameras. Either you have admin access and you
				can trigger any cameras, or you have guest access and you can
				trigger cameras which have guest associated with them.</dd>
			<dt>Freak Name</dt>
			<dd>Select from the list. Must be defined first</dd>
		</dl>

		<a name="actions-cancel-action"></a>
		<h3>Cancel Action</h3>
		<p>Cancels an action.</p>
		<dl>
			<dt>Action Name</dt>
			<dd>The action to cancel. This cancels actions which have been
				set to have a delayed start. For example a panic button may delay
				the start of a certain flood light. If you want to cancel the panic
				action you should associated a delete action event with your panic
				off button and any delayed start actions will be cancelled.</dd>
		</dl>

		<a name="actions-web-prefix"></a>
		<h3>Web Prefix</h3>
		<p>Change the current webprefix, which affects the prefix inserted
			in E-mail alerts.</p>
		<dl>
			<dt>Prefix</dt>
			<dd>e.g. http://www.mydomain.com:8080</dd>
		</dl>

		<a name="actions-phidget-output"></a>
		<h3>Phidget Output</h3>
		<p>Outputs to a phidget when the event fires.</p>
		<dl>
			<dt>Phidget Name</dt>
			<dd>Name of the phidget.</dd>
			<dt>Phidget Action Type</dt>
			<dd>Type of phidget action.</dd>
			<dt>Port</dt>
			<dd>The phidgets port to receive.</dd>
			<dt>PulseTrain</dt>
			<dd>This has the form of a comma-separated list of numbers. Each
				number represents a time in milliseconds to hold that state for. A
				pulse train starts in the setup Initial output state and flip flops
				state for the time specified in sequence until it's finished and
				then sets it back to the initial output state. For example
				1000,500,1000 will flip for one second, then flop for 1/2 second
				then flip again for one second, then go back to the initial output
				state.</dd>
		</dl>

	</div>

	<a name="rfxcom"></a>
	<h2>RFXCOM</h2>
	<div class="white">
		<p>Add RFXCOM generic input and output descriptors to the system</p>

		<a name="rfxcom-generic-input"></a>
		<h3>Generic Input</h3>
		<p>This defines an input event that comes from the rfxcom device.
			It's generic because you define the bytes in the packet itself. This
			does require some knowledge of the protocol. It's best to reference
			the API manual obtainable from RFXCOM themselves. We provide details
			of some of the more common packets here.</p>

		<dl>
			<dt>Name</dt>
			<dd>The identifier of the input event. Must be unique</dd>
			<dt>Description</dt>
			<dd>for your reference. A description of the event</dd>
			<dt>Type</dt>
			<dd>GENERIC INPUT</dd>
			<dt>Event Name</dt>
			<dd>The name of the event how it will appear in the drop down
				selector for use in triggering actions</dd>
			<dt>Delay between events</dt>
			<dd>If another event arrives before this time is up it is
				ignored. Stops event arriving quicker than is useful</dd>
			<dt>Add Row</dt>
			<dd>This button adds another row to the packet description/ To
				delete a row, click on the "-" sign that appears to the left of the
				row</dd>
			<dt>Row</dt>
			<dd>
				Each row contains fields as follows
				<dl>
					<dt>Offset</dt>
					<dd>This is the offset into the packet. Note: the first byte
						in the packet is the packet length. This is ommited in the
						descriptor as it is implied.</dd>
					<dt>Mask</dt>
					<dd>This defines which bits of the byte is relevant to the
						comparison. It's expressed in based 16 (hex). For example, a mask
						of "FF" implies that all of the bits of the byte must match. A
						mask value of "00" means ignore the value of this byte. The 4th
						byte of the packet is a sequence number, for matching events you
						should ignore this byte by setting the mask to "FF" and setting
						the value to "00" (Note this is the 3rd byte in the list, index
						value 2)</dd>
					<dt>Operator</dt>
					<dd>In most cases you would set this simply to "=" as you are
						matching the type of identifier of a command. However, there are
						cases where it is useful to specify a relational operation other
						than equals. Specifically if matching a temperature range, of the
						transition below or above a temperature. Temperature sensors
						specify the temperature in parts of a byte in the packet</dd>
					<dt>Value1, Value1</dt>
					<dd>This is Value1 for all operators other than "Range" where
						a second value comes into play</dd>
				</dl>
			</dd>
		</dl>

		<a name="rfxcom-generic-output"></a>
		<h3>Generic Output</h3>
		<p>This defines an output event that comes from the rfxcom device.
			It's generic because you define the bytes in the packet itself. This
			does require some knowledge of the protocol. It's best to reference
			the API manual obtainable from RFXCOM themselves. We provide details
			of some of the more common packets here. An output event is where you
			are turning something on, for example, a light or ringing a door bell</p>

		<dl>
			<dt>Name</dt>
			<dd>The identifier of the input event. Must be unique</dd>
			<dt>Description</dt>
			<dd>for your reference. A description of the event</dd>
			<dt>Type</dt>
			<dd>GENERIC OUTPUT</dd>
			<dt>Add Row</dt>
			<dd>This button adds another row to the packet description/ To
				delete a row, click on the "-" sign that appears to the left of the
				row</dd>
			<dt>Row</dt>
			<dd>
				Each row contains fields as follows
				<dl>
					<dt>Offset</dt>
					<dd>This is the offset into the packet. Note: the first byte
						in the packet is the packet length. This is ommited in the
						descriptor as it is implied.</dd>
					<dt>Value</dt>
					<dd>This is the value in hex of the byte in the packet you are
						sending. Again, the length byte is ommitted.</dd>
				</dl>
			</dd>
		</dl>

		<a name="rfxcom-packet-structure"></a>
		<h3>Packet Structure</h3>

		<p>The rfxtrx433 device is a very convenient device as it
			abstracts all the different types of home automation devices into a
			single common packet structure, making it possible and easy to define
			a generic approach to handling these devices. With the GENERIC INPUT
			and GENERIC OUTPUT descriptors Stalked by the state can receive events
			from and control any new device that may be supported by the
			Rfxtrx433 device in the present and in the future. We will later be
			adding convenience widgets to make it easier to add input event
			definitions and define output events, which will be available as
			software updates, but currently you will be unrestricted by using the
			generic descriptors.</p>

		<p>An rfxtrx433 packet always has the following structure</p>

		<dl>
			<dt>Length byte</dt>
			<dd>The first byte in the packet defines how many bytes will
				follow. It is not necessary to specify this byte when defining input
				event or output descriptors but it is listed in the RFXCOM logs.</dd>
			<dt>Type</dt>
			<dd>The second byte in the packet is the "Type" byte. This
				represents the "class" of the packet. Similar classes are grouped
				together with a common "Type" byte.</dd>
			<dt>Sub Type</dt>
			<dd>The sub type defines the "specific" type of product.</dd>

			<dt>Seq nbr</dt>
			<dd>This is a number that a sender may increment to keep track
				of the outgoing actions. Outgoing actions will be acknowledged with
				the same sequence number, so it's possible to determine how many
				commands are still outstanding so as not to overflow the buffer to
				the Rfxtrx433 device. When using Stalked by the state, we ignore this
				byte as the typical sort of usages are not likely to result in
				buffer overflow, so it's ok to always set this to "00" for example.</dd>
			<dt>Data bytes</dt>
			<dd>The remainer of the bytes are data to the packet. Typically
				the device identifier bytes will be present here, as well as the
				specific type of command (on, off, dim value etc), and on packets
				that are received, the final byte usually also contains values that
				represent the signal strength of the received packet (And hence can
				be ignored when matching with input events).</dd>
		</dl>

		<h4>Sample packet structures</h4>

		<dl>
			<dt>X10</dt>
			<dd>
				<table class="packetDescriptor" cellpadding="10" cellspacing="0">
					<tbody>
						<tr>
							<td>Length</td>
							<td>07</td>
						</tr>
						<tr>
							<td>Type</td>
							<td>10</td>
						</tr>
						<tr>
							<td>Sub Type</td>
							<td>00</td>
						</tr>
						<tr>
							<td>Seq Nbr</td>
							<td>00-FF</td>
						</tr>
						<tr>
							<td>House Code</td>
							<td>41-50 (A-P)</td>
						</tr>
						<tr>
							<td>Unit Code</td>
							<td>01-10 (Unit code 1-16)</td>
						</tr>
						<tr>
							<td>Command</td>
							<td>00=Off, 01=On, 02=Dim, 03=Bright, 05=All/Group off,
								06=All/Group on</td>
						</tr>
						<tr>
							<td>Signal Strength</td>
							<td>00-FF</td>
						</tr>
					</tbody>
				</table>
			</dd>
			<dt>Klikaanklikuit, Home Easy UK, NEXA, CHACON (AC)</dt>
			<dd>
				<table class="packetDescriptor" cellpadding="10" cellspacing="0">
					<tbody>
						<tr>
							<td>Length</td>
							<td>0B</td>
						</tr>
						<tr>
							<td>Type</td>
							<td>11</td>
						</tr>
						<tr>
							<td>Sub Type</td>
							<td>00</td>
						</tr>
						<tr>
							<td>Seq Nbr</td>
							<td>00-FF</td>
						</tr>
						<tr>
							<td>ID1</td>
							<td>00-03</td>
						</tr>
						<tr>
							<td>ID2</td>
							<td>00-FF</td>
						</tr>
						<tr>
							<td>ID3</td>
							<td>00-FF</td>
						</tr>
						<tr>
							<td>ID4</td>
							<td>01-FF</td>
						</tr>
						<tr>
							<td>Unit Code</td>
							<td>01-10 (Units 1-16)</td>
						</tr>
						<tr>
							<td>Command</td>
							<td>00=Off, 01=On, 02=Set level, 03=Group off, 04=Group on,
								05=Set group level</td>
						</tr>
						<tr>
							<td>Level</td>
							<td>00-FF (0% - 100%)</td>
						</tr>
						<tr>
							<td>Signal Strength</td>
							<td>00-FF</td>
						</tr>
					</tbody>
				</table>
			</dd>
		</dl>

		<p>For further packet structure details, please request the SDK
			from rfxcom.</p>

	</div>

	<a name="scheduled"></a>
	<h2>Scheduled Actions</h2>
	<div class="white">
		<p>Schedule action to execute on a certain day and time.</p>
		<dl>
			<dt>Name</dt>
			<dd>Name of the schedule</dd>
			<dt>Event Name</dt>
			<dd>Name of the event to trigger.</dd>
			<dt>Guest</dt>
			<dd>Allow guest access.</dd>
			<dt>Days of the week</dt>
			<dd>On which days should the event trigger. (Scheduled actions
				are weekly)</dd>
			<dt>Time</dt>
			<dd>On what time should the event trigger.</dd>
		</dl>
	</div>


	<a name="combination"></a>
	<h2>Combination Events</h2>
	<div class="white">
		<p>This defines "synthetic" events. Or events that will be
			triggered when all of the real ones selected from the list occur
			within the specified number of seconds.</p>
		<dl>
			<dt>Resulting event</dt>
			<dd>Name of the resulting event</dd>
			<dt>Within seconds</dt>
			<dd>All the selected events must have triggered within this
				amount of seconds</dd>
			<dt>Trigger events</dt>
			<dd>Combine these selected events.</dd>
		</dl>
	</div>


	<a name="buttons"></a>
	<h2>Button Groups</h2>
	<div class="white">
		<p>Define pages of smartphone buttons</p>
		<dl>
			<dt>Button groups</dt>
			<dd>Name of the group. (Used in the url to specify the page)</dd>
			<dl>
				<dt>Group name</dt>
				<dd>The name of the group. Choose a name that represents the
					kind of buttons you have in your group. For example "Panic" or
					"Lights"</dd>
				<dt>Url</dt>
				<dd>The Url that you should use to access the page</dd>
			</dl>
			<dt></dt>
			<dd></dd>
			<dt>Buttons</dt>
			<dd>A button that triggers an event when pushed.</dd>
			<dl>
				<dt>Name</dt>
				<dd>Name of this button. Also the name of the event that
					pushing it triggers</dd>
				<dt>Description</dt>
				<dd>Description of this button. For your convenience.</dd>
				<dt>Guest</dt>
				<dd>Allow guest access. Only buttons with this selected can be
					triggered by a guest freak.</dd>
			</dl>
		</dl>
	</div>


	<a name="email"></a>
	<h2>E-mail</h2>
	<div class="white">
		<p>Setup your E-mail for messaging.</p>
		<dl>
			<dt>Name</dt>
			<dd>Name of this E-mail provider (Convenience)</dd>
			<dt>Description</dt>
			<dd>Description of this E-mail provider (Convenience)</dd>
			<dt>Mailhost</dt>
			<dd>Hostname of the mail server you use</dd>
			<dt>Port</dt>
			<dd>Usually set to 25, or 587 for authenticated mail</dd>
			<dt>Username</dt>
			<dd>Specifies password when sending mail requires
				authentication. Note: It doesn't support encrypted sending, only
				authenticated</dd>
			<dt>Password</dt>
			<dd>Password for above username</dd>
		</dl>
	</div>


	<a name="profiles"></a>
	<h2>Profiles</h2>
	<div class="white">
		<p>This are named time-of-day constraints.</p>
		<dl>
			<dt>Name</dt>
			<dd>Name of your profile. Must be unique</dd>
			<dt>Description</dt>
			<dd>Description of this profile
			</dt>
			<dt>Tagname</dt>
			<dd>This is the tag name that will appear in the profiles list
				in the Advanced section of an action. Define the time range once
				here and use it in many actions. For example, define "Night time"
				and then specify certain actions to only occur at night, such as
				some SMSes.</dd>
			<dt>Time</dt>
			<dd>Specify the time constraint. Note: You may have several
				range sections.</dd>
		</dl>
	</div>


	<a name="maintenance"></a>
	<h2>Maintenance</h2>
	<div class="white">
		<p>Reboot and format functions</p>
		<dl>
			<dt>Reboot</dt>
			<dd>Reboots the freak.</dd>
			<dt>Format disk</dt>
			<dd>Formats the disk inserted ready for use as video capture.</dd>
		</dl>
	</div>


	<a name="links"></a>
	<h2>Links</h2>
	<div class="white">
		<h3>Main settings</h3>

		<p>Very flexible link configuration utility. Create custom "Views"
			on your cameras that include such things as a selection of the
			cameras. Time of day that you want to see video events for for this
			view. Date ranges (Usually left blank, but can specify "today" for
			just todays events instead of the last 24 hours) or even simple
			patterns that must match eventnames (Only the character "*" is
			special and matches anything). Event names can be a list specified
			one pattern per line. For example you could match *door* (Pattern on
			one line) and *back* for events out the back.</p>

		<p>The video type of the link can be selected. i.e. MPJG for
			iPhones and other phones that support MJPG, or PJPG for Android and
			phones that don't support MJPG. WebM if viewing on a browser on the
			web (Or from Gingerbread onwards Android phones).</p>

		<h3>Clustering</h3>
		<p>Note. The camera list will automatically display the list of
			cameras that you have at your disposal from co-operating freaks that
			you cluster with. The resulting link when then be a merge of all of
			your local cameras that you selected and any cameras that are
			triggered from remote freaks. Note: if an event triggered both a
			local camera and a remote one, then the merged list will show the
			local and remote cameras on the same line in the list.</p>

		<h3>Finally</h3>

		CLick on "Save" and save the link to your list and then book mark the
		link created for your convenience.
	</div>

</div>