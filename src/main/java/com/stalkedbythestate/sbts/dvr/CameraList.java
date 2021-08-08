package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class CameraList {
	private List<Camera> cameras;

	public CameraList() {
		cameras = new ArrayList<Camera>();
	}

	public List<Camera> getCameras() {
		return cameras;
	}

	public void setCameras(List<Camera> cameras) {
		this.cameras = cameras;
	}
	
}
