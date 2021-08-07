package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/listmjpg"})
public class ListMjpg extends HttpServlet {
	private static final long serialVersionUID = 5340778751700698614L;
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;
	private ListService listService;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
//		listService = new ListService();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (freak == null)
			freak = Freak.getInstance();

		if (listService == null)
			listService = new ListService();

		listService.service(freak, VideoType.MJPEG, request, response);
	}

}
