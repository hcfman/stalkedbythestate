package com.stalkedbythestate.sbts.sbtsdevice.config;

public class Progress {
	private int progress;
	private String message;
	private boolean cont;

	public Progress() {
	}

	public Progress(int progress, String message, boolean cont) {
		this.progress = progress;
		this.message = message;
		this.cont = cont;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isCont() {
		return cont;
	}

	public void setCont(boolean cont) {
		this.cont = cont;
	}

	@Override
	public String toString() {
		return "Progress [cont=" + cont + ", message=" + message
				+ ", progress=" + progress + "]";
	}

}
