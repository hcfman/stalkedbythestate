<%@ page import="java.util.List" %>

<script type="text/javaScript">

function selectAll() {
	$('form#stateChanger div.button').addClass('checked');
}

function unSelectAll() {
	$('form#stateChanger div.button').removeClass('checked');
}

function dispatch(sender) {
	$(sender).addClass('button-active');
	var oHash = {};
	$('div.button').each(function () {
		var button = jQuery(this);
		if ( button.hasClass('checked') ) {
			oHash[button.attr('id')] = 'On';
		}
	});
	
	$('input.toSubmit').each( function() {
		oHash[this.name] = this.value;
	});
	
	oHash['timestamp'] = new Date().getTime();
	
	jQuery.ajax({
		'type': 'POST',
		'url': 'changestate',
		'data' : oHash,
		'success': function(data) {
			if (!data.match(/^Ok\s/)) {
				$(sender).addClass('button-error');
			} else {
				location.assign('changestate');
			}
		
//			$(sender).removeClass('button-active');
		},
		'error' : function(data) {
			$(sender).addClass('button-error');
		}
	});

/*
	jQuery.post('net?event=', function (data) {
		if (!data.match(/^Ok\s/)) {
			$(sender).addClass('button-error');
		}
		
		$(sender).removeClass('button-active');
		
	});

*/
}

function toggleCheck(button) {
	var cb = jQuery(button);
	if (cb.hasClass('checked')) {
		cb.removeClass('checked');
	}else{
		cb.addClass('checked');
	}
}

</script>

<style type="text/css">
			body {
				padding-top: 8px;
			}
                        div.button {
                                margin: 20px 10%;
                                padding: 14px 15px 14px 130px;
                                text-align: left;
                                font-family: helvetica, arial, sans-serif;
                                font-size: 400%;
                                text-shadow: #000 0px 2px 2px;
                                background: #5b5b5b;
                                cursor: pointer;
                                border-radius: 6px;
                                -moz-border-radius: 6px;
                                -webkit-border-radius: 6px;
                                color: white;
                        }

                        div.button-active {
                                text-shadow: black 0px 1px 1px;
                                background: #0361ba url(images/ListItemBackgroundHilite.gif) repeat-x;
                        }

                        div.button-error {
                                text-shadow: black 0px 1px 1px;
                                background: #b90e03 url(images/ListItemBackgroundErrorHilite.gif) repeat-x;
                        }
                        
                        div.checked {
                        	background: #5b5b5b url(images/check-icon128.png) no-repeat 15px center;
                        }
                        
                        div.button > input { 
                        	height:100px; width:100px;
                        	vertical-align:middle;
                        }
                        
                        input.action {
                        	display:block;
                        	width:100%;
                        	height:100%;
                        	text-align:center;
                        	vertical-align:middle;
                        	font-size: 300%;
                        	background-color: #5b5b5b;
                        	
                        	border:none;
                        	padding: 14px 15px;
                        	
                        	cursor: pointer;
                            border-radius: 6px;
                            -moz-border-radius: 6px;
                            -webkit-border-radius: 6px;
                            color: white;
                            
                            margin-bottom: 20px;
                        }
                        
                        div#actions-div {
                        	margin: 20px 10%;
                        }
                        
                        table#actions {
                        	width:100%;
                        }
                </style>
	
<form id="stateChanger" method="POST">
<%
List<String> tagList = (List<String>) request.getAttribute("tagList");
int count = 1;
for (String tagName : tagList) {
%>
<div id="state<%= count %>" class="button" onclick="toggleCheck(this)">
<%= tagName %>
<input type="hidden" rel="state<%= count %>" name="values<%= count %>" value="<%= tagName %>" class="toSubmit">
</div>
<%
	count++;
}
%>

<div id="actions-div">
<table id="actions" cellpadding="0px" cellspacing="0px">
<tr>
<td width="50%" style="padding-right:10px;"><input class="action" type="button" value=" Select All " onClick="selectAll();"></td>
<td width="50%" style="padding-left:10px;"><input class="action" type="button" value=" Unselect All " onClick="unSelectAll();"></td>
</tr>
</table>

<input style="width:100%" class="action" type="button" value=" Submit " onClick="dispatch(this);">

</div>
</form>
