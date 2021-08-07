ProfileFormListener = function () {
	
	this.shouldCancelAddProfile = function (note) {
		ProfileController.dummy = {};
	};
	
	this.shouldAddProfile = function (note) {
		var profileData = note.data;
		
		if ( ProfileHelper.validate(profileData) ) {
			var profile = ProfileHelper.makeProfile(profileData);
			ProfileController.profileList.push( profile );
			ProfileController.builder.buildProfileList();
			
			ProfileController.dummy = {};
		}else{
			return false; // prevent closing dialog
		}
	};
	
	this.removeProfile = function (note) {
		ProfileController.removeProfileByName( note.data.name );
		ProfileController.builder.buildProfileList();
	};
	
	this.didLoadAddTime = function ( note ) {
		if ( !ProfileController.dummy.times ) {
			ProfileController.dummy.times = [];
		}
		ProfileController.dummy.ranges = [];
	};
	
	this.shouldAddTime = function ( note ) {
		var dows 		= note.data;
		var validTime 	= {};
		validTime.times = ProfileController.dummy.ranges;
		validTime.dows	= [];
		
		var count = 1;
		for (var d in dows) {
			var value = dows[d];
			if (value) {
				validTime.dows.push((count%7)+1);
			}
			count++;
		}
		
		var times = ProfileController.dummy.times;
		times.push(validTime);
		ProfileController.builder.buildTimes(times);
		
		//JSPostNotification('OnInfo', 'New time added to action');
	};
	
	this.didLoadAddTimeRange = function ( note ) {};
	
	this.shouldAddTimeRange = function ( note ) {
		var ranges = ProfileController.dummy.ranges;
		ranges.push(note.data);
		ProfileController.builder.buildTimeRanges(ranges);
	};
	
	this.shouldCancelAddTime = function (note) {
		ProfileController.dummy.ranges = [];
	};
	
	
	this.editProfile = function (note) {
		var subject = ProfileController.getProfileByName(note.data.name);
		JSDialog.openDialog('jsp/content/forms/profile-new.html', note.sender, subject, {buttonName:'Save'});
	};
	
	this.didLoadEditProfile = function (note) {
		var profile = note.data.subject;
		
		var tmpl = note.data.template;
		tmpl.setObject(profile);
	};
	
	this.didShowEditProfile = function (note) {
		jQuery('input#profileName').attr('disabled', true);
		
		var profile = note.data.subject;
		ProfileController.dummy.times = profile.timeSpecs;
		ProfileController.builder.buildTimes( profile.timeSpecs );
	};
	
	this.shouldEditProfile = function (note) {
		var profileData = note.data;
		
		if ( ProfileHelper.validate(profileData) ) {
			var profile = ProfileHelper.makeProfile(profileData);
			ProfileController.replaceProfile( profile );
			ProfileController.builder.buildProfileList();
			
			ProfileController.dummy = {};
		}else{
			return false; // prevent closing dialog
		}
	}
	
	/*
	 * Added to fix the bug where you cancel a profile edit the time object will remain
	 * in memory and gets added to the next time object you add to a profile.
	 */
	this.shouldCancelEditProfile = function () {
		ProfileController.dummy = {};
	};
}