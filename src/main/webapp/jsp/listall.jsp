<%@ page import="java.util.HashMap,com.stalkedbythestate.sbts.json.ViewJSON,com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType,java.util.TreeMap,org.apache.log4j.Logger,java.util.List,java.util.Map,java.util.SortedSet,java.util.Date,java.text.SimpleDateFormat,com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <link rel="stylesheet" href="css/sbts.css">
    <link rel="stylesheet" href="css/listing.css">
    <link media="handheld, only screen and (max-device-width: 480px)" rel="stylesheet" href="css/handheld.css">

    <script type="text/javascript" src="js/jquery.js"></script>

    <link rel="stylesheet" href="css/jquery-ui/jquery-ui.css">
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>

    <script type="text/javascript" src="js/sbts.js"></script>
</head>
<body>

<%
Long[] allEvents = (Long[]) request.getAttribute("allEvents");
Map<Long, String> eventDescMap = (Map<Long,String>) request.getAttribute("eventDescMap");
Map<Integer, CameraDevice> cameraDevices = (Map<Integer, CameraDevice>) request.getAttribute("cameras");
SortedSet<Integer> sortOrder = (SortedSet<Integer>) request.getAttribute("sortedSet");
TreeMap<Long, boolean[]> eventMap = (TreeMap<Long, boolean[]>) request.getAttribute("eventMap");
Logger logger = (Logger) request.getAttribute("logger");
Integer actualSize = (Integer) request.getAttribute("actualSize");
ViewJSON viewJSON = (ViewJSON) request.getAttribute("viewJSON");
VideoType videoType = (VideoType) request.getAttribute("videoType");
String[] cameraNames = (String[]) request.getAttribute("cameraNames");
%>
<script type="text/javaScript">
var offsetMapping = [<%
int count = 0;
for (int i : viewJSON.getOffsetMapping()) {
	if (count > 0)
		%>,<%
	count++;
	%><%= i %><%
}
%>];

var webPrefixes = [<%
count = 0;
for (String webPrefix : viewJSON.getWebPrefixes()) {
	if (count > 0)
		%>,<%
	count++;
	%>"<%= webPrefix %>"<%
}
%>];
</script>

<div id="frame">

<div id="container">
<div id="logo"><img alt="Stalked by the state" src="images/sbts.jpg" onclick="location.assign('.')"></div>

<div id="top-header">
	
	
	<img alt="Stalked by the state" src="images/sbts-title.jpg">
	
	<div class="hr"></div>
</div>

<div id="filters">
	<input type="checkbox" id="openEventInNewWindow" onclick="shouldEventsOpenInNewWindow(this)" checked="checked">&nbsp;Open video in new window
	<div id="trigger-daterange">
		<form action="" method="post">
			
			From <input id="trigger-datepicker-from" name="startdate" type="text" class="input-round" placeholder="From date">
			&nbsp;&nbsp;
			Till <input id="trigger-datepicker-till" name="enddate" type="text" class="input-round"  placeholder="Till date">
			&nbsp;
			<input type="submit" class="button" value="Search">
		
		</form>
	</div>
	
	<a id="filters-filter" class="button-disclosure-off" href="#"><span>Filter Options</span></a>
</div>

<div id="filter-overlay">
	<div class="content">
		<form action="/_list.htm#filter_applied" method="post" onsubmit="return false;">
		
		<table>
			<tbody>
				<tr>
					<td id="filter-on"><h1>Filter on:</h1></td>
					<td id="filter-events">
						
					</td>
				</tr>
				<tr>
					<td class="buttons" colspan="2"><input id="filters-filter-apply" type="button" class="button" value="Apply filter"></td>
				</tr>
			</tbody>
		</table>
		
		</form>
	</div>
</div>

