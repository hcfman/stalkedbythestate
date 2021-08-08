package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import java.util.List;

public class CameraList {
	private List<String> cameras;
	
	public void setCameras( List<String> cameras ) {
		this.cameras = cameras;
	}
	
	public List<String> getCameras() {
		return cameras;
	}

}
