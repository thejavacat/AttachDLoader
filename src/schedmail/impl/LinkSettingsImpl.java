package schedmail.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import schedmail.interfaces.LinkSettings;
import schedmail.util.Settings;

public class LinkSettingsImpl implements LinkSettings {
	private URL url;
	private File path;
	
	public URL getURL() {
		return url;
	}
	public File getPath() {
		return path;
	}
	
	public void setURL(URL url) {
		this.url = url;
	}
	public void setPath(File path) {
		this.path = path;
	}
	
	public void commit() {
		try {
			Settings.getInstance().addLink(this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}