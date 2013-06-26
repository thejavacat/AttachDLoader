package schedmail.core;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SearchTerm;

import schedmail.interfaces.InboxGetters;
import schedmail.terms.AfterTerm;
import schedmail.terms.AllTerm;
import schedmail.util.Protocol;
import schedmail.util.Settings;
import schedmail.util.Util;

public class Inbox implements ConnectionListener {
	private final Protocol protocol;
	private final boolean ssl;
	private final String host;
	private final String user;
	private final String password;
	private final File path;
	
	private Session session;
	private Store store;
	private Folder folder;
	
	private boolean opened;
	
	public boolean open() throws AuthenticationFailedException, NoSuchProviderException, MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.imap.ssl.enable", Boolean.toString(ssl));
		
		session = Session.getInstance(props);
		
		store = session.getStore(protocol.getLowerCase());
		store.connect(host, user, password);
		
		folder = store.getFolder("INBOX");
		folder.addConnectionListener(this);
		folder.open(Folder.READ_ONLY);
		
		return opened = folder.isOpen();
	}
	public boolean close() throws MessagingException {
		folder.close(false);
		store.close();
		
		return opened = store.isConnected();
	}
	public boolean isOpen() {
		return opened;
	}
	
	public List<Message> getMessages(SearchTerm term) throws MessagingException {
		List<Message> list = new Vector<>();
		
		Message[] messages = folder.search(term);
		if (messages != null)
			for (Message m : messages)
				list.add(m);
		
		return list;
	}
	
	public List<Message> getAllMessages() throws MessagingException {
		return this.getMessages(new AllTerm());
	}
	public List<Message> getMessages(Date after) throws MessagingException {
		return this.getMessages(new AfterTerm(after));
	}
	public List<Message> getNewMessages() throws MessagingException {
		return this.getMessages(new Date(Settings.getInstance().getLastUpdated()));
	}
	
	public List<MimeBodyPart> getAttachments(Message message) throws IOException, MessagingException {
		List<MimeBodyPart> parts = new Vector<>();
		
		Object att = message.getContent();
		if (att instanceof MimeMultipart) {
			MimeMultipart content = (MimeMultipart) att;
			
			for (int i = 0; i < content.getCount(); i++)
				parts.add((MimeBodyPart) content.getBodyPart(i));
		}
		
		return parts;
	}
	public List<MimeBodyPart> getAttachments(Message message, String[] formats) throws IOException, MessagingException {
		List<MimeBodyPart> parts = new Vector<>();
		
		Object att = message.getContent();
		if (att instanceof MimeMultipart) {
			MimeMultipart content = (MimeMultipart) att;
			
			for (int i = 0; i < content.getCount(); i++) {
				MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);
				
				for (String f : formats) {
					if (Util.hasFormat(part, f)) {
						parts.add(part);
						break;
					}
				}
			}
		}
		
		return parts;
	}
	public Inbox(InboxGetters inbox) throws AuthenticationFailedException, NoSuchProviderException, MessagingException {
		protocol = inbox.getProtocol();
		ssl = inbox.getSSLState();
		host = inbox.getHost();
		user = inbox.getUser();
		password = inbox.getPassword();
		path = inbox.getPath();
		
		Util.initFolder(path);
		
		this.open();
	}
	
	public List<File> downloadAttachments(String[] formats) throws MessagingException, IOException {
		List<File> files = new Vector<>();
		
		List<Message> messages = this.getNewMessages();
		for (Message m : messages) {
			List<MimeBodyPart> parts = this.getAttachments(m, formats);
			
			for (MimeBodyPart p : parts) {
				File file = new File(path, MimeUtility.decodeText(p.getFileName()));
				
				p.saveFile(file);
				
				files.add(file);
			}
		}
		
		return files;
	}
	
	public void closed(ConnectionEvent arg0) {
		opened = false;
	}
	public void disconnected(ConnectionEvent arg0) {
		opened = false;
	}
	public void opened(ConnectionEvent arg0) {
		opened = true;
	}
}