package schedmail.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom2.Attribute;
import org.jdom2.Element;

import schedmail.interfaces.LinkGetters;
import schedmail.interfaces.LinkSettings;
import schedmail.util.Settings;

@SuppressWarnings("serial")
public class LinkXML extends Element implements LinkSettings {
	private Attribute url = new Attribute("url", "");
	private Attribute path = new Attribute("path", "");
	
	public LinkXML(LinkGetters link) {
		super("link");
		this.build();
		this.initialize(link);
	}
	
	private void build() {
		this.setAttribute(url);
		this.setAttribute(path);
	}
	private void initialize(LinkGetters link) {
		this.setURL(link.getURL());
		this.setPath(link.getPath());
	}
	
	public URL getURL() {
		String value = url.getValue();
		
		try {
			return value.length() == 0 ? null : new URL(value);
		}
		catch (MalformedURLException e) {
			return null;
		}
	}
	public File getPath() {
		String value = path.getValue();
		
		return value.length() == 0 ? null : new File(value);
	}
	
	public void setURL(URL url) {
		this.url.setValue(url == null ? "" : url.toString());
	}
	public void setPath(File path) {
		this.path.setValue(path == null ? "" : path.getAbsolutePath());
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