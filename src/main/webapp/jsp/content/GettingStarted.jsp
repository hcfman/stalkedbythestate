<h1>Getting Started</h1>

<ol class="toc">
	<li><a href="#how-it-works">How it works</a></li>
	<li><a href="#add-your-devices">Add your devices</a></li>
	<li><a href="#your-first-button">Your first button</a></li>
	<li><a href="#create-an-action">Create an action for your first button</a></li>
	<li><a href="#turning-a-light-on">Create an action to turn a light on</a></li>
	<li><a href="#sending-video-and-an-sms">Sending video and an SMS in response to an X10 PIR event</a></li>
</ol>

<div id="getting-started">

	<a name="how-it-works"></a>
	<h2>How it works</h2>

	<div class="white">
		<p>Your StalkedByTheState works by process input events triggered by devices such as X10 PIR sensors (by means of a CM15PRO x10 controller),
		PIR sensors from other manufacturers (by means of a "Phidget interface card), user-defined
		buttons from your smart phone, other StalkedByTheStates (SBTS's) and by scheduled calendar events. The events all have a name associated with them that you
		associated with the device when you set it up. This name is then selected from a drop down menu in the actions menu associated with an output action</p>
		
		<p>
		Possible input actions include:
		
		<ul>
		<li>X10 PIR events</li>
		<li>Input events from a <a href="http://www.phidgets.com/">Phidgets</a> I/O card (<a href="http://www.phidgets.com/">1018 - PhidgetInterfaceKit 8/8/8</a>)</li>
		<li>Used defined buttons created in a button group (A page of buttons) from the Button Groups page</li>
		<li>Other SBTS's (They connect to this SBTS via the url /sbts/net?event=eventName or /sbts/guest/net?event=eventName)</li>
		<li>Calendar scheduled events from the Schedule Actions page</li>
		</ul>
		</p>
		
		<p>
		Possible event response actions include:
		
		<ul>
		<li>Trigger video capture</li>
		<li>E-mail alert</li>
		<li>Add a tag to the current state (User defined name, the presence or absense of a tag can control whether an action is actioned or not)</li>
		<li>Delete a tag<li>
		<li>Call out to an external system with custom parameters (http gateway)</li>
		<li>Trigger video on another SBTS (Remote video)</li>
		<li>Cancel a previously actioned, but delayed action (By it's name)<li>
		<li>Set the webprefix, which is the prefix required to reach the device (May be different in internal to your network versus external to your network for example)</li>
		<li>Set an output port on the Phidget to either on, off or send it a custom shaped pulse train</li>
		</ul>
	</div>
	
	<a name="add-your-devices"></a>
	<h2>Add your devices</h2>
	
	<div class="white">
	
	<h3>X10 Controller</h3>
	
	<p>
	If you have a CM15PRO X10 Controller, first set the house code that it will receive events on with the software that came with the controller then simply plug it
	into your SBTS. Then click on the "System Status" page and it should show up green to indicate that it is connected. It should then be ready to turn on/off devices
	on the house code that you setup with it's own software.
	</p>
	
	<h3>sd disk</h3>
	
	<p>
	If you intend to record video, then you will need a sd-disk. Note: You insert this disk upside down into the SBTS. i.e. if you are looking down onto the SBTS's logo then you insert
	the card such that the gold contacts are facing up towards you. This is possibly the opposite to your intuition. Click on the "System Status" and it should show you that
	the disk is present but it's not formatted for SBTS. Go to the Maintenance page now and press format. Note: This can take a very long time in the case of a large
	capacity sd disk (Possibly more than an hour). All the time the progress bar will not move, be patient and wait till it to finish. Note: You should choose the very
	fastest sd cards otherwise (Depending on how many cameras you have or the video size you are recording) it may not be able to keep up. When it's finished you should
	be able to see it's System Status as green and view the format log.
	</p>
	
	<h3>Cameras</h3>
	
	<p>
	If you have a camera, plug in the camera and determine the url that you want to record the video stream from. This will be in the camera's manual, or sometimes you
	can click on "View image" in the browser and see the url. It is often something like /video.mjpg. Set whether or not you want a guest SBTS to be able to view it and
	define the recording parameters.
	</p>
	
	<h3>Phidgets</h3>
	
	<p>
	If you have a phidget and an X10-Controller you will need to add a USB hub as there is only one USB connector. Define the phidget with it's serial number which is printed
	on a sticker on the back of it. Then plugin the phidget and it should show up as green on the System Status page.
	</p>
	
	</div>

	<a name="your-first-button"></a>
	<h2>Your first button</h2>
	
	<h3>Create a button group</h3>
	
	<div class="white">
	<p>
	Go to the Button Groups menu page and click on "Add Button Group". This will define a group of buttons (Buttons that display together on a single page). Within
	this group add a few buttons. The name you give to a button is used as the name of the event in the drop down menu when creating actions. The description field is
	for your information only. Select whether or not a guest freak is allowed to use this button (A guest freak is defined as an SBTS whose rights are selected as "guest"
	when defining the user account that the guest freak will use). Finally, save all of the editing with the Save button on the page. The Url field shows you a link that
	you can bookmark that you click on to get your page of buttons.
	</p>
	</div>
	
	<a name="create-an-action"></a>
	<h2>Create an action for your first button</h2>
	<div class="white">
		<h3>Add a "Add Tag" action</h3>
	
		<p>Go to the Actions menu page. Click on the add Action button. Note: All actions have the basic options present on the pop and advanced functions tucked away and you have
		to click on "Show Advanced" to see these. If you don't have an X10 controller or a Phidget yet then the easiest action to do is see the result quickly is to add a tag
		or "state" or send yourself an E-mail (You would have to setup your E-mail provider first for this). We will add a tag for this action.
		</p>
		
		<p>Select the name you gave to your button in the drop down list, add the name of the action (Must be unique) in the Name first and a Description for your own use in the
		Description field. Select "Add Tag" from the Type field. Add some new tag name, such as "My Tag" and a validFor value of "120" (two minutes). Save this action
		and save the whole list of actions with the Save button at the bottom. Now go back to your Button Groups page and click on the Url link you made for your button.
		Now click on your button. Voila! Nothing visible happened. However, if you click on the "Links" menu page and then on the "Change state buttons" link you will see
		your tags is present. If you wait two minutes then reload this page you will see the tag has disappeared.
		<p>
		
		<h3>Now check how this can affect actions</h3>
		
		<p>
		Go to the actions page and click on "Add action". Now click on the "Show Advanced" link. You will now see your tag present in the two lists titled "Required tags"
		and "Disallowed tags". Selecting a tag from the required tags means that the action you are editing is only allowed to execute if the tag with a name in the Required tags
		section is currently active. Conversely, an action is only allowed to execute if no tag that is selected in the disallowed tags area is currently present/active.
		</p>
		
	</div>
	
	<a name="turning-a-light-on">
	<h2>Create an action to turn a light on</h2>

	<div class="white">
	
		<p>Go to the Button Groups page and add a button to an existing or new group with the name "Light on" (For example). Now go to the Actions Page and add a new action. Select
		the event called "Light on" that you created earlier. Give it a name and a description and select X10 as the action type. Add the house code and unit code and select "On" as the action.
		Now go to the Button Groups page and click on the link for the button group. Click the Light on button. The light should go on. Note: If you want to use house codes
		other than the default one of "A" you must set this up with the software that comes with the CM15PRO according to their instructions.
		</p>
	</div>
	
	<a name="sending-video-and-an-sms">
	<h2>Sending video and an SMS in response to an X10 PIR event</h2>

	<div class="white">
	<h3>First add the rfxcom PIR device</h3>
		<p>Add the rfxcom device that corresponds to the PIR. Set the correct house and unit code. Add the eventname you want for it to send when it's triggered
		On and for Off. Note: You can test that you have the correct code selected by waving your hand in front of the PIR and then checking the rfxcom logs.
		</p>
		
	<h3>Now setup your SMS provider</h3>
	
		<p>Go to the SMS page and click on the link to AQL to setup your account. Once you have your username and password enter these details to the settings as well as
		who you want the SMSes to appear to come from. Note: AQL provide a number of free SMSes for each account setup from this link to get you started.
		</p>
		
	<h3>Setup your cameras</h3>
	
		<p>You will need to add some cameras in order to trigger video from them. We recommend recording two video streams for each camera. One at the highest resolution
		and one at a resolution that is suitable for your camera such as 384x288 or lower. Note: It's important to set the frame rate always to 5 frames per second as that is
		what the system expects and plays back at.
		</p>
		
	<h3>Add the Video action</h3>
	
		<p>Go to the Actions page and add a video trigger action. Select the event that you used when you setup the X10 device that corresponds to the On trigger.
		Select Video as the Action type. Select which cameras from your list that you have earlier setup that you wish to trigger when this PIR is activated. Save.
		</p>
		
	 <h3>Add the SMS action</h3>
	 
		 <p>Go to the Actions page. Add an Action and choose the same event as for the video event above. Choose SMS as the action type. Enter your phone number with the full
		 access for International access from the UK if the number is not a UK number. (For example, the US would be 00 1 then your number. The Netherlands would be 00 31 then your
		 number). Select the same cameras that you selected from the video trigger above.
		 </p>
		 
	 <h3>Add the webprefix</h3>
	 
		 <p>Setup your webprefix in the System Page . If you just set this up and are accessing it from your internal web via an IP address, this will probably looks something like: http://192.168.1.5:8080).
		 </p>
		 
	 <h3>Trigger the event</h3>
	 
		 <p>Walk through the sensor. It should now both trigger video and send you an SMS with a link to the video. You can also see this video by going to the Links page
		 and selecting the link called "List MJPEG events".
		 </p>
	</div>
	
	
	
	
	
</div>