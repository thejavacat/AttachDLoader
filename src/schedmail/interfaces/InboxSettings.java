package schedmail.interfaces;

import java.io.File;

import schedmail.util.Protocol;

public interface InboxSettings extends InboxGetters {
	public void setInboxName(String name);
	public void setProtocol(Protocol protocol);
	public void setSSLState(boolean ssl);
	public void setHost(String host);
	public void setUser(String user);
	public void setPassword(String password);
	public void setPath(File path);
	
	public void commit();
}