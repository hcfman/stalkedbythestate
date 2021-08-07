(function () {
	String.prototype.flatten = function () {
		return this.toLowerCase().replace(' ', '_');
	}
	
	// Array Remove - By John Resig (MIT Licensed)
	Array.prototype.remove = function(from, to) {
		var rest = this.slice((to || from) + 1 || this.length);
		this.length = from < 0 ? this.length + from : from;
		return this.push.apply(this, rest);
	};
	
	/**
	 * Check if the array contains the value
	 * @param value: The value to match
	 */
	Array.prototype.hasValue = function (value) {
		for (var i = 0, l = this.length; i < l; i ++) {
			if (this[i] == value) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Calls the callback on each element in the array.
	 * @param callback: function to call
	 */
	Array.prototype.each = function (callback) {
		for (var i = 0, l = this.length; i < l; i ++) {
			callback.call(this[i]);
		}
		return this;
	}
	/**
	 * Class to filter trigger events.
	 *
	 * Todo: Find a way to preserve spaces in a event name?
	 */
	var TriggerFilter 	= function (tableSelector) {
		
		// regex to clean eventnames
		this.clean			= /[\s\t\r\n#@\|!\?\$\^%&\(\)\-\[\]<>\*\/\\:]/ig
		this.context 		= $(tableSelector);
		this.uniqueEvents 	= [];
		/**
		 * Initializes the triggerfilter.
		 */
		this.init		= function () {
			var hash 	= {};
			var events	= this.uniqueEvents;
			var clean	= this.clean;
			
			this.context.find('tbody > tr').each( function () {
				var self 		= $(this),
					eventName 	= self.find('div.trigger-event').text().replace(clean, '');
				
				if (!hash[eventName]) {
					events.push(eventName);
				}
				
				hash[eventName] = 1;
				
				self.addClass('event_' + eventName);
			});
			
			events.sort();
		}
		/**
		 * Applies the filter.
		 * @param data: list of event names
		 */
		this.apply		= function (data) {
			if (data.length < 1) return;
			
			var selector 	= [],
				deselector	= [],
				context		= this.context[0];
			
			// buid a list of (de)selectors	
			data.each(function(){
				selector.push	( 'tr.event_' 		+ this.toString() 		);
				deselector.push	( 'tr:not(.event_' 	+ this.toString() + ')' );
			});
			
			// first hide the rows that don't match
			$( deselector.join(','), context ).each(function () {
				$(this).hide();
			});
			// show rows that do match
			$( selector.join(','), context ).each(function () {
				$(this).show();
			});
		}
		
		// Initialize
		this.init();
	}
	// make the TriggerFilter class available in a global scope
	window.TriggerFilter = TriggerFilter;
})();