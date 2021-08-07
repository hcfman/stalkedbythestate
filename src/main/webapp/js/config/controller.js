(function (Global, jQuery) {

	ConfigController = function () {
			this.selectedEvent;
			
			
			this.commit = function () {
				
			}
			
			this.selectEvent = function (eventKey) {
				config.selectEvent(eventKey);
				this.updateActionForSelectedEvent();
			}
			
			this.selectFirstEvent = function () {
				jQuery('#configEvents tr:first-child').click();
			}
			
			this.updateEventList = function () {
				var config	= Config();
				var events	= config.events;
				
				var $tbody = jQuery('#configEvents');
				
				$tbody.find('tr').each( function () {
					if (this.className == 'no-delete') {
						return;
					}
					
					$(this).remove();
				}).end();
				
				var count = events.length;
	
				for ( var i = 0; i < count; i++ ) {
					var e	= events[i];
					var row = '<tr id="row_event_'+e.key.flatten()+'" onclick="ConfigController.selectEvent(\''+e.key+'\')">\
								<td>'+e.type+'</td><td>'+e.key+'</td>\
								</tr>';
	
					$tbody.append( row );
				}
			}
		}

})(window, jQuery);