/**
 * Main event listener for ActionController
 * 
 * TODO: get rid of unused methods
 */

ActionFormListener = function() {

	this.urlChanged = function(input) {
		if (input.value.match(/^https\:\/\/[a-z0-9]+/i)) {
			jQuery('.VerifyHostnameAttribute').show();
		} else {
			jQuery('.VerifyHostnameAttribute').hide();
			jQuery('.VerifyHostnameAttribute input').attr('checked', false);
		}
	};

	this.actionTypeChanged = function(note) {
		var type = note.data.value;
		var action = note.data.action;

		// JSObject.alertObject(action);

		if (!action) {
			action = ActionHelper.createActionOfType(type);
		}

		var attributes = ActionHelper.getAttributes(action);

		ActionController.builder.buildAttributes(attributes);

		jQuery('#action_cameras > option').each(function() {
			if (action.cameraSet && action.cameraSet.hasValue(this.value)) {
				this.selected = true;
			}
		});

		if (action.phidgetActionType
				&& action.phidgetActionType.toLowerCase() != 'pulse') {
			jQuery('input[name="pulseTrain"]').parent().parent().hide();
		}

	};

	this.phidgetActionTypeChanged = function(note) {
		var type = note.sender.value;

		if (type.toLowerCase() != 'pulse') {
			jQuery('input[name="pulseTrain"]').parent().parent().hide();
		} else {
			jQuery('input[name="pulseTrain"]').parent().parent().show();
		}

	};

	this.didLoadAddAction = function(note) {
		var t = note.data.template;
		t.set('delay', '0');
		t.set('delayUnits', 'sec');
		t.set('hysteresisUnits', 'sec');
		t.set('hysteresis', '0');
		t.set('availableTagNames', ActionController.getAvailableTagNames());
	};

	this.didShowAddAction = function(note) {
		var action = note.data.subject;

		if (action) {
			var actionType = action.actionType;
			JSPostNotification('ActionTypeChanged', note.sender, {
				value : actionType,
				action : action
			});

			ActionController.dummy.times = action.validTimes;
			if (action.eventCounter != undefined)
				ActionController.builder.buildCounter(action.eventCounter);
			ActionController.builder.buildTimes(action.validTimes);

			// set selected options for lists
			jQuery('#action_trigger > option[value="' + action.eventName + '"]')
					.attr('selected', true);
			jQuery('#action_type > option[value="' + actionType + '"]').attr(
					'selected', true);

			if (action.profiles) {
				jQuery('#action_profiles > option').each(function() {
					if (action.profiles.hasValue(this.value)) {
						this.selected = true;
					}
				});
			}

			jQuery('#andMode > option').each(function() {
				if (action.andMode == this.value)
					this.selected = true;
				else
					this.selected = false;

			});

			if (action.videoType) {
				jQuery('#action_videotype > option').each(function() {
					if (action.videoType == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.methodType) {
				jQuery('#action_methodtype > option').each(function() {
					if (action.methodType == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.direction) {
				jQuery('#action_direction > option').each(function() {
					if (action.direction == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.phidgetActionType) {
				jQuery('#phidget_action_type > option').each(function() {
					if (action.phidgetActionType == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.delayUnits) {
				jQuery('#delayUnits > option').each(function() {
					if (action.delayUnits == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.hysteresisUnits) {
				jQuery('#hysteresisUnits > option').each(function() {
					if (action.hysteresisUnits == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.rfxcomCommand) {
				jQuery('#action_rfxcom > option').each(function() {
					if (action.rfxcomCommand == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.responseGroup) {
				jQuery('#action_responsegroup > option').each(function() {
					if (action.responseGroup == this.value) {
						this.selected = true;
					}
				});
			}

			jQuery('#action_pos_tags > option')
					.each(
							function() {
								if (action.positiveTagNames
										&& action.positiveTagNames
												.hasValue(this.value)) {
									this.selected = true;
								}
							});

			jQuery('#action_neg_tags > option')
					.each(
							function() {
								if (action.negativeTagNames
										&& action.negativeTagNames
												.hasValue(this.value)) {
									this.selected = true;
								}
							});

			jQuery('#actionUrl').each(
					function() {
						ActionController.listener.urlChanged(document
								.getElementById('actionUrl'));
						jQuery('.VerifyHostnameAttribute input').attr(
								'checked', !!action.verifyHostname);
					});

			if (actionType == 'DELETE TAG' && action.tagName) {
				// set tagName for DELETE_TAG
				jQuery('#delete_tagname > option').each(function() {
					if (action.tagName == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.freakName) {
				// set freakName for VIDEO
				jQuery('#action_freakname > option').each(function() {
					if (action.freakName == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.actionName) {
				// set actionName
				jQuery('#action_actionname > option').each(function() {
					if (action.actionName == this.value) {
						this.selected = true;
					}
				});
			}

			if (action.phidgetName) {
				// set actionName
				jQuery('#action_phidgets > option').each(function() {
					if (action.phidgetName == this.value) {
						this.selected = true;
					}
				});
			}
		}
	};

	this.shouldAddAction = function(note) {
		var tmp = ActionHelper.createActionOfType(note.data.type);
		var action = JSObject.merge(action, note.data);

		if (action.protocol && !action.verifyHostname) {
			action.verifyHostname = false;
		}

		if (ActionHelper.validate(action)) {
			// add times
			if (ActionController.dummy.times) {
				action.validTimes = ActionController.dummy.times;
			}

			if (ActionController.dummy.eventCounter != undefined) {
				action.eventCounter = ActionController.dummy.eventCounter;
			}

			Configuration.actionList.push(action);
			ActionController.builder.buildActionList(Configuration.actionList);
			ActionHelper
					.hiliteActionWithIndex(Configuration.actionList.length - 1);

			JSPostNotification('OnInfo', 'New action added: ' + action.name);

			return true;
		}
		// Return FALSE to JSNotificationCenter can stop calling
		// observers and notify the sender that validation failed.
		return false;
	}

	this.didLoadAddTime = function(note) {
		if (!ActionController.dummy.times) {
			ActionController.dummy.times = [];
		}
		ActionController.dummy.ranges = [];
	}

	this.shouldAddTime = function(note) {
		var dows = note.data;
		var validTime = {};
		validTime.times = ActionController.dummy.ranges;
		validTime.dows = [];

		var count = 1;
		for ( var d in dows) {
			var value = dows[d];
			if (value) {
				validTime.dows.push((count % 7) + 1);
			}
			count++;
		}

		var times = ActionController.dummy.times;
		times.push(validTime);
		ActionController.builder.buildTimes(times);

		// JSPostNotification('OnInfo', 'New time added to action');
	}

	this.didLoadAddTimeRange = function(note) {
	}

	this.shouldAddTimeRange = function(note) {
		var ranges = ActionController.dummy.ranges;
		ranges.push(note.data);
		ActionController.builder.buildTimeRanges(ranges);

		// JSPostNotification('OnInfo', 'New range added to time');
	}

	this.didLoadEditAction = function(note) {
		var t = note.data.template;
		var action = note.data.subject;

		t.setObject(action);
		t.set('availableTagNames', ActionController.getAvailableTagNames());
		t.set('nameDisabledAttribute', 'disabled');
		t.set('guestCheckAttribute', action.guest ? 'checked' : '');

		ActionController.inEditMode = true;
	}

	this.didShowEditAction = function(note) {
		var action = note.data.subject;

		if (action.freakName && action.cameraSet) {
			this.renderCameraListForFreak(action.freakName);
		}

		this.didShowAddAction(note);
	}

	this.shouldEditAction = function(note) {
		var tmp = ActionHelper.createActionOfType(note.data.type);
		var action = JSObject.merge(action, note.data);

		var validateOptions = {
			skipUniqueName : true
		};

		if (ActionHelper.validate(action, validateOptions)) {
			// add times
			if (ActionController.dummy.times) {
				action.validTimes = ActionController.dummy.times;
			}

			if (ActionController.dummy.eventCounter != undefined) {
				action.eventCounter = ActionController.dummy.eventCounter;
			}

			var index = ActionController.replaceActionByName(action.name,
					action);
			ActionController.builder.buildActionList();
			ActionHelper.hiliteActionWithIndex(index);

			// JSPostNotification('OnInfo', 'Edited action: ' + action.name);

			ActionController.inEditMode = false;
			return true;
		}

		return false;
	}

	this.removeAction = function(note) {
		ActionController.removeActionByName(note.data.name);
		ActionController.builder.buildActionList();

		// JSPostNotification('OnWarning', 'Removed action: ' + note.data.name);
	}

	this.editAction = function(note) {
		var actionName = note.data.name;
		var action = ActionController.setDummyActionByName(actionName);
		JSDialog.openDialog('jsp/content/forms/action-new.html',
				note.sender, action, {
					buttonName : 'Save'
				});
	}

	this.didLoadAddParameter = function(note) {
		// JSObject.alertObject( note.subject );
	}

	this.shouldAddParameter = function(note) {
		var tmpl = new JSTemplate(
				'%{foreach, var:parameters, repeatString:<tr><td><span class="token">%key</span></td><td><span class="token">%value</span></td><td><input type="button" value="-" onclick="ActionHelper.removeRow(this)"</td></tr>}');

		var params = {};
		params[note.data.key] = note.data.value;

		tmpl.set('parameters', params);

		jQuery(note.sender).prev().find('tbody').append(tmpl.render());

		return true;
	}

	this.shouldExitPage = function(note) {
		// console.log( 'We can do some last minute stuff here...' );
	};

	this.renderCameraListForFreak = function(freak) {
		var remotes = ActionController.availableRemoteCameras[freak];
		tmpl = ActionHelper.getAttributeTemplate('cameraset-remote');
		tmpl.set('remoteCameras', remotes);
		var html = tmpl.render();

		jQuery('#cameraset-remote').replaceWith(html);
	};
}
