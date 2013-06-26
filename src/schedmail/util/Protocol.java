package schedmail.util;


public enum Protocol {
	IMAP("imap", "IMAP"),
	POP3("pop3", "POP3");
	
	private final String lowerCase;
	private final String upperCase;
	
	private Protocol(String lowerCase, String upperCase) {
		this.lowerCase = lowerCase;
		this.upperCase = upperCase;
	}
	
	public String getLowerCase() {
		return lowerCase;
	}
	public String getUpperCase() {
		return upperCase;
	}
}