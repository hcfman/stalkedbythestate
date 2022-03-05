LinkBuilder = new (function () {
	this.generateUid = function () {
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1) + 
			'-' + (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	};
	
	this.build = function () {
		
		var form = jQuery('form#linkBuilder');
		var list = jQuery('tbody#buildLinks');
		
		var name 		= jQuery('input[name="name"]'		, form).val();
		
		if (name.match(/^\s*$/)) {
			JSPostNotification('OnError', 'Name cannot be empty');
			return false;
		}
		
		var listType 	= jQuery('select[name="listtype"]'	, form).val();
		var cameras 	= jQuery('select[name="cameras"]'	, form);
		var events 		= jQuery('textarea[name="events"]'	, form).val();
		var dateStart 	= jQuery('input[name="datestart"]'	, form).val();
		var dateEnd 	= jQuery('input[name="dateend"]'	, form).val();
		var timeStart 	= jQuery('select[name="timestart"]'	, form).val();
		var timeEnd 	= jQuery('select[name="timeend"]'	, form).val();
		
		// check when start time is set the end time is set as well.
		if ( timeStart != 'null' && timeEnd == 'null' ) {
			JSPostNotification('OnError', 'Please set the end time');
			return false;
		}
		
		// the other way around.
		if ( timeEnd != 'null' && timeStart == 'null' ) {
			JSPostNotification('OnError', 'Please set the start time');
			return false;
		}
		
		// check if end time is greater then start time.
		// FIXME: maybe get a better expression?
		var regexStripTime 	= /(^0)|\:/g;
		var startTimeVal 	= parseInt(timeStart.replace(regexStripTime, ''));
		var endTimeVal 		= parseInt(timeEnd.replace(regexStripTime, ''));
		
		if (startTimeVal >= endTimeVal) {
			JSPostNotification('OnError', 'Ending time must be greater then start time.');
			return false;
		}
		
		switch (listType) {
			case 'PJPEG':	listType = 'listpjpg'	; break;
			case 'WEBM'	:	listType = 'listwebm'	; break;
			default		:	listType = 'listmjpg'	; break;
		}
		
		var link 	= listType;
		var params 	= []; 
		this.createCamerasLink(params, cameras) 
		this.createEventsLink(params, events) +
		this.createDateLink(params, dateStart, dateEnd) + 
		this.createTimesLink(params, timeStart, timeEnd);
		
		if (params.length > 0) {
			link += '?' + params.join('&amp;');
		}
		
		console.log('Created link: ' + link);
		
		var isDuplicateItem = false;
		var uid 			= this.generateUid();
		
		var tmpl = SharedTemplates.getTemplate('link');
		tmpl.set('uid'	, uid);
		tmpl.set('name'	, name);
		tmpl.set('link'	, link);
		
		var items = list.find('li');
		items.each(function () {
			var self = jQuery(this);
			if (self.text() == name) {
				isDuplicateItem = true;
				self.replaceWith( tmpl.render() );
			}
		});
		
		if (!isDuplicateItem) {
			list.append( tmpl.render() );
		}
		
	};
	
	this.createCamerasLink = function (params, cameras) {
		if (cameras.length > 0) {
			var _groups = cameras.find('optgroup');
			var groups = {};
			var hasFreaks = false;
			
			_groups.each( function () {
				var g 		= jQuery(this);
				var label 	= g.attr('label');
				
				groups[label] = [];
				
				g.find('option').each( function () {
					if (this.selected) {
						if (label != 'sbts') {
							hasFreaks = true;
						}
						groups[label].push(this.value);
					}
				});
				 
			});
			
			this.createCameraGroupLinks(params, groups, hasFreaks);
		}
		
	};
	
	this.createCameraGroupLinks = function (params, groups, hasFreaks) {
		var link = 'cameralist=';
		var hasCams = false;
		
		if (groups['sbts'].length > 0) {
			link += (groups['sbts'].join(','));
			hasCams = true;
		}
		
		if (hasFreaks) {
			delete groups['sbts'];
			
			link += '&amp;freaklist=';
			var tmp = [];
			
			for (var label in groups) {
				var group = groups[label];
				if (group.length > 0) {
					hasCams = true;
					for (var i = 0, c = group.length; i < c; i++) {
						group[i] = (group[i]);
					}

					tmp.push( (label) + ':' + group.join(',') );
				}
			}
			
			if (tmp.length > 0) {
				// %20 == space
				link += tmp.join('%20');
			}
		}
		
		if (hasCams) {
			params.push(link);
		}
	};
	
	this.createEventsLink = function (params, events) {
		var events = events.split('\n');
		var newEvents = new Array();

		for (var i = 0, c = events.length; i < c; i++) {
			events[i] = events[i].trim();
			if (events[i].length > 0) {
				newEvents.push(encodeURIComponent(events[i]));
			}
		}

		if (newEvents.length > 0) {
			params.push('eventlist=' + newEvents.join(','));
		}
	};
	
	this.createDateLink = function (params, dateStart, dateEnd) {
		var tmp = [];
		
		if ( !dateStart.match(/^\s*$/) ) {
			tmp.push('startdate=' + (dateStart));
		}
		
		if ( !dateEnd.match(/^\s*$/) ) {
			tmp.push('enddate=' + (dateEnd));
		}
		
		if (tmp.length > 0) {
			params.push(tmp.join('&amp;'));
		}
	};
	
	this.createTimesLink = function (params, timeStart, timeEnd) {
		var tmp = [];
		
		console.log(arguments);
		
		if (timeStart == 'null') {
			// nothing to do then
			return;
		}

		tmp.push(timeStart);
		tmp.push('-' + timeEnd);
		
		params.push('timerange=' + tmp.join(''));
	};
	
	this.removeLink = function (uid) {
		jQuery('#' + uid).parent().parent().remove();
	};
	
	this.buildLinkList = function (list) {
		var html = '';
		var tmpl = SharedTemplates.getTemplate('link');
		
		for (var i = 0, c = list.length; i < c; i++) {
			tmpl.setObject( list[i] )
			tmpl.set( 'uid', this.generateUid() );
			
			html += tmpl.render();
			
			tmpl.reset();
		}
		
		jQuery('tbody#buildLinks').html( html );
	};
	
	this.createObjectFromList = function () {
		var a = new Array();
		jQuery('tbody#buildLinks a').each(function () {
			var obj = new Object();
			obj.name = this.name;
			obj.link = this.href;
			a.push(obj);
		});
		return {linkList:a};
	};
	
	this.saveLinks = function () {
		var links 	= this.createObjectFromList();
		var json 	= Configuration.toJSON(links);
		
		jQuery.ajax( {
            url : 'savelinks',
            type : 'POST',
            contentType: 'application/json',
            dataType: 'json',
            async:true,
            data:json,
            error: function (xhr) {
            	ShowActivity(false);

            	var reason = xhr.status + ' ' + xhr.statusText.toUpperCaseFirst();
            	JSPostNotification('OnError', 'Failed to save links<br>Reason: ' + reason );
            	
    			// TODO: do stuff with response...
            },
            success: function (response) {
            	if (response.result == true) {
            		JSPostNotification('OnInfo', 'Saved links' );
            	}else{
            		JSPostNotification('OnError', response.messages.join('<br>') );
            	}
            	
            	ShowActivity(false);
    		}
        });
	}
});