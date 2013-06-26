package schedmail.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import schedmail.interfaces.LinkSettings;
import schedmail.interfaces.Listener;
import schedmail.util.Util;

@SuppressWarnings("serial")
public class LinkSettingsWindow extends JFrame implements LinkSettings, ActionListener {
	private List<Listener> listeners = new Vector<>();
	
	private int columns = 30;
	
	private LinkSettings link;
	
	private JLabel urlLabel = new JLabel("URL");
	private JLabel pathLabel = new JLabel("Path");
	
	private JTextField urlField = new JTextField(columns);
	private JTextField pathField = new JTextField(columns);
	
	private JButton pathButton = new JButton("...");
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	private JPanel placeholder = new JPanel();
	
	public LinkSettingsWindow(LinkSettings link) {
		this.link = link;
		
		this.build();
		this.init();
		
		this.pack();
		this.setResizable(false);
	}
	
	private void build() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("LinkSettings");
		
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		pathButton.setActionCommand("path");
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		pathButton.addActionListener(this);
		
		pathField.setEditable(false);
		
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(urlLabel)
					.addComponent(pathLabel)
				)
				.addGroup(layout.createParallelGroup()
					.addComponent(urlField)
					.addGroup(layout.createSequentialGroup()
						.addComponent(pathField)
						.addComponent(pathButton)
					)
				)
			)
			.addGroup(layout.createSequentialGroup()
				.addComponent(placeholder)
				.addComponent(okButton)
				.addComponent(cancelButton)
			)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(urlLabel)
				.addComponent(urlField)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(pathLabel)
				.addComponent(pathField)
				.addComponent(pathButton)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(placeholder)
				.addComponent(okButton)
				.addComponent(cancelButton)
			)
		);
	}
	private void init() {
		this.setURL(link.getURL());
		this.setPath(link.getPath());
	}
	private boolean check() {
		URL url = this.getURL();
		
		if (url != null) {
			try {
				InputStream in = url.openStream();
				in.close();
			}
			catch (IOException e) {
				String message = "URL is unreachable!";
				
				JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
				
				return false;
			}
			
			if (!Util.canCreate(url)) {
				String message = "Can't parse filename!";
				
				JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
				
				return false;
			}
		}
		else {
			return false;
		}
		
		return true;
	}
	
	public void commit() {
		link.setURL(this.getURL());
		link.setPath(this.getPath());
		
		link.commit();
	}
	
	public URL getURL() {
		try {
			return new URL(urlField.getText());
		}
		catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		
		return null;
	}
	public File getPath() {
		return new File(pathField.getText());
	}
	
	public void setURL(URL url) {
		urlField.setText(url == null ? "" : url.toString());
	}
	public void setPath(File path) {
		pathField.setText(path.getAbsolutePath());
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