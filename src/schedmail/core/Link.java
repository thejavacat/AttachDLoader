package schedmail.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import schedmail.interfaces.LinkGetters;
import schedmail.util.Downloader;

public class Link {
	private final URL url;
	private final File path;
	
	public Link(LinkGetters link) {
		url = link.getURL();
		path = link.getPath();
	}
	
	public File download() throws IOException {
		return Downloader.download(url, path);
	}
}