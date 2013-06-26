package schedmail.impl;

import java.io.File;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Element;

import schedmail.interfaces.InboxGetters;
import schedmail.interfaces.InboxSettings;
import schedmail.util.Protocol;
import schedmail.util.Settings;

@SuppressWarnings("serial")
public class InboxXML extends Element implements InboxSettings {
	private Attribute name = new Attribute("name", "");
	private Attribute protocol = new Attribute("protocol", "");
	private Attribute ssl = new Attribute("ssl", "");
	private Attribute host = new Attribute("host", "");
	private Attribute user = new Attribute("user", "");
	private Attribute password = new Attribute("password", "");
	private Attribute path = new Attribute("path", "");
	
	public InboxXML(InboxGetters inbox) {
		super("inbox");
		this.build();
		this.initialize(inbox);		
	}
	
	private void build() {
		this.setAttribute(name);
		this.setAttribute(protocol);
		this.setAttribute(ssl);
		this.setAttribute(host);
		this.setAttribute(user);
		this.setAttribute(password);
		this.setAttribute(path);
	}
	private void initialize(InboxGetters inbox) {
		this.setInboxName(inbox.getInboxName());
		this.setProtocol(inbox.getProtocol());
		this.setSSLState(inbox.getSSLState());
		this.setHost(inbox.getHost());
		this.setUser(inbox.getUser());
		this.setPassword(inbox.getPassword());
		this.setPath(inbox.getPath());
	}
	
	public String getInboxName() {
		String value = name.getValue();
		
		return value.length() == 0 ? null : value;
	}
	public Protocol getProtocol() {
		String value = protocol.getValue();
		
		return value.length() == 0 ? null : Protocol.valueOf(value);
	}
	public boolean getSSLState() {
		return Boolean.getBoolean(ssl.getValue());
	}
	public String getHost() {
		String value = host.getValue();
		
		return value.length() == 0 ? null : value;
	}
	public String getUser() {
		String value = user.getValue();
		
		return value.length() == 0 ? null : value;
	}
	public String getPassword() {
		String value = password.getValue();
		
		return value.length() == 0 ? null : value;
	}
	public File getPath() {
		String value = name.getValue();
		
		return value.length() == 0 ? null : new File(value);
	}
	
	public void setInboxName(String name) {
		this.name.setValue(name == null ? "" : name);
	}
	public void setProtocol(Protocol protocol) {
		this.protocol.setValue(protocol == null ? "" : protocol.toString());
	}
	public void setSSLState(boolean ssl) {
		this.ssl.setValue(Boolean.toString(ssl));
	}
	public void setHost(String host) {
		this.host.setValue(host == null ? "" : host);
	}
	public void setUser(String user) {
		this.user.setValue(user == null ? "" : user);
	}
	public void setPassword(String password) {
		this.password.setValue(password == null ? "" : password);
	}
	public void setPath(File path) {
		this.path.setValue(path == null ? "" : path.getAbsolutePath());
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