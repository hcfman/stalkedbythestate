FormHelper = {};
/**
	Collects data from a form and returns it.
*/
FormHelper.getData = function (form) {
	var data = {};
	form.find('input, select, textarea').each( function () {
		// skip buttons
		if (this.type && this.type == 'button') {
			return;
		}
		
		// field MUST have a name defined
		if (this.name) {
			var value;
			
			if ( this.name == "parameters" ) {
				
				if (data.parameters) return;
				
				var params = {};
				
				jQuery( this ).next().find('tbody > tr').each( function () {
					var row = jQuery(this).find('td');
					var key = row.first().text();
					var value = row.first().next().text();
					
					params[key] = value;
				});
				data['parameters'] = params;
				return;
				
			} else if ( this.type == 'checkbox' ) {
				value = this.checked;
			} else {
				value = jQuery(this).val();
			}
			
			//if (this.name != 'parameters') {
				data[this.name] = value;
			//}
		}else{
			console.warn( this.toString() + ' has no name attribute defined' );
		}
	} );
	
	return data;
}

TimeHelper = {};

TimeHelper.stringify = function (range) {
	var r = JSObject.clone(range);
	for (p in r) {
		var v = r[p];
		
		if ( typeof(v) != 'number' ) {
			continue;
		}
		
		if (v == 0) {
			r[p] = '00';
		}else if (v < 10) {
			r[p] = '0' + r[p];
		}
	}
	return r;
}

var _dayNames = [
          'Sunday',
          'Monday',
          'Tuesday',
          'Wednesday',
          'Thursday',
          'Friday',
          'Saturday'
	];
TimeHelper.getDayNameFromInteger = function (i) {
	i--;
	if (i > 0 && i < 7) {
		return _dayNames[i];
	}else{
		return _dayNames[0];
	}
};