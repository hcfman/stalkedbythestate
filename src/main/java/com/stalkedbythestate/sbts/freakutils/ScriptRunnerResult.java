package com.stalkedbythestate.sbts.freakutils;


public class ScriptRunnerResult {
	private int result;
	private String output;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "ScriptRunnerResult [output=" + output + ", result=" + result
				+ "]";
	}

}
