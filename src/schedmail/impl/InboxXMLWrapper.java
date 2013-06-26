package schedmail.impl;

import java.io.File;
import java.io.IOException;

import org.jdom2.Element;

import schedmail.interfaces.InboxSettings;
import schedmail.util.Protocol;
import schedmail.util.Settings;

public class InboxXMLWrapper implements InboxSettings {
	private Element inbox;
	
	public InboxXMLWrapper(Element inbox) {
		this.inbox = inbox;
	}
	
	public String getInboxName() {
		String value = inbox.getAttributeValue("name");
		
		return value.length() == 0 ? null : value;
	}
	public Protocol getProtocol() {
		String value = inbox.getAttributeValue("protocol");
		
		return value.length() == 0 ? null : Protocol.valueOf(value);
	}
	public boolean getSSLState() {
		return Boolean.parseBoolean(inbox.getAttributeValue("ssl"));
	}
	public String getHost() {
		String value = inbox.getAttributeValue("host");
		
		return value.length() == 0 ? null : value;
	}
	public String getUser() {
		String value = inbox.getAttributeValue("user");
		
		return value.length() == 0 ? null : value;
	}
	public String getPassword() {
		String value = inbox.getAttributeValue("password");
		
		return value.length() == 0 ? null : value;
	}
	public File getPath() {
		String value = inbox.getAttributeValue("path");
		
		return value.length() == 0 ? null : new File(value);
	}
	
	public void setInboxName(String name) {
		inbox.getAttribute("name").setValue(name == null ? "" : name);
	}
	public void setProtocol(Protocol protocol) {
		inbox.getAttribute("protocol").setValue(protocol == null ? "" : protocol.toString());
	}
	public void setSSLState(boolean ssl) {
		inbox.getAttribute("ssl").setValue(Boolean.toString(ssl));
	}
	public void setHost(String host) {
		inbox.getAttribute("host").setValue(host == null ? "" : host);
	}
	public void setUser(String user) {
		inbox.getAttribute("user").setValue(user == null ? "" : user);
	}
	public void setPassword(String password) {
		inbox.getAttribute("password").setValue(password == null ? "" : password);
	}
	public void setPath(File path) {
		inbox.getAttribute("path").setValue(path == null ? "" : path.getAbsolutePath());
	}
	
	public void commit() {
		try {
			Settings.getInstance().addInbox(this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}