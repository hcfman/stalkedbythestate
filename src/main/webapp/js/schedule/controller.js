/*
	Schedule actions
*/
(function () {
	
	window.SBTSScheduleInstance = null;
	window.SBTSScheduleRepeat = {
		No: 0,
		Weekly: 1
	};
	
	
	
	window.SBTSSchedule = function () {
		if (!window.SBTSScheduleInstance) {
			window.SBTSScheduleInstance = new _SBTSSchedule();
		}
		return window.SBTSScheduleInstance;
	}
	
	window.SBTSScheduleAction = function (actionName, actionTimeString) {
		this.name = actionName;
		this.time = actionTimeString;
		this.dows = [];
		this.repeats = SBTSScheduleRepeat.No;
		this.isNew	 = false;
		
		this.render = function () {
			var isNewClass 	= this.isNew ? ' new' : '';
			this.isNew 		= false;
			
			var dowString = '';
			dowString += DayOfWeek.stringFromDay(this.dows[0]);
			
			for (var i = 1, c = this.dows.length; i < c; i++) {
				dowString += ', ' + DayOfWeek.stringFromDay(this.dows[i]);
			}
			
			return '<div class="action'+ isNewClass +'">\
				<div class="repeats">' + dowString + '</div>' + this.name + '</div>';
		}
	}
	
	window._SBTSSchedule = function () {
		this.actions = [];
		
		this.deleteActionWithName = function (actionName) {
			var count = this.actions.length;
			for (var i = 0; i < count; i++) {
				var action = this.actions[i];
				if (action.name == actionName) {
					this.actions.remove(i);
					break;
				}
			}
			this.render();
		}
		
		this.addAction = function (actionObject) {
			actionObject.isNew = true;
			this.actions.push(actionObject);
			this.render();
		}
		
		this.sortActions = function () {
			this.actions.sort(this._actionSort);
		}
		
		this._actionSort = function (a,b) {
			var value = 0;
			if (a.time < b.time) {
				value = -1;
			}
			if (a.time > b.time) {
				value = 1;
			}
			return value;
		}
		
		this.render = function () {
			this.sortActions();
			
			var actions = this.actions;
			var count	= actions.length;
			var $tbody	= jQuery('#scheduleActions');
			
			$tbody.find('tr').each( function () {
				if (this.className == 'no-delete') {
					return;
				}
				
				$(this).remove();
			}).end();
			
			if (count == 0) {
				$tbody.append('<tr><td colspan="3">Empty</td></tr>');
			}
			
			for (var i = 0; i < count; i++) {
				var a 	= actions[i];
				var row	= '<tr>\
					<td>' + a.time + '</td>\
					<td>' + a.render() + '</td>\
					<td><input type="button" value="Delete" onclick="SBTSSchedule().deleteActionWithName(\'' + a.name + '\')"</td>\
				</tr>';
				
				$tbody.append( row );
			}
			
		}
	}
})();