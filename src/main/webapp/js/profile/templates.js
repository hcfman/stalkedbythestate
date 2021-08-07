SharedTemplates.setTemplate('profile-list-empty', '\
	<tr>\
	<td colspan="5"><center>There are no profiles defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('profile-list-row', '\
	<tr>\
	<td>%{name}</td>\
	<td>%{description}</td>\
	<td>%{tagname}</td>\
	<td style="text-align:right"><input type="button" name="Edit Profile" value="Edit" onclick="JSPostNotification(\'EditProfile\', this, {name:\'%{name}\'});"></td>\
	<td style="text-align:right"><input title="Remove Profile" type="button" value="-" onclick="JSPostNotification(\'RemoveProfile\', this, {name:\'%{name}\'});"></td>\
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