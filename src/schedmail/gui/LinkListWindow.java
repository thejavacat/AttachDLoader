package schedmail.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import schedmail.interfaces.LinkSettings;
import schedmail.interfaces.Listener;
import schedmail.util.Settings;

@SuppressWarnings("serial")
public class LinkListWindow extends JFrame implements ActionListener, Listener {
	private JList<URL> list = new JList<>();
	
	private JPanel crudButtonPanel = new JPanel();
	private JPanel controlButtonPanel = new JPanel();
	private JPanel placeholder = new JPanel();
	
	private JButton addButton = new JButton("Add");
	private JButton editButton = new JButton("Edit");
	private JButton removeButton = new JButton("Remove");
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	public LinkListWindow() {
		this.build();
		this.init();
		this.setSize(500, 250);
		this.setResizable(false);
	}
	
	private void build() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("List Window");
		
		JScrollPane scroll = new JScrollPane(list);
		
		this.getContentPane().add(crudButtonPanel, BorderLayout.EAST);
		this.getContentPane().add(controlButtonPanel, BorderLayout.SOUTH);
		this.getContentPane().add(scroll, BorderLayout.CENTER);
		
		crudButtonPanel.setLayout(new BoxLayout(crudButtonPanel, BoxLayout.Y_AXIS));
		controlButtonPanel.setLayout(new BoxLayout(controlButtonPanel, BoxLayout.X_AXIS));
		
		crudButtonPanel.add(addButton);
		crudButtonPanel.add(editButton);
		crudButtonPanel.add(removeButton);
		controlButtonPanel.add(placeholder);
		controlButtonPanel.add(okButton);
		controlButtonPanel.add(cancelButton);
		
		addButton.setMaximumSize(new Dimension(80, 27));
		editButton.setMaximumSize(new Dimension(80, 27));
		removeButton.setMaximumSize(new Dimension(80, 27));
		okButton.setPreferredSize(new Dimension(80, 27));
		cancelButton.setPreferredSize(new Dimension(80, 27));
		
		addButton.setActionCommand("add");
		editButton.setActionCommand("edit");
		removeButton.setActionCommand("remove");
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		
		addButton.addActionListener(this);
		editButton.addActionListener(this);
		removeButton.addActionListener(this);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	private void init() {
		Vector<URL> urls = new Vector<>();
		List<LinkSettings> links = Settings.getInstance().getLinks();
		
		for (LinkSettings link : links) {
			urls.add(link.getURL());
		}
		
		list.setListData(urls);
		
		if (urls.size() == 0) {
			editButton.setEnabled(false);
			removeButton.setEnabled(false);
		}
		else {
			editButton.setEnabled(true);
			removeButton.setEnabled(true);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Settings settings = Settings.getInstance();
		String command = e.getActionCommand();
		
		if (command.equalsIgnoreCase("add")) {
			LinkSettings link = settings.createNewLink();
			LinkSettingsWindow window = new LinkSettingsWindow(link);
			
			window.addListener(this);
			
			window.setVisible(true);
		} else if (command.equalsIgnoreCase("edit")) {
			LinkSettings link = settings.getLink(list.getSelectedValue());
			LinkSettingsWindow window = new LinkSettingsWindow(link);
			
			window.addListener(this);
			
			window.setVisible(true);
		} else if (command.equalsIgnoreCase("remove")) {
			int i = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm removing",
																		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if (i == JOptionPane.YES_OPTION) {
				try {
					settings.removeLink(list.getSelectedValue());
					this.init();
				}
				catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		} else if (command.equalsIgnoreCase("ok")) {
			this.dispose();
			
			JOptionPane.showMessageDialog(null, "Restart the application to apply changes");
		} else if (command.equalsIgnoreCase("cancel")) {
			this.dispose();
		}
	}
	
	public void update() {
		this.init();
	}
}