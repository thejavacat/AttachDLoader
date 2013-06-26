package schedmail.tasks;

import java.io.File;
import java.util.Date;
//import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

//import javax.mail.AuthenticationFailedException;
//import javax.mail.MessagingException;
//import javax.mail.NoSuchProviderException;
import javax.swing.JOptionPane;

import schedmail.core.Inbox;
import schedmail.interfaces.InboxGetters;
import schedmail.util.Settings;
import schedmail.util.Unpacker;
import schedmail.util.Util;

public class InboxTask extends TimerTask {
	private InboxGetters config;
	
	public InboxTask(InboxGetters config) {
		this.config = config;
	}
	
	public void run() {
		try {
			Inbox inbox = new Inbox(config);
			
			if (inbox.isOpen()) {
				String[] formats = {".zip", ".xls", ".xlsx"};
				
				List<File> files = inbox.downloadAttachments(formats);
				for (File f : files) {
					if (f.exists()) {
						if (Util.endsWith(f.getName(), ".zip")) {
							Unpacker.unzip(f, config.getPath());
							f.delete();
						}
					}
				}
			}
			
			inbox.close();
			
			Settings.getInstance().setLastUpdated(new Date().getTime());
		}
		/*catch (AuthenticationFailedException e) {
			e.printStackTrace();
		}
		catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}*/
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
}