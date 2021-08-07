SharedTemplates.setTemplate('rfxcomCommand-list-empty', '\
	<tr>\
	<td><center>There are no RFXCOM commands defined</center></td>\
	</tr>\
');

SharedTemplates.setTemplate('rfxcomCommand-list-row', '\
	<tr>\
	<td>%{name}</td>\
	<td>%{description}</td>\
	<td>%{eventName}</td>\
	<td>%{rfxcomType}</td>\
	<td style="text-align:right"><input type="button" name="Edit Device" value="Edit" onclick="JSPostNotification(\'EditDevice\', this, {name:\'%{name}\'});"></td>\
	<td style="text-align:right"><input title="Remove Device" type="button" value="-" onclick="JSPostNotification(\'RemoveDevice\', this, {name:\'%{name}\'});"></td>\
	</tr>\
');

SharedTemplates.setTemplate('rfxcom-input-row1', '\
	<tr>\
	<td style="text-align:center"><input title="Remove Device" type="button" value="-" onclick="RfxcomHelper.removeInputListRow(%{index});"></td>\
	<td style="text-align:center">[%{index}]</td>\
	<td><input type="text" name="mask%{index}" size="2" maxlength="2" value="%{mask}" onkeyup="RfxcomHelper.inputMaskChanged(this, %{index})"></td>\
	<td>\
		<select id="operator%{index}" name="operator%{index}" onchange="RfxcomHelper.inputOperatorChanged(this, %{index})">\
			<option value="EQ">=</option>\
			<option value="GE">&gt;=</option>\
			<option value="GT">&gt;</option>\
			<option value="LE">&lt;=</option>\
			<option value="LT">&lt;</option>\
			<option value="RANGE">Range</option>\
		</select>\
	</td>\
	<td><input type="text" id="value_1_%{index}" name="value_1_%{index}" size="2" maxlength="2" value="%{value1}" onkeyup="RfxcomHelper.inputValue1Changed(this, %{index})"></td>\
	<td></td>\
	<td>%{fieldUsage}</td>\
	</tr>\
	');

SharedTemplates.setTemplate('rfxcom-input-row2', '\
		<tr>\
		<td style="text-align:center"><input title="Remove Device" type="button" value="-" onclick="RfxcomHelper.removeInputListRow(%{index});"></td>\
		<td style="text-align:center">[%{index}]</td>\
		<td><input type="text" name="mask%{index}" size="2" maxlength="2" value="%{mask}" onkeyup="RfxcomHelper.inputMaskChanged(this, %{index})"></td>\
		<td>\
			<select id="operator%{index}" name="operator%{index}" onchange="RfxcomHelper.inputOperatorChanged(this, %{index})">\
				<option value="EQ">=</option>\
				<option value="GE">&gt;=</option>\
				<option value="GT">&gt;</option>\
				<option value="LE">&lt;=</option>\
				<option value="LT">&lt;</option>\
				<option value="RANGE">Range</option>\
			</select>\
		</td>\
		<td><input type="text" id="value_1_%{index}" name="value_1_%{index}" size="2" maxlength="2" value="%{value1}" onkeyup="RfxcomHelper.inputValue1Changed(this, %{index})"></td>\
		<td><input type="text" id="value_2_%{index}" name="value_2_%{index}" size="2" maxlength="2" value=%{value2} onkeyup="RfxcomHelper.inputValue2Changed(this, %{index})"></td> \
		<td>%{fieldUsage}</td>\
		</tr>\
		');

SharedTemplates.setTemplate('inputWidget', '\
	<table border="0"> \
	<tr> \
		<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> \
		<th>Offset</th> \
		<th>Mask</th> \
		<th>Operator</th> \
		<th>Value1</th> \
		<th>Value2</th> \
		<th>Usage</th> \
	</tr> \
	<tr> \
		<td><input type="button" value="Add Row" colspan="7" onClick="RfxcomHelper.addInputListRow()"></td> \
	</tr> \
	%{inputWidgetRows} \
	</table> \
	');
	
SharedTemplates.setTemplate('outputWidget', '\
	<table border="0"> \
	<tr> \
		<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> \
		<th>Offset</th> \
		<th>Value</th> \
		<th>Usage</th> \
	</tr> \
	<tr> \
		<td><input type="button" value="Add Row" colspan="4" onClick="RfxcomHelper.addOutputListRow();"></td> \
	</tr> \
	%{outputWidgetRows} \
	</table> \
	');

SharedTemplates.setTemplate('rfxcom-output-row', '\
	<tr> \
		<td style="text-align:center"><input title="Remove Device" type="button" value="-" onclick="RfxcomHelper.removeOutputListRow(%{index});"></td> \
		<td style="text-align:center">[%{index}]</td> \
		<td><input type="text" id="value%{index}" name="value%{index}" size="2" maxlength="2" value="%{value}" onkeyup="RfxcomHelper.outputValueChanged(this, %{index})"></td> \
		<td>%{fieldUsage}</td> \
	</tr> \
	');

