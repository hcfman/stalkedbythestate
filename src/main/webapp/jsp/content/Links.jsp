<h1>Links</h1>

<ul>
	<li><a href="listmjpg" title="listmjpg">List events</a></li>
	<li><a href="changestate" title="changestate">Change state buttons</a></li>
</ul>

<jsp:include page="components/scripts.jsp" />

<script type="text/javascript" src="js/links/templates.js"></script>
<script type="text/javascript" src="js/links/linkbuilder.js"></script>

<h2>My Links</h2>
<table class="data">
	<tbody id="buildLinks"></tbody>
</table>

<br>
<input type="button" onclick="LinkBuilder.saveLinks()" value="Save links">

<form id="linkBuilder">
	<h2>Link Builder</h2>
	<p>Use this utility to build links to useful views.</p>
	<table>
		<tbody>
			<tr>
				<td colspan="4">
					<strong>Name</strong>
					<br>
					<input type="text" name="name" placeholder="New View">
					<br>
					<br>
					<strong>List type</strong>
					<br>
					<select name="listtype">
						<option value="MJPEG">MJPEG</option>
					</select>
					<br>
					<br>
				</td>
			</tr>
			<tr>
				<td class="section">
					<label>Select cameras</label>
					<br>
					<select name="cameras" multiple>
						<optgroup label="sbts">
							
						</optgroup>
					</select>
				</td>
				<td class="section">
					<label>Events patterns</label>
					<br>
					<p><em>Each line defines a pattern to match an event.<br>
					The star(*) can be used as a wildcard.</em></p>
					<textarea name="events" cols="30" rows="8"></textarea>
				</td>
				<td class="section">
					<label>Date</label>
					<br>
					<br>
					<table>
						<tbody>
							<tr>
								<td>
									<strong>Start</strong>
									<br>
									<em>YYYY-MM-DD or today</em>
									<br>
									<input name="datestart" type="text" placeholder="today">
								</td>
							</tr>
							<tr>
								<td>
									<strong>End</strong>
									<br>
									<em>YYYY-MM-DD</em>
									<br>
									<input name="dateend" type="text">
								</td>
							</tr>
						</tbody>
					</table>
				</td>
				
				<td class="section">
					<label>Time</label>
					<br>
					<br>
					<table>
						<tbody>
							<tr>
								<td>
									<strong>Start</strong>
									<br>
									<em>HH:MM</em>
									<br>
									<select name="timestart">
									<option value="null"></option>
									<option value="00:00">00:00</option>
									<option value="01:00">01:00</option>
									<option value="02:00">02:00</option>
									<option value="03:00">03:00</option>
									<option value="04:00">04:00</option>
									<option value="05:00">05:00</option>
									<option value="06:00">06:00</option>
									<option value="07:00">07:00</option>
									<option value="08:00">08:00</option>
									<option value="09:00">09:00</option>
									<option value="10:00">10:00</option>
									<option value="11:00">11:00</option>
									<option value="12:00">12:00</option>
									<option value="13:00">13:00</option>
									<option value="14:00">14:00</option>
									<option value="15:00">15:00</option>
									<option value="16:00">16:00</option>
									<option value="17:00">17:00</option>
									<option value="18:00">18:00</option>
									<option value="19:00">19:00</option>
									<option value="20:00">20:00</option>
									<option value="21:00">21:00</option>
									<option value="22:00">22:00</option>
									<option value="23:00">23:00</option>
									<option value="24:00">24:00</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>
									<strong>End</strong>
									<br>
									<em>HH:MM</em>
									<br>
									<select name="timeend">
									<option value="null"></option>
									<option value="00:00">00:00</option>
									<option value="01:00">01:00</option>
									<option value="02:00">02:00</option>
									<option value="03:00">03:00</option>
									<option value="04:00">04:00</option>
									<option value="05:00">05:00</option>
									<option value="06:00">06:00</option>
									<option value="07:00">07:00</option>
									<option value="08:00">08:00</option>
									<option value="09:00">09:00</option>
									<option value="10:00">10:00</option>
									<option value="11:00">11:00</option>
									<option value="12:00">12:00</option>
									<option value="13:00">13:00</option>
									<option value="14:00">14:00</option>
									<option value="15:00">15:00</option>
									<option value="16:00">16:00</option>
									<option value="17:00">17:00</option>
									<option value="18:00">18:00</option>
									<option value="19:00">19:00</option>
									<option value="20:00">20:00</option>
									<option value="21:00">21:00</option>
									<option value="22:00">22:00</option>
									<option value="23:00">23:00</option>
									<option value="24:00">24:00</option>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
	
	<br>
	<input type="button" onclick="LinkBuilder.build()" value="Build">
</form>


<!-- Start loading the config -->
<script type="text/javascript">
	try {
		var obj = new (function () {
			this.initialise = function (note) {
				var cameraList 	= note.data.cameraList;
				var freakMap 	= note.data.freakMap;
				
				var cams = jQuery('select[name="cameras"]');
				var sbts  = cams.find('optgroup');
				
				for (var i = 0, c = cameraList.length; i < c; i++) {
					sbts.append('<option value="' + cameraList[i] + '"">Camera #' + cameraList[i] + '</option>');
				}
				
				cams.append(sbts);
				
				
				for (var freakName in freakMap) {
					var freakCams = freakMap[freakName];
					
					var group = jQuery('<optgroup label="' + freakName + '"></optgroup>');
					for (var i = 0, c = freakCams.length; i < c; i++) {
						group.append('<option value="' + freakCams[i] + '">Camera #' + freakCams[i] + '</option>');
					}
					
					cams.append(group);
				}
				
				LinkBuilder.buildLinkList( note.data.linkList );
			}
		});
		
		JSNotificationCenter.addObserver(obj, 'initialise', 'LoadedJSON');
		ConfigLoadJSON('linkbuildergetjson');
	} catch (e) {
		console.error( e );
	}
</script>