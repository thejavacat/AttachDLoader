package schedmail.impl;

import java.io.File;
import java.io.IOException;

import schedmail.interfaces.InboxSettings;
import schedmail.util.Protocol;
import schedmail.util.Settings;

public class InboxSettingsImpl implements InboxSettings {
	private String name;
	private Protocol protocol;
	private boolean ssl;
	private String host;
	private String user;
	private String password;
	private File path;
	
	public String getInboxName() {
		return name;
	}
	public Protocol getProtocol() {
		return protocol;
	}
	public boolean getSSLState() {
		return ssl;
	}
	public String getHost() {
		return host;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public File getPath() {
		return path;
	}
	
	public void setInboxName(String name) {
		this.name = name;
	}
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	public void setSSLState(boolean ssl) {
		this.ssl = ssl;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPath(File path) {
		this.path = path;
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