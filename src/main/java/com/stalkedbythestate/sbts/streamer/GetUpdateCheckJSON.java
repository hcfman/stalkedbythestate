package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.stalkedbythestate.sbts.freakutils.ScriptRunner;
import com.stalkedbythestate.sbts.freakutils.ScriptRunnerResult;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.UpdateCheckJSON;

public class GetUpdateCheckJSON {
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	public GetUpdateCheckJSON(FreakApi freak, SbtsDeviceConfig sbtsConfig) {
		this.freak = freak;
		this.sbtsConfig = sbtsConfig;
	}

	public UpdateCheckJSON getUpdateCheckResult() throws Exception {
		return null;
	}
}
