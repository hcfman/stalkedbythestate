/*
 * Main event listener for ActionController
 * TODO: get rid of unused methods
 */
ScheduleFormListener = function () {
	
	this.shouldAddSchedule = function (note) {
		var scheduleData = note.data;
		
		if ( ScheduleHelper.validate(scheduleData) ) {
			var schedule = ScheduleHelper.makeSchedule( scheduleData );
			ScheduleController.schedules.push( schedule );
			ScheduleController.builder.buildSchedules();
		}else{
			return false;
		}
	};
	
	this.editSchedule = function (note) {
		var subject = ScheduleController.getScheduleByName(note.data.name);
		JSDialog.openDialog('jsp/content/forms/schedule-new.html', note.sender, subject, {buttonName:'Save'});
	};
	
	this.didLoadEditSchedule = function (note) {
		var tmpl = note.data.template;
		var schedule = note.data.subject;
		tmpl.setObject(schedule);
		tmpl.set('isGuestChecked', schedule.guest ? ' checked' : '');
		
		for (var i = 0, c = schedule.timeSpec.dows.length; i < c; i++) {
			var day = schedule.timeSpec.dows[i];
			tmpl.set('isDay' + day + 'Checked', ' checked');
		}
		
	};
	
	this.didShowEditSchedule = function (note) {
		var schedule = note.data.subject;
		
		jQuery('input#scheduleName').attr('disabled', true);

		jQuery('#timeRangeStartHour > option').each( function () {
			if ( this.value == schedule.timeSpec.times[0].startHour ) {
				this.selected = true;
			}
		});
		
		jQuery('#timeRangeStartMin > option').each( function () {
			if ( this.value == schedule.timeSpec.times[0].startMin ) {
				this.selected = true;
			}
		});
		
		jQuery('#timeRangeStartSec > option').each( function () {
			if ( this.value == schedule.timeSpec.times[0].startSec ) {
				this.selected = true;
			}
		});
	};
	
	this.shouldEditSchedule = function (note) {
		var scheduleData = note.data;
		
		if ( ScheduleHelper.validate(scheduleData) ) {
			var schedule = ScheduleHelper.makeSchedule( scheduleData );
			ScheduleController.replaceSchedule( schedule );
			ScheduleController.builder.buildSchedules();
		}else{
			return false;
		}
	}
	
	this.removeSchedule = function (note) {
		ScheduleController.removeSchedule(note.data);
		ScheduleController.builder.buildSchedules();
	};
};