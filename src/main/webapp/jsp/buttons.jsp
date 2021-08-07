<!DOCTYPE html>

<%
	String groupName = (String) request.getAttribute("groupName");
%>

<html>
	<head>
		<link href="data:image/x-icon;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQEAYAAABPYyMiAAAABmJLR0T///////8JWPfcAAAACXBIWXMAAABIAAAASABGyWs+AAAAF0lEQVRIx2NgGAWjYBSMglEwCkbBSAcACBAAAeaR9cIAAAAASUVORK5CYII=" rel="icon" type="image/x-icon" />
		
<style type="text/css">
			body {
				padding-top: 8px;
			}
                        div.button {
                                margin: 20px 10%;
                                padding: 14px 15px;
                                text-align: center;
                                font-family: helvetica, arial, sans-serif;
                                font-size: 50px;
                                text-shadow: #000 0px 2px 2px;
                                background-color: #5b5b5b;
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
                </style>

		
		<script src="js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript">
			function dispatch(sender, name) {
				$(sender).addClass('button-active');
				jQuery.get('net?timestamp=' + new Date().getTime() +
						'&event=' + name, function (data) {
					if (!data.match(/^Ok\s/)) {
						$(sender).addClass('button-error');
					}
					
					$(sender).removeClass('button-active');
				});
			}
		</script>
		
		<title>Buttons</title>
	</head>
	<body>
		
		<script type="text/javascript">
			
			$(document).ready(function () {
				jQuery.get('buttoneventsjson?group=<%= groupName %>', function (data) {
					
					if (!data.result || !data.eventNames) {
						alert('Errors');
					}
					
					var html = '';
					
					for (var i = 0, c = data.eventNames.length; i < c; i++) {
						var event = data.eventNames[i];
						html += '<div class="button" onclick="dispatch(this, \'' + event + '\')">' + event + '</div>';
					}
					
					$('body').html(html);
					//$('div.button:first-child').addClass('first');
					//$('div.button:last-child').addClass('last');
				});
			});
			
		</script>
		
	</body>
</html>