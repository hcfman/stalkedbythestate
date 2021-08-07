ScheduleHelper = function () {
	
	this.validate = function (schedule) {
		
		var validator = Validator.getValidator( 'Schedule' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(schedule);
	};
	
	this.makeSchedule = function (data) {
		var schedule = {};
		schedule.name = data.name;
		schedule.eventName = data.eventName;
		schedule.guest = data.guest;
		schedule.timeSpec = {
				dows:[],
				times:[]
		};
		
		var time = {
				startHour:parseInt(data.startHour, 10),
				startMin:parseInt(data.startMin, 10),
				startSec:parseInt(data.startSec, 10)
		};
		
		schedule.timeSpec.times.push(time);
		
		for (var i = 1; i < 8; i++) {
			if ( data['dow' + i] == true ) {
				schedule.timeSpec.dows.push(i);
			}
		}
		
		return schedule;
	};
};

ScheduleHelper = new ScheduleHelper();