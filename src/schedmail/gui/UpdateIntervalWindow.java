package schedmail.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import schedmail.util.Settings;

@SuppressWarnings("serial")
public class UpdateIntervalWindow extends JFrame implements ActionListener {
	private int columns = 10;
	
	private JTextField field = new JTextField(columns);
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	public UpdateIntervalWindow() {
		this.build();
		this.init();
		this.pack();
		this.setResizable(false);
	}
	
	private void build() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Update Interval");
		
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
			.addComponent(field)
			.addGroup(layout.createSequentialGroup()
				.addComponent(okButton)
				.addComponent(cancelButton)
			)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addComponent(field)
			.addGroup(layout.createParallelGroup()
				.addComponent(okButton)
				.addComponent(cancelButton)
			)
		);
	}
	private void init() {
		this.setUpdateInterval(Settings.getInstance().getUpdateInterval());
	}
	private boolean check() {
		try {
			int interval = this.getUpdateInterval();
			
			if (interval < 1) {
				throw new NumberFormatException();
			}
		}
		catch (NumberFormatException e) {
			String message = "Wrong number format!";
			
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		return true;
	}
	
	public void commit() {
		try {
			Settings.getInstance().setUpdateInterval(this.getUpdateInterval());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public Integer getUpdateInterval() throws NumberFormatException {
		return new Integer(field.getText());
	}
	public void setUpdateInterval(Integer interval) {
		field.setText(interval.toString());
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equalsIgnoreCase("ok")) {
			if (this.check()) {
				this.commit();
				this.dispose();
				
				JOptionPane.showMessageDialog(null, "Restart the application to apply changes");
				//FIXME: Message shown even if values not changed
			}
		}
		else if (command.equalsIgnoreCase("cancel")) {
			this.dispose();
		}
	}
}