package de.foospace.homeControl.core.data;

public enum Message {
	CONNECT("connect"),
	DISCONNECT("disconnect"),
	OK("OK");
	
	private String message;

	Message(String message) {
		this.message = message;
	}

	public String message() {
		return message;
	}

}
