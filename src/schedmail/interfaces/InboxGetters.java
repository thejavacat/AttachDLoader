package schedmail.interfaces;

import java.io.File;

import schedmail.util.Protocol;

public interface InboxGetters {
	public String getInboxName();
	public Protocol getProtocol();
	public boolean getSSLState();
	public String getHost();
	public String getUser();
	public String getPassword();
	public File getPath();
}