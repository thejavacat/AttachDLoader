import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import schedmail.gui.InboxListWindow;
import schedmail.gui.InboxSettingsWindow;
import schedmail.gui.LinkListWindow;
import schedmail.gui.LinkSettingsWindow;
import schedmail.gui.UpdateIntervalWindow;
import schedmail.interfaces.InboxSettings;
import schedmail.interfaces.LinkSettings;
import schedmail.tasks.InboxTask;
import schedmail.tasks.LinkTask;
import schedmail.util.Settings;

public class Main {
	private static Settings settings = Settings.getInstance();
	private static ActionListener listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if (command.equalsIgnoreCase("Add New Inbox")) {
				InboxSettings inbox = settings.createNewInbox();
				InboxSettingsWindow window = new InboxSettingsWindow(inbox);
				
				window.setVisible(true);
			} else if (command.equalsIgnoreCase("Add New Link")) {
				LinkSettings link = settings.createNewLink();
				LinkSettingsWindow window = new LinkSettingsWindow(link);
				
				window.setVisible(true);
			} else if (command.equalsIgnoreCase("Show Inboxes")) {
				InboxListWindow window = new InboxListWindow();
				
				window.setVisible(true);
			} else if (command.equalsIgnoreCase("Show Links")) {
				LinkListWindow window = new LinkListWindow();
				
				window.setVisible(true);
			} else if (command.equalsIgnoreCase("Set Update Interval")) {
				UpdateIntervalWindow window = new UpdateIntervalWindow();
				
				window.pack();
				window.setVisible(true);
			} else if (command.equalsIgnoreCase("Exit")) {
				System.exit(0);
				//FIXME: Implement normal exit
			}
		}
	};
	
	private static void initTray() throws AWTException {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage("a.png");
			PopupMenu menu = new PopupMenu();
			TrayIcon icon = new TrayIcon(image, "SchedMail", menu);
			
			tray.add(icon);
			
			MenuItem addInboxItem = new MenuItem("Add New Inbox");
			MenuItem addLinkItem = new MenuItem("Add New Link");
			MenuItem showInboxesItem = new MenuItem("Show Inboxes");
			MenuItem showLinksItem = new MenuItem("Show Links");
			MenuItem openSettingsItem = new MenuItem("Set Update Interval");
			MenuItem exitItem = new MenuItem("Exit");
			
			menu.add(addInboxItem);
			menu.add(addLinkItem);
			menu.add(showInboxesItem);
			menu.add(showLinksItem);
			menu.add(openSettingsItem);
			menu.add(exitItem);
			
			addInboxItem.addActionListener(listener);
			addLinkItem.addActionListener(listener);
			showInboxesItem.addActionListener(listener);
			showLinksItem.addActionListener(listener);
			openSettingsItem.addActionListener(listener);
			exitItem.addActionListener(listener);
		}
	}
	private static void start() {
		Timer timer = new Timer();
		
		List<InboxSettings> inboxes = settings.getInboxes();
		for (InboxSettings inbox : inboxes) {
			timer.schedule(new InboxTask(inbox), 1_000 * 60, settings.getUpdateInterval());
		}
		
		List<LinkSettings> links = settings.getLinks();
		for (LinkSettings link : links) {
			timer.schedule(new LinkTask(link), 1_000 * 60, settings.getUpdateInterval());
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			Main.initTray();
			Main.start();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
}