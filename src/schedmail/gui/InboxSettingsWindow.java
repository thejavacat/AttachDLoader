package schedmail.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import schedmail.core.Inbox;
import schedmail.interfaces.InboxSettings;
import schedmail.interfaces.Listener;
import schedmail.util.Protocol;
import schedmail.util.Settings;

@SuppressWarnings("serial")
public class InboxSettingsWindow extends JFrame implements InboxSettings, ActionListener {
	private List<Listener> listeners = new Vector<>();
	
	private int columns = 30;
	
	private InboxSettings inbox;
	
	private JLabel nameLabel = new JLabel("Name");
	private JLabel protocolLabel = new JLabel("Protocol");
	private JLabel hostLabel = new JLabel("Host");
	private JLabel userLabel = new JLabel("User");
	private JLabel passwordLabel = new JLabel("Password");
	private JLabel pathLabel = new JLabel("Path");
	
	private JTextField nameField = new JTextField(columns);
	private JComboBox<Protocol> protocolField = new JComboBox<Protocol>(Protocol.values());
	private JCheckBox sslField = new JCheckBox("Use SSL");
	private JTextField hostField = new JTextField(columns);
	private JTextField userField = new JTextField(columns);
	private JPasswordField passwordField = new JPasswordField(columns);
	private JTextField pathField = new JTextField(columns);
	
	private JButton pathButton = new JButton("...");
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	private JPanel placeholder = new JPanel();
	
	public InboxSettingsWindow(InboxSettings inbox) {
		this.inbox = inbox;
		
		this.build();
		this.init();
		
		this.pack();
		this.setResizable(false);
	}
	
	protected void build() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("InboxSettings");
		
		okButton.setActionCommand("ok");
		okButton.addActionListener(this);
		
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		
		pathButton.setActionCommand("path");
		pathButton.addActionListener(this);
		
		pathField.setEditable(false);
		
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()		//	Labels
					.addComponent(nameLabel)
					.addComponent(protocolLabel)
					.addComponent(hostLabel)
					.addComponent(userLabel)
					.addComponent(passwordLabel)
					.addComponent(pathLabel)
				)
				.addGroup(layout.createParallelGroup()		//	Fields
					.addComponent(nameField)
					.addGroup(layout.createSequentialGroup()
						.addComponent(protocolField)
						.addComponent(sslField)
					)
					.addComponent(hostField)
					.addComponent(userField)
					.addComponent(passwordField)
					.addGroup(layout.createSequentialGroup()
						.addComponent(pathField)
						.addComponent(pathButton)
					)
				)
			)
			.addGroup(layout.createSequentialGroup()		//	Buttons
				.addComponent(placeholder)
				.addComponent(okButton)
				.addComponent(cancelButton)
			)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)		//	Name
				.addComponent(nameLabel)
				.addComponent(nameField)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)		//	Protocol & SSL
				.addComponent(protocolLabel)
				.addComponent(protocolField)
				.addComponent(sslField)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)		//	Host
				.addComponent(hostLabel)
				.addComponent(hostField)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)		//	User
				.addComponent(userLabel)
				.addComponent(userField)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)		//	Password
				.addComponent(passwordLabel)
				.addComponent(passwordField)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)		//	Path
				.addComponent(pathLabel)
				.addComponent(pathField)
				.addComponent(pathButton)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)		//	Buttons
				.addComponent(placeholder)
				.addComponent(okButton)
				.addComponent(cancelButton)
			)
		);
	}
	protected void init() {
		this.setInboxName(inbox.getInboxName());
		this.setProtocol(inbox.getProtocol());
		this.setSSLState(inbox.getSSLState());
		this.setHost(inbox.getHost());
		this.setUser(inbox.getUser());
		this.setPassword(inbox.getPassword());
		this.setPath(inbox.getPath());
	}
	protected boolean check() {
		if (this.getInboxName() == null) {
			String message = "Empty \"Name\" Field!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		} else if (Settings.getInstance().getInbox(this.getInboxName()) != null) {
			String message = "Not unique inbox name!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		if (this.getHost() == null) {
			String message = "Empty \"Host\" Field!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		if (this.getUser() == null) {
			String message = "Empty \"User\" Field!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		if (this.getPassword() == null) {
			String message = "Empty \"Password\" Field!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		if (this.getPath() == null) {
			String message = "Empty \"Path\" Field!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		try {
			Inbox inbox = new Inbox(this);
			inbox.close();
		}
		catch (AuthenticationFailedException e) {
			String message = "Wrong User/Password pair!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		catch (NoSuchProviderException e) {
			String message = "Wrong Host or Bad Internet Connection";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		catch (MessagingException e) {
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		return true;
	}
	
	public void commit() {
		inbox.setInboxName(this.getInboxName());
		inbox.setProtocol(this.getProtocol());
		inbox.setSSLState(this.getSSLState());
		inbox.setHost(this.getHost());
		inbox.setUser(this.getUser());
		inbox.setPassword(this.getPassword());
		inbox.setPath(this.getPath());
		
		inbox.commit();
	}
	
	public String getInboxName() {
		String name = nameField.getText();
		
		return name.length() == 0 ? null : name;
	}
	public Protocol getProtocol() {
		return protocolField.getItemAt(protocolField.getSelectedIndex());
	}
	public boolean getSSLState() {
		return sslField.isSelected();
	}
	public String getHost() {
		String host = hostField.getText();
		
		return host.length() == 0 ? null : host;
	}
	public String getUser() {
		String user = userField.getText();
		
		return user.length() == 0 ? null : user;
	}
	public String getPassword() {
		String password = new String(passwordField.getPassword());
		
		return password.length() == 0 ? null : password;
	}
	public File getPath() {
		String path = pathField.getText();
		
		return path.length() == 0 ? null : new File(path);
	}
	
	public void setInboxName(String name) {
		nameField.setText(name);
	}
	public void setProtocol(Protocol protocol) {
		protocolField.setSelectedIndex(protocol == null ? 0 : protocol.ordinal());
	}
	public void setSSLState(boolean ssl) {
		sslField.setSelected(ssl);
	}
	public void setHost(String host) {
		hostField.setText(host);
	}
	public void setUser(String user) {
		userField.setText(user);
	}
	public void setPassword(String password) {
		passwordField.setText(password);
	}
	public void setPath(File path) {
		pathField.setText(path == null ? "" : path.getAbsolutePath());
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equalsIgnoreCase("ok")) {
			if (this.check()) {
				this.commit();
				this.dispose();
				this.notifyListeners();
			}
		} else if (command.equalsIgnoreCase("cancel")) {
			this.dispose();
		} else if (command.equalsIgnoreCase("path")) {
			JFileChooser fc = new JFileChooser();
			
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setSelectedFile(this.getPath());
			
			int value = fc.showOpenDialog(this);
			
			if (value == JFileChooser.APPROVE_OPTION) {
				this.setPath(fc.getSelectedFile());
			}
		}
	}
	
	private void notifyListeners() {
		for (Listener l : listeners) {
			l.update();
		}
	}
	public void addListener(Listener l) {
		listeners.add(l);
	}
}