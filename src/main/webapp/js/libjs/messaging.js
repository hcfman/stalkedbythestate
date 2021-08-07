JSMessageStack = function () {
	this._stack = [];
	
	this.hasMessage = function (text) {
		for (var i = 0, c = this._stack.length; i < c; i++) {
			var entry = this._stack[i];
			if ( entry && entry.msg == text ) {
				return entry;
			}
		}
		return false;
	}
	
	this.remove = function (entry) {
		this._stack[entry.index] = undefined;
	}
	
	this.post = function ( type, msg ) {
		var div = jQuery( '<div class="' + type + '"><span class="count"></span><p>' + msg + '</p></div>' );
		jQuery('#message-container').append(div);
		
		entry = {};
		entry.div = div;
		entry.msg = msg;
		entry.count = 1;
		entry.index = this._stack.length;
		entry.timeout = window.setTimeout(function () {
			div.fadeOut('slow');
		}, 3000);
		
		this._stack.push(entry);
	}
	
	this.postError = function ( note ) {
		this.post('error', note.sender);
	}
	
	this.postWarning = function ( note ) {
		this.post('warning', note.sender);
	}
	
	this.postInfo = function ( note ) {
		this.post('info', note.sender);
	}
}

JSMessageStack = new JSMessageStack();

JSNotificationCenter.addObserver(JSMessageStack, 'postInfo'		, 'OnInfo');
JSNotificationCenter.addObserver(JSMessageStack, 'postWarning'	, 'OnWarning');
JSNotificationCenter.addObserver(JSMessageStack, 'postError'	, 'OnError');