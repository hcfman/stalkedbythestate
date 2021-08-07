ProfileHelper = function () {
	
	this.validate = function (profile) {
		
		var validator = Validator.getValidator( 'Profile' );
		jQuery('div.ui-dialog-content input').removeClass('validation-error');
				
		return validator.check(profile);
	};
	
	this.makeProfile = function (data) {
		var profile = {};
		profile.name = data.name;
		profile.description = data.description;
		profile.tagname = data.tagname;
		profile.timeSpecs = ProfileController.dummy.times;

		return profile;
	};
};

ProfileHelper = new ProfileHelper();

// TODO: need to improve these time removing methods
TimeHelper.removeTime = function (rowElement, index) {
	ProfileController.dummy.times.remove(index);
	
	ProfileController.builder.buildTimes( ProfileController.dummy.times );
	JSPostNotification('OnWarning', 'Time removed from profile');
};

TimeHelper.removeTimeRange = function (rowElement, index) {
	//ActionHelper.removeRow(rowElement);
	ProfileController.dummy.ranges.remove(index);
	ProfileController.builder.buildTimeRanges( ProfileController.dummy.ranges );
	JSPostNotification('OnWarning', 'Range removed from time');
};
