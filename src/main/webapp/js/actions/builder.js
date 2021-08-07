/**
	Responsable for building form parts on demand
*/
ActionBuilder = function () {
	// build attributes for actions
	this.buildAttributes = function (attributes) {
		var tmpl = new JSTemplate('%{foreach, var:actionAttributes, repeatString:<tr><td>%value</td></tr>}');
		
		tmpl.setObject(ActionController.dummy);
		tmpl.set('actionAttributes', attributes);
		
		jQuery('#actionAttributes').html( tmpl.render() );
	}
	
	// build the action list to display
	this.buildActionList = function () {
		var list = '';
		var tmpl;
		var actions = ActionController.getActionList();
		
		// render all actions
		if ( actions.length > 0 ) {
			tmpl = SharedTemplates.getTemplate('action-list-row');
				
			for (var i = 0, c = actions.length; i < c; i++) {
				var action = actions[i];
				
				if (!action) {
					console.warn( 'Action not defined but in actionList' );
					continue;
				}
				
				tmpl.set('guestImage'		, action.guest ? 'check.png' : 'false.png');
				tmpl.set('delayString'	, action.delay ? action.delay + '<i>s</i>' : '');
				tmpl.setObject(action);
				
				list += tmpl.render();
				
				tmpl.reset();
			}
		// show a message for empty list
		}else{
			tmpl = SharedTemplates.getTemplate('action-list-empty');
			list += tmpl.render();
		}
		
		jQuery('#actionList').html(list);
	};
	
	this.buildCounter = function(eventCounter) {
		var tmpl = SharedTemplates.getTemplate('counter-table');
		tmpl.set('count', eventCounter.count);
		tmpl.set('withinSeconds', eventCounter.withinSeconds);
		jQuery('#counter').html(tmpl.render());
		jQuery('#addCounter').val("Remove counter");
		jQuery('#addCounter').removeAttr('onclick');
		jQuery('#addCounter').click(function() {
			ActionController.dummy.eventCounter = null;
			
			jQuery('#counter').html('');
			jQuery('#addCounter').val("Add counter");
			jQuery('#addCounter').click(function() {
				JSDialog.openDialog('jsp/content/forms/counter-new.html', this, null, {buttonName:'Save', width:'500px'});
			});
		});
	};
	
	this.buildTimes = function (times) {
		if ( !times || times.length == 0) {
			return;
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
		if ( !ranges || ranges.length == 0) {
			return;
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
	}
}