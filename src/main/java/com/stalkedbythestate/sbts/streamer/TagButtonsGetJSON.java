package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.TagActionImpl;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.TagButton;
import com.stalkedbythestate.sbts.json.TagButtonsJSON;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

@WebServlet(urlPatterns={"/tagbuttonsgetjson"})
public class TagButtonsGetJSON extends HttpServlet {
	private static final long serialVersionUID = 483239419320902596L;
	private static final Logger logger = Logger
			.getLogger(TagButtonsGetJSON.class);
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		// Gson gson = new
		// GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		TagButtonsJSON tagButtonsJSON = new TagButtonsJSON(true);

		Map<String, TagButton> tagButtonMap = new TreeMap<String, TagButton>();
		for (Action action : sbtsConfig.getActionConfig().getActionList()) {
			if (!(action instanceof TagActionImpl))
				continue;

			if (logger.isDebugEnabled())
				logger.debug("Action is ACTION_ADD_TAG");

			TagActionImpl tagActionImpl = (TagActionImpl) action;

			if (tagActionImpl.getValidFor() != null
					&& tagActionImpl.getValidFor() != 0L) {
				if (logger.isDebugEnabled())
					logger.debug("Skipping");
				continue;
			}

			TagButton tagButton = tagButtonMap.get(tagActionImpl.getTagName());
			if (tagButton == null) {
				tagButton = new TagButton();
				if (logger.isDebugEnabled())
					logger.debug("tagName is: " + tagActionImpl.getTagName());
				tagButton.setTagName(tagActionImpl.getTagName());
			}

			if (action.getActionType() == ActionType.ACTION_ADD_TAG) {
				if (logger.isDebugEnabled())
					logger.debug("Action is seton");
				tagButton.setOnEventName(action.getEventName());
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Action is setoff");
				tagButton.setOffEventName(action.getEventName());
			}

			if (logger.isDebugEnabled())
				logger.debug("Putting (" + tagActionImpl.getTagName() + ", "
						+ tagButton + ")");
			tagButtonMap.put(tagActionImpl.getTagName(), tagButton);

		}

		tagButtonsJSON.setTagButtons(tagButtonMap.values());
		out.print(gson.toJson(tagButtonsJSON));
	}

}