<div id="main">
	<table id="trigger-events">
		<thead>
			<tr>
				<th>Time</th>
				<th colspan="5">Event</th>
			</tr>
		</thead>
		<tbody>
		<%
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		for ( Long eventValue : allEvents ) {
			Date date = new Date(eventValue);
			String dateString = format.format(date).replaceAll(" ", "&nbsp;");
			String day = dayFormat.format(date);
			
			String description = eventDescMap.get(eventValue);
		%>
			<tr>
				<td width="150"><div class="trigger-time"><%=dateString%></div></td>
				<td><div class="trigger-event"><%=description%></div></td>
				<%
				if (sortOrder.size() > 0) {
					int lastIndex = sortOrder.last();
					logger.debug("actualSize: " + actualSize);
					for (int camIndex = 0; camIndex < actualSize; camIndex++) {
						CameraDevice cameraDevice = cameraDevices.get(viewJSON.getOffsetMapping()[camIndex]);
				%>
						<td><% logger.debug("camIndex: " + camIndex);
						logger.debug("eventValue: " + eventValue);
						logger.debug("eventMap: " + eventMap.get(eventValue));
						if (!eventMap.get(eventValue)[camIndex]) {
						%>&nbsp;<%
						} else {
							String name = viewJSON.getCameraNames()[camIndex];
							int cam = viewJSON.getOffsetMapping()[camIndex];
						%><div class="trigger-cam cam<%=camIndex + 1%>"><input type="hidden" name="cam" value="<%=camIndex%>"><input type="hidden" name="event" value="<%=eventValue%>"><input type="hidden" name="day" value="<%=day%>"><%=name%></div><%
						}
						%>
						</td>
				<%
					}
				}
				%>
			</tr>
		<%
		}
		%>
		</tbody>
	</table>
</div>

</div>
</div>
</div>

<script type="text/javascript">
    window.shouldEventsOpenInNewWindow = function (checkbox) {
            if (checkbox.checked) {
                    jQuery('.trigger-cam').addClass('openInNewWindow');
            }else{
                    jQuery('.trigger-cam').removeClass('openInNewWindow');
            }
    };

	
	
	var filter 			= new TriggerFilter('#trigger-events'),
		events 			= filter.uniqueEvents,
		eventContainer	= $('#filter-events');
	
	// add all unique events to the container
	for (var i = 0, l = events.length; i < l; i++) {
		var e  = events[i];
		eventContainer.append('<div class="filter-trigger-event"><input type="checkbox" value="' + e + '">&nbsp;<label>' + e + '</label></div>');
	}
	
	// datepicker stuff
	$("#trigger-datepicker-from").datepicker();
	$("#trigger-datepicker-from").datepicker( "option", { dateFormat: "yy-mm-dd" } );
	$("#trigger-datepicker-till").datepicker();
	$("#trigger-datepicker-till").datepicker( "option", { dateFormat: "yy-mm-dd" } );
	
	// filter stuff
	$('#filter-overlay label').each( function () {
		$(this).click( function () {
			var p = $(this).prev();
			
			p 			= p[0];
			p.checked 	= !p.checked;
		});
	});
	
	$('#filters-filter').click( function () {
		$(this).toggleClass('button-disclosure-off button-disclosure-on');
		$('#filter-overlay').slideToggle('fast');
	});
	
	$('#filters-filter-apply').click( function () {
		$('#filters-filter').toggleClass('button-disclosure-off button-disclosure-on');
		
		var elements 	= $('#filter-overlay').slideToggle('fast').find('input[type=checkbox]:checked, input[type=text]');
		var data		= [];
		
		for (var i = 0, l = elements.length; i< l; i++) {
			data.push(elements[i].value);
		}
		
		filter.apply(data);
	});

function load( cam, day, event, newWindow ) {
	var url;
<%
	if (videoType == VideoType.MJPEG) {
%>
		url = webPrefixes[cam] + 'view?t=' + event + "&cam=" + offsetMapping[cam];
<%
	} else if (videoType == VideoType.PJPEG) {
%>
		url = webPrefixes[cam] + 'pview?t=' + event + "&cam=" + offsetMapping[cam];
<%
	} else if (videoType == VideoType.WEBM) {
%>
		url = webPrefixes[cam] + "webm/" + offsetMapping[cam] + "/" + day + "/" + event + ".htm";
<%
	}
%>
    if (newWindow) {
            window.open(url, event.replace(' ', '-'), 'toolbar=false')
    }else{
            location.assign(url);
    }

}

$( document ).ready( function() {
    jQuery('.trigger-cam').addClass('openInNewWindow');
	$('.trigger-cam').click( function() {
	    var event = $( this ).find( 'input[name=event]' ).eq( 0 ).val();
	    var cam = $( this ).find( 'input[name=cam]' ).eq( 0 ).val();
	    var day = $( this ).find( 'input[name=day]' ).eq( 0 ).val();
	    var self = $( this ).addClass( 'visited' );
	    load( cam, day, event, self.hasClass('openInNewWindow'));
	});
} );
</script>

</body>
</html>
