EmptyFunction 	= function () {};	// just what it says

// make a fake console available for browsers that don't support it.
// also make a fake console when debug is "false" to suppress messages.
try {
	if (!Debug || !console) {
		console = {log:EmptyFunction, info:EmptyFunction, warn:EmptyFunction, error:EmptyFunction};
	}
} catch (e) {
	console = {log:EmptyFunction, info:EmptyFunction, warn:EmptyFunction, error:EmptyFunction};
}


// Array prototypes
Array.prototype.remove = function(from, to) {
	var rest = this.slice((to || from) + 1 || this.length);
	this.length = from < 0 ? this.length + from : from;
	return this.push.apply(this, rest);
}


Array.prototype.hasValue = function (value) {
	for (var i = 0, l = this.length; i < l; i ++) {
		if (this[i] == value) {
			return true;
		}
	}
	return false;
}


Array.prototype.each = function (callback) {
	for (var k in this) {
		var v = this[k];
		if ( typeof(v) != 'function' ) {
			callback.call(this, k, v);
		}
	}
}

Array.prototype.first = function () {
	return this[0];
}

Array.prototype.last = function () {
	return this[this.length - 1];
}

Array.prototype.sortNumeric = function () {
	return this.sort( function sortNumber(a, b) { 
		return parseInt(a) - parseInt(b); 
	});
}

Array.prototype.find = function ( callback ) {
	var found = [];
	
	for (var k in this) {
		var v = this[k];
		var t = typeof(v);
		
		if (t != 'function' && callback.call(this, k, v)) {
			found.push({key:parseInt(k), value:v});
		}
	}
	
	return found;
}

// String prototypes
String.prototype.toUpperCaseFirst = function () {
	return this.replace(/^./i, function(m){
		return m.toUpperCase();
	});
}

String.prototype.toUpperCaseWords = function () {
	return this.replace(/\b(.).*?\b/g, function (m) {
		return m.toUpperCaseFirst();
	});
}

String.prototype.toFlattened = function () {
	return this.toLowerCase().replace(/\s/g, '_');
}

/**
	Converts:
		"Hello baby", "Dive-into the_river"
	to:
		"HelloBaby", "DiveIntoTheRiver"
*/
String.prototype.toClassified = function () {
	return this.replace(/[-_]/g, ' ').toUpperCaseWords().replace(/\s/g, '');
}


// JSObject
JSObject = {};
JSObject.clone = function (obj) {
	var n = {};
	for (var p in obj) {
		n[p] = obj[p];
	}
	return n;
}

JSObject.merge = function (a, b) {
	var c = this.clone(a);
	for (var p in b) {
		c[p] = b[p];
	}
	return c;
}

JSObject.alertObject = function (obj) {
	var s = 'Object\n-----------';
	for (var p in obj) {
		var v = obj[p];
		if (typeof(v) == 'function') {
			continue;
		}
		s += '\n' + p + ': ' + v;
	}
	alert(s);
}



JSObject.clone = function(subject) {
	var obj = {};
	for (var p in subject) {
		var v = subject[p];
		if ( typeof(v) !== 'function' ) {
			obj[p] = v;
		}
	}
	return obj;
}