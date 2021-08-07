ScheduleBuilder = function () {
	this.buildSchedules = function () {
		var schedules = ScheduleController.getSchedules();
		var scheduleTmpl = SharedTemplates.getTemplate('schedule');
		var output = '';
		
		for (var i = 0, c = schedules.length; i < c; i++) {
			var schedule = schedules[i];
			
			scheduleTmpl.setObject(schedule);
			scheduleTmpl.set('dows', this.buildDows(schedule.timeSpec.dows));
			scheduleTmpl.set('times', this.buildTimes(schedule.timeSpec.times));
			
			output += scheduleTmpl.render();
			scheduleTmpl.reset();
		}
		
		jQuery('#scheduleContainer').html( output );
	}
	
	this.buildDows = function (dows) {
		var output = '';
		var dowTmpl = SharedTemplates.getTemplate('dow');
		
		for (var i = 1; i < 8; i++) {
			dowTmpl.set('value', (i % 7) + 1);
			dowTmpl.set('isChecked', dows.hasValue((i % 7) + 1) ? 'checked="checked"' : '');
			output += dowTmpl.render();
			dowTmpl.reset();
		}
		
		return output;
	}
	
	this.buildTimes = function (times) {
		var output = '';
		var timeTmpl = SharedTemplates.getTemplate('time');
		
		for (var i = 0, c = times.length; i < c; i++) {
		
			timeTmpl.setObject( TimeHelper.stringify(times[i]) );
			output += timeTmpl.render();
			timeTmpl.reset();
		}

		return output;
	}
}