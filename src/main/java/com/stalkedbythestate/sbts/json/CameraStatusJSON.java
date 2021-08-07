package com.stalkedbythestate.sbts.json;

public class CameraStatusJSON {
	private String name;
	private int index;
	private boolean up;

	public CameraStatusJSON() {
	}

	public CameraStatusJSON(String name, int index, boolean up) {
		this.name = name;
		this.index = index;
		this.up = up;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	@Override
	public String toString() {
		return "CameraStatusJSON [index=" + index + ", name=" + name + ", up="
				+ up + "]";
	}

}
