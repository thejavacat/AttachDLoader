package schedmail.tasks;

import java.io.File;
//import java.io.IOException;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import schedmail.core.Link;
import schedmail.interfaces.LinkGetters;
import schedmail.util.Unpacker;
import schedmail.util.Util;

public class LinkTask extends TimerTask {
	private LinkGetters config;
	
	public LinkTask(LinkGetters config) {
		this.config = config;
	}
	
	public void run() {
		Link link = new Link(config);
		
		try {
			File file = link.download();
			
			if (Util.endsWith(file.getName(), ".zip")) {
				Unpacker.unzip(file);
				file.delete();
			}
		}
		/*catch (IOException e) {
			e.printStackTrace();
		}*/
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
}