ProfileBuilder = function () {
	this.buildProfileList = function () {
		var list = '';
		var tmpl;
		var profiles = ProfileController.getProfileList();
		
		// render all actions
		if ( profiles.length > 0 ) {
			tmpl = SharedTemplates.getTemplate('profile-list-row');
				
			for (var i = 0, c = profiles.length; i < c; i++) {
				var profile = profiles[i];
				
				if (!profile) {
					console.warn( 'Profile not defined but in profileList' );
					continue;
				}
				
				tmpl.setObject(profile);
				
				list += tmpl.render();
				
				tmpl.reset();
			}
		// show a message for empty list
		}else{
			tmpl = SharedTemplates.getTemplate('profile-list-empty');
			list += tmpl.render();
		}
		
		jQuery('#profileList').html(list);
	};
	
	this.buildTimes = function (times) {
		if ( !times) {
			times = [];
		}
		
		var tmpl 		= SharedTemplates.getTemplate('action-time');
		var tmplRange 	= SharedTemplates.getTemplate('action-time-range-no-button');
		var html 		= '';
		
		for (var i = 0, c = times.length; i < c; i++) {
			var time = times[i];
			tmpl.setObject(time);
			tmpl.set( 'timeIndex', i.toString() );
			tmpl.set('dows', this.buildDows(time));
			tmpl.set('ranges', this.buildTimeRanges(time.times, true));
			html += tmpl.render();
			
			tmpl.reset();
		}
		
		jQuery('#times').html( html );
	};
	
	this.buildDows = function (time) {
		var dows = time.dows;
		
		if (dows.length == 7) {
			return 'Every day of the week';
		}
		
		var text = '';
		for (var i = 0, c = dows.length; i < c; i++) {
			var day = TimeHelper.getDayNameFromInteger(dows[i]);
			var prefix = '';
			
			if (i > 0) {
				prefix = ' ,';
			}
			
			text += prefix + day;
		}
		return text;
	};
	
	this.buildTimeRanges = function (ranges, withoutDeleteButton) {
		if ( !ranges) {
			ranges = [];
		}
		
		var tmpl = SharedTemplates.getTemplate('action-time-range' + (withoutDeleteButton ? '-no-button' : '') );
		var html = '';
		
		for (var i = 0, c = ranges.length; i < c; i++) {
			var range 	= ranges[i];
			// IMPORTANT: fixed non rendering 0 values
			var r 		= TimeHelper.stringify(range);
			tmpl.setObject( r );
			tmpl.set( 'rangeIndex', i.toString() );
			html += tmpl.render();
			
			tmpl.reset();
		}
		
		jQuery('#timeRanges').html( html );
		
		return html;
	};
}