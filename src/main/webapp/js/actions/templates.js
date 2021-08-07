/**
	Collection of shared templates
*/


//
// ATTRIBUTES
//


// default
SharedTemplates.setTemplate('attribute-default', '\
	<tr>\
	<td><label>%{name}</label></td>\
	<td %{attribs}><input type="text" class="attribute_value" name="%{property}" value="%{value}" /></td>\
	</tr>\
');

//phidget list
SharedTemplates.setTemplate('attribute-phidgetname', '\
	<tr>\
	<td><label>Phidget Name</label></td>\
	<td>\
	<select id="action_phidgets" name="phidgetName">\
		%{each, var:availablePhidgets, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

//rfxcom command list
SharedTemplates.setTemplate('attribute-rfxcomcommand', '\
	<tr>\
	<td><label>Command</label></td>\
	<td>\
	<select id="action_rfxcom" name="rfxcomCommand">\
		%{each, var:availableRfxcomCommands, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

// E-mail Group Names
SharedTemplates.setTemplate('attribute-responsegroup', '\
	<tr>\
	<td><label>Response Buttons</label></td>\
	<td>\
	<select id="action_responsegroup" name="responseGroup">\
		<option value="">Not required</options>\
		%{each, var:availableButtonGroups, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

// cameralist
SharedTemplates.setTemplate('attribute-cameraset', '\
	<tr>\
	<td colspan="2"><label>List of cameras</label>\
	<br />\
	<select id="action_cameras" name="cameraSet" multiple="multiple">\
		%{each, var:availableCameras, repeatString:<option value="%value">Camera #%value</options>}\
	</select>\
	</td>\
	</tr>\
');

//cameralist remote
SharedTemplates.setTemplate('attribute-cameraset-remote', '\
	<tr id="cameraset-remote">\
	<td colspan="2"><label>List of remote cameras</label>\
	<br />\
	<select id="action_cameras" name="cameraSet" multiple="multiple">\
		%{each, var:remoteCameras, repeatString:<option value="%value">Camera #%value</options>}\
	</select>\
	</td>\
	</tr>\
');

//cameralist
SharedTemplates.setTemplate('attribute-videotype', '\
	<tr>\
	<td><label>Video Type</label></td>\
	<td><select id="action_videotype" name="videoType">\
		%{each, var:availableVideoTypes, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

// methodType
SharedTemplates.setTemplate('attribute-methodtype', '\
	<tr>\
	<td><label>Method type</label></td>\
	<td>\
	<select id="action_methodtype" name="methodType">\
		%{each, var:availableMethodTypes, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

//tagname
SharedTemplates.setTemplate('attribute-tagname', '\
	<tr>\
	<td><label>List of tags</label></td>\
	<td>\
	<select id="delete_tagname" name="tagName">\
		%{each, var:deletableTagNames, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

//freakname
SharedTemplates.setTemplate('attribute-freakname', '\
	<tr>\
	<td><label>Freak Name</label></td>\
	<td>\
	<select id="action_freakname" name="freakName" onchange="ActionController.listener.renderCameraListForFreak(this.value)">\
		%{each, var:availableFreakNames, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

// direction
SharedTemplates.setTemplate('attribute-direction', '\
	<tr>\
	<td><label>Direction</label></td>\
	<td>\
	<select id="action_direction" name="direction">\
		%{each, var:availableDirections, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

// parameters
SharedTemplates.setTemplate('attribute-parameters', '\
	<tr>\
	<td><label>Parameters</label></td>\
	<td>\
	<input type="hidden" name="parameters" value="">\
	<table class="action-parameters" style="width:auto;" cellspacing="0">\
		<thead>\
		<tr><th>Key</th><th>Value</th><th>&nbsp;</th></tr>\
		</thead>\
		<tbody>\
		%{foreach, var:parameters, repeatString:<tr><td><span class="token">%key</span></td><td><span class="token">%value</span></td><td><input type="button" value="-" onclick="ActionHelper.removeRow(this)"</td></tr>}\
		</tbody>\
	</table>\
	<input type="button" name="Add parameter" onclick="JSDialog.openDialog(\'jsp/content/forms/key-value.html\', this)" value="Add parameter">\
	<br><br>\
	</td>\
	</tr>\
');

//cameralist
SharedTemplates.setTemplate('attribute-phidgetactiontype', '\
	<tr>\
	<td><label>Phidget Action Type</label></td>\
	<td><select id="phidget_action_type" name="phidgetActionType" onchange="JSPostNotification(\'PhidgetActionTypeChanged\', this, {});">\
		%{each, var:phidgedTypes, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

// actionName
SharedTemplates.setTemplate('attribute-actionname', '\
	<tr>\
	<td colspan="2"><label>Action name</label>\
	<br />\
	<select id="action_actionname" name="actionName">\
		%{each, var:availableActionNames, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');


//protocol
SharedTemplates.setTemplate('attribute-protocol', '\
	<tr>\
	<td><label>Protocol</label></td>\
	<td>\
	<select id="action_protocol" name="protocol" onchange="ActionController.listener.protocolChanged(this)">\
		%{each, var:availableProtocols, repeatString:<option value="%value">%value</options>}\
	</select>\
	</td>\
	</tr>\
');

SharedTemplates.setTemplate('attribute-url', '\
		<tr>\
		<td><label>Url</label></td>\
		<td>\
			<input type="text" name="url" onkeyup="ActionController.listener.urlChanged(this)" id="actionUrl" value="%{url}" onblur="ActionHelper.forceProtocol(this)">\
			<span class="VerifyHostnameAttribute">&nbsp;<input type="checkbox" name="verifyHostname" checked>&nbsp;Verify hostname</span>\
		</td>\
		</tr>\
	');

SharedTemplates.setTemplate('attribute-duration', '\
		<tr id="duration">\
		<td><label>Duration</label></td>\
		<td>\
			<input type="text" name="duration" value="%{duration}">\
		</td>\
		</tr>\
	');

//
// Action
//


// action in list
SharedTemplates.setTemplate('action-list-empty', '\
	<tr>\
	<td><center>There are no actions defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('action-list-row', '\
	<tr>\
	<td width="50px" class="center"><img alt="%{guestImage}" src="images/%{guestImage}"></td>\
	<td>%{eventName}</td>\
	<td>%{actionType}</td>\
	<td>%{name}</td>\
	<td>%{description}</td>\
	<td style="text-align:right"><input type="button" name="Edit action" value="Edit" onclick="JSPostNotification(\'EditAction\', this, {name:\'%{name}\'});"></td>\
	<td style="text-align:right"><input title="Remove action" type="button" value="-" onclick="JSPostNotification(\'RemoveAction\', this, {name:\'%{name}\'});"></td>\
	</tr>\
');

SharedTemplates.setTemplate('action-time', '\
	<tr class="time-row">\
	<td>Time&nbsp;&nbsp;</td>\
	<td class="range"><div class="dows">%{dows}</div>\
		<table><tbody>%{ranges}</tbody></table></td>\
	<td><input type="button" value="-" onclick="TimeHelper.removeTime(this, %{timeIndex});"></td>\
	</tr>\
');

SharedTemplates.setTemplate('counter-table', '\
	<form onsubmit="return false">\
		<table>\
		<tbody>\
			<tr>\
				<td>Count</td>\
				<td>&nbsp;</td>\
				<td>%{count}</td>\
			</tr>\
			<tr>\
				<td>Within</td>\
				<td>&nbsp;</td>\
				<td>%{withinSeconds} Secs</td>\
			</tr>\
		</tbody>\
	</table>\
	</form>\
');

SharedTemplates.setTemplate('action-time-range', '\
	<tr>\
	<td><span class="token">%{startHour}</span></td>\
	<td>:</td>\
	<td><span class="token">%{startMin}</span></td>\
	<td>:</td>\
	<td><span class="token">%{startSec}</span></td>\
	<td> - </td>\
	<td><span class="token tokenEndTime">%{endHour}</span></td>\
	<td>:</td>\
	<td><span class="token tokenEndTime">%{endMin}</span></td>\
	<td>:</td>\
	<td><span class="token tokenEndTime">%{endSec}</span></td>\
	<td><input type="button" value="-" onclick="TimeHelper.removeTimeRange(this, %{rangeIndex});"></td>\
	</tr>\
');

SharedTemplates.setTemplate('action-time-range-no-button', '\
	<tr>\
	<td><span class="token">%{startHour}</span></td>\
	<td>:</td>\
	<td><span class="token">%{startMin}</span></td>\
	<td>:</td>\
	<td><span class="token">%{startSec}</span></td>\
	<td> - </td>\
	<td><span class="token tokenEndTime">%{endHour}</span></td>\
	<td>:</td>\
	<td><span class="token tokenEndTime">%{endMin}</span></td>\
	<td>:</td>\
	<td><span class="token tokenEndTime">%{endSec}</span></td>\
	</tr>\
');