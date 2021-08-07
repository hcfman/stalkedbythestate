SharedTemplates.setTemplate('schedule', '\
	<div class="schedule round">\
		<input type="button" name="Edit Schedule" value="Edit" onclick="JSPostNotification(\'EditSchedule\', this, {name:\'%{name}\'});">\
		<input type="button" name="Remove Schedule" value="-" onclick="JSPostNotification(\'RemoveSchedule\', this, {name:\'%{name}\'});">\
		<h2 class="round">%{name}</h2>\
		<div class="center">Fire event \'%{eventName}\'</div>\
		<table>\
		<tbody>\
			<tr>\
				<td>Mo</td>\
				<td>Tu</td>\
				<td>We</td>\
				<td>Th</td>\
				<td>Fr</td>\
				<td>Sa</td>\
				<td>Su</td>\
			</tr>\
			<tr>%{dows}</tr>\
		</tbody>\
	</table>\
		<div class="times">@%{times}</div>\
	</div>\
');

SharedTemplates.setTemplate('dow', '\
	<td><input type="checkbox" name="dows[]" value="%{value}" %{isChecked} disabled></td>\
');


SharedTemplates.setTemplate('time', '\
	<div> <span>%{startHour}</span> :  <span>%{startMin}</span> : <span>%{startSec}</span> </div>\
');