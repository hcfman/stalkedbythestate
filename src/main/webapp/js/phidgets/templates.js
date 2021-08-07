SharedTemplates.setTemplate('phidget-list-empty', '\
	<tr>\
	<td><center>There are no phidgets defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('phidget-list-row', '\
	<tr>\
	<td>%{name}</td>\
	<td>%{description}</td>\
	<td>%{serialNumber}</td>\
	<td style="text-align:right"><input type="button" name="Edit Phidget" value="Edit" onclick="\
		JSPostNotification(\'EditPhidget\', this, {name:\'%{name}\'});">\
	<input title="Remove Phidget" type="button" value="-" onclick="JSPostNotification(\'RemovePhidget\', this, {name:\'%{name}\'});"></td>\
	</tr>\
');

SharedTemplates.setTemplate('phidget-port-row', '\
	<tr>\
		<td><div class="port-index">%{index}</div></td>\
	<td>\
		<table><tr>\
		<tr><td><input type="checkbox" name="inputState%{index}" value="on" %{isInputChecked}></td><td>On/off initial input state</td></tr>\
		<tr><td><input type="checkbox" name="outputState%{index}" value="on" %{isOutputChecked}></td><td>On/off initial output state</td></tr>\
		<td>OnTrigger</td><td><input type="text" value="%{onTrigger}" name="onTrigger%{index}"></td></tr>\
		<tr><td>OffTrigger</td><td><input type="text" value="%{offTrigger}" name="offTrigger%{index}"</td></tr></table>\
	</td></tr>\
');

SharedTemplates.setTemplate('phidget-port-out-row', '\
		<tr>\
			<td><div class="port-index">%{index}</div></td>\
		<td>\
			<table><tr>\
			<tr><td><input type="checkbox" name="port" value="%{index}" %{isChecked}></td><td>On/off initial state</td></tr>\
			</table>\
		</td></tr>\
	');