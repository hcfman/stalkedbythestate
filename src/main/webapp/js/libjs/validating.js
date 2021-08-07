/*
 * validating.js
 * 
 * Last Modified: 30/05/2011 
 */

Validator = function () {
	this.validators = {};
		
	this.addValidator = function ( name, method ) {
		if ( typeof(method) == 'function' ) {
			this.validators[name] = method;
		}
	};
	
	this.getValidator = function ( name ) {
		var validator = this.validators[name];
		if (!validator) {
			validator = function () {return false;}
		}
		return {check:validator};
	}
	
	this.isEmpty = function (value) {
		return value.match(/^\s*$/);
	}
	
	this.isNumeric = function (value) {
		return parseInt(value).toString().match(/^\d+$/);
	}
	
	this.isEmail = function (value) {
		return value.match(/^[a-z0-9._-]+@[a-z0-9.-]+\.[a-z]{2,4}(\.[a-z]{2,4})?/i);
	}
	
	this.isURL = function (value) {
		return value.match(/^(?:http|https):\/\/.+$/i);
	}
};

Validator = new Validator();