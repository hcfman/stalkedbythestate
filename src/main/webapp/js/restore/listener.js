RestoreListener = function() {

	this.didShowRestore = function(note) {
	};

	this.dialogShouldRestore = function(note) {
		jQuery('#restoreform').submit();
	};

};