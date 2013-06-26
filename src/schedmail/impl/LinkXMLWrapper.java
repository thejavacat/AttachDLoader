package schedmail.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom2.Element;

import schedmail.interfaces.LinkSettings;
import schedmail.util.Settings;

public class LinkXMLWrapper implements LinkSettings {
	private Element link;
	
	public LinkXMLWrapper(Element link) {
		this.link = link;
	}
	
	public URL getURL() {
		String value = link.getAttributeValue("url");
		
		try {
			return value.length() == 0 ? null : new URL(value);
		}
		catch (MalformedURLException e) {
			return null;
		}
	}
	public File getPath() {
		String value = link.getAttributeValue("path");
		
		return value.length() == 0 ? null : new File(value);
	}
	
	public void setURL(URL url) {
		link.getAttribute("url").setValue(url == null ? "" : url.toString());
	}
	public void setPath(File path) {
		link.getAttribute("path").setValue(path == null ? "" : path.getAbsolutePath());
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