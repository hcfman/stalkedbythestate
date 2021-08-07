(function ($) {
	
	var Controller = {
		removeItem: function (button) {
			var collection = button.parentNode.parentNode;
			var $collection = $(collection);
		}
	}
	
	
	$.fn.editableCollection = function (addCallback) {
		
		if (typeof(addCallback) != 'function') {
			addCallback = function (){alert('No handler')};
		}
		
		return this.each( function () {
			
			var self = $(this);
			var buttonbarHtml = '<div class="buttonbar">\
					<div class="button add"></div>\
					<div class="button remove" onclick="_EditableCollectionController.removeItem(this)"></div>\
				</div>';
			
			self.addClass('sbts-collection');
			self.append( buttonbarHtml );
			self.find('.add').click(function () {
				var collection = button.parentNode.parentNode;
				var $collection = $(collection);
				$collectionaddCallback();
			});
		});
	}
	
	window._EditableCollectionController = controller;
})(jQuery)