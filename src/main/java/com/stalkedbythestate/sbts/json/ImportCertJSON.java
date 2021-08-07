package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.List;

public class ImportCertJSON {
	private String alias;
	private String content;
	private boolean result;
	private List<String> messages = new ArrayList<String>();

	public ImportCertJSON() {
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "ImportCertJSON [alias=" + alias + ", content=" + content
				+ ", messages=" + messages + ", result=" + result + "]";
	}

}
