package schedmail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import schedmail.impl.InboxSettingsImpl;
import schedmail.impl.InboxXML;
import schedmail.impl.InboxXMLWrapper;
import schedmail.impl.LinkSettingsImpl;
import schedmail.impl.LinkXML;
import schedmail.impl.LinkXMLWrapper;
import schedmail.interfaces.InboxGetters;
import schedmail.interfaces.InboxSettings;
import schedmail.interfaces.LinkGetters;
import schedmail.interfaces.LinkSettings;

public class Settings {
	private static Settings instance = new Settings();
	
	public static Settings getInstance() {
		return instance;
	}
	
	private final File defaultFolder;
	
	private final File settingsFolder;
	private final File cacheFolder;
	
	private final File configFile;
	private final File linkListFile;
	private final File inboxListFile;
	
	private Document linkListDocument;
	private Document inboxListDocument;
	
	private Properties config;
	private Element linkList;
	private Element inboxList;
	
	private boolean autocommit = true;
	
	private Settings() {
		defaultFolder = new File(System.getProperty("user.dir"));
		settingsFolder = new File(defaultFolder, "settings");
		cacheFolder = new File(defaultFolder, "cache");
		configFile = new File(settingsFolder, "config");
		linkListFile = new File(settingsFolder, "linklist");
		inboxListFile = new File(settingsFolder, "inboxlist");
		
		try {
			this.initialize();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (JDOMException e) {
			e.printStackTrace();
		}
	}
	
	private void initialize() throws IOException, JDOMException {
		Util.initFolder(defaultFolder);
		Util.initFolder(settingsFolder);
		Util.initFolder(cacheFolder);
		this.initConfigFile();
		this.initLinkListFile();
		this.initInboxListFile();
	}
	
	private void initConfigFile() throws IOException {
		this.loadDefaultConfig();
		
		if (Util.initFile(configFile))
			this.saveConfigToFile();
		else
			this.loadConfigFromFile();
	}
	private void initLinkListFile() throws IOException, JDOMException {
		if (Util.initFile(linkListFile)) {
			this.loadDefaultLinkList();
			this.saveLinkListToFile();
		}
		else
			this.loadLinkListFromFile();
	}
	private void initInboxListFile() throws IOException, JDOMException {
		if (Util.initFile(inboxListFile)) {
			this.loadDefaultInboxList();
			this.saveInboxListToFile();
		}
		else
			this.loadInboxListFromFile();
	}
	
	private void loadDefaultConfig() {
		config = new Properties();
		
		config.setProperty("update_interval", "1");
		config.setProperty("last_update", Long.toString(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
	}
	private void loadDefaultLinkList() {
		linkListDocument = new Document();
		linkList = new Element("linklist");
		linkListDocument.setRootElement(linkList);
	}
	private void loadDefaultInboxList() {
		inboxListDocument = new Document();
		inboxList = new Element("inboxlist");
		inboxListDocument.setRootElement(inboxList);
	}
	
	private void loadConfigFromFile() throws IOException {
		FileInputStream input = new FileInputStream(configFile);
		
		config.load(input);
		
		input.close();
	}
	private void loadLinkListFromFile() throws JDOMException, IOException {
		linkListDocument = new SAXBuilder().build(linkListFile);
		linkList = linkListDocument.getRootElement();
	}
	private void loadInboxListFromFile() throws JDOMException, IOException {
		inboxListDocument = new SAXBuilder().build(inboxListFile);
		inboxList = inboxListDocument.getRootElement();
	}
	
	private void saveConfigToFile() throws IOException {
		FileOutputStream output = new FileOutputStream(configFile);
		
		config.store(output, null);
		
		output.close();
	}
	public void saveLinkListToFile() throws IOException {
		FileOutputStream output = new FileOutputStream(linkListFile);
		
		new XMLOutputter(Format.getPrettyFormat()).output(linkListDocument, output);
		
		output.close();
	}
	public void saveInboxListToFile() throws IOException {
		FileOutputStream output = new FileOutputStream(inboxListFile);
		
		new XMLOutputter(Format.getPrettyFormat()).output(inboxListDocument, output);
		
		output.close();
	}
	
	public File getDefaultFolder() {
		return defaultFolder;
	}
	public File getSettingsFolder() {
		return settingsFolder;
	}
	public File getCacheFolder() {
		return cacheFolder;
	}
	
	public File getConfigFile() {
		return configFile;
	}
	public File getLinkListFile() {
		return linkListFile;
	}
	public File getInboxListFile() {
		return inboxListFile;
	}
	
	public Document getLinkListDocument() {
		return linkListDocument;
	}
	public Document getInboxListDocument() {
		return inboxListDocument;
	}
	
	public Properties getConfig() {
		return config;
	}
	public Element getLinkList() {
		return linkList;
	}
	public Element getInboxList() {
		return inboxList;
	}
	
	protected Element getInboxElement(String name) {
		List<Element> inboxes = inboxList.getChildren("inbox");
		
		for (Element inbox : inboxes)
			if (inbox.getAttributeValue("name").equals(name))
				return inbox;
		
		return null;
	}
	protected Element getLinkElement(URL url) {
		List<Element> links = linkList.getChildren("link");
		
		for (Element link : links)
			if (link.getAttributeValue("url").equals(url.toString()))
				return link;
		
		return null;
	}
	
	public InboxSettings createNewInbox() {
		InboxSettings inbox = new InboxSettingsImpl();
		
		inbox.setProtocol(Protocol.IMAP);
		inbox.setSSLState(true);
		inbox.setPath(defaultFolder);
		
		return inbox;
	}
	public LinkSettings createNewLink() {
		LinkSettings link = new LinkSettingsImpl();
		
		link.setPath(defaultFolder);
		
		return link;
	}
	
	public List<InboxSettings> getInboxes() {
		List<InboxSettings> inboxes = new Vector<>();
		
		List<Element> children = inboxList.getChildren("inbox");
		for (Element child : children)
			inboxes.add(new InboxXMLWrapper(child));
		
		return inboxes;
	}
	public List<LinkSettings> getLinks() {
		List<LinkSettings> links = new Vector<>();
		
		List<Element> children = linkList.getChildren("link");
		for (Element child : children)
			links.add(new LinkXMLWrapper(child));
		
		return links;
	}
	
	public InboxSettings getInbox(String name) {
		Element inbox = this.getInboxElement(name);
		
		return inbox == null ? null : new InboxXMLWrapper(inbox);
	}
	public LinkSettings getLink(URL url) {
		Element link = this.getLinkElement(url);
		
		return link == null ? null : new LinkXMLWrapper(link);
	}
	
	public void addInbox(InboxGetters inbox) throws IOException {
		if (this.getInbox(inbox.getInboxName()) == null) {
			inboxList.addContent(new InboxXML(inbox));
			
			if (autocommit)
				this.saveInboxListToFile();
		}
	}
	public void addLink(LinkGetters link) throws IOException {
		if (this.getLink(link.getURL()) == null) {
			linkList.addContent(new LinkXML(link));
			
			if (autocommit)
				this.saveLinkListToFile();
		}
	}
	
	public void removeInbox(String name) throws IOException {
		Element inbox = this.getInboxElement(name);
		
		if (inbox != null) {
			inboxList.removeContent(inbox);
			
			if (autocommit)
				this.saveInboxListToFile();
		}
	}
	public void removeLink(URL url) throws IOException {
		Element link = this.getLinkElement(url);
		
		if (link != null) {
			linkList.removeContent(link);
			
			if (autocommit)
				this.saveLinkListToFile();
		}
	}
	
	public int getUpdateInterval() {
		return new Integer(config.getProperty("update_interval"));
	}
	public long getLastUpdated() {
		return new Long(config.getProperty("last_update"));
	}
	
	public void setUpdateInterval(Integer hours) throws IOException {
		if (hours >= 1) {
			config.setProperty("update_interval", hours.toString());
			
			if (autocommit)
				this.saveConfigToFile();
		}
	}
	public void setLastUpdated(Long time) throws IOException {
		config.setProperty("last_update", time.toString());
		
		if (autocommit)
			this.saveConfigToFile();
	}
}