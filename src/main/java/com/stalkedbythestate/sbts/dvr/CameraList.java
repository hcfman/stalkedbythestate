package com.stalkedbythestate.sbts.dvr;

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
