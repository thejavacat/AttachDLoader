package schedmail.interfaces;

import java.io.File;
import java.net.URL;

public interface LinkSettings extends LinkGetters {
	public void setURL(URL url);
	public void setPath(File path);
	
	public void commit();
}