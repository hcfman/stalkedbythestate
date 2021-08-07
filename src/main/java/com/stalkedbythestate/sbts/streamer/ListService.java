package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.ViewJSON;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class ListService implements Serializable {
	private static final long serialVersionUID = -6872207630519445402L;
	private static final Logger logger = Logger.getLogger(ListService.class);
	
	protected void service(FreakApi freak, VideoType videoType, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

		ListJSON listJSON = new ListJSON();
		ViewJSON viewJSON = listJSON.getJSON(false, freak, videoType, request, response);
		Map<Integer, CameraDevice> cameraDevices = listJSON.getCameraDevices();
		SortedSet<Integer> cameraSet = listJSON.getCameraSet();

		Long[] allEvents = new ArrayList<Long>(viewJSON.getEventMap().keySet()).toArray(new Long[0]);
		Arrays.sort(allEvents, Collections.reverseOrder());

		request.setAttribute("logger", logger);
		request.setAttribute("allEvents", allEvents);
		request.setAttribute("eventDescMap", viewJSON.getEventDescMap());
		request.setAttribute("cameras", cameraDevices);
		request.setAttribute("sortedSet", cameraSet);
		request.setAttribute("actualSize", viewJSON.getActualSize());
		request.setAttribute("eventMap", viewJSON.getEventMap());
		request.setAttribute("viewJSON", viewJSON);
		request.setAttribute("videoType", videoType);

		RequestDispatcher view = request.getRequestDispatcher("jsp/listall.jsp");
		view.forward(request, response);
	}

}
