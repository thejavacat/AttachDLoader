package schedmail.util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

public class Util {
	public static void copy(InputStream input, OutputStream output) throws IOException {
		int b;
		while ((b = input.read()) != -1)
			output.write(b);
	}
	public static String getFileName(URL url) {
		String path = url.toString();
		String file = path.substring(path.lastIndexOf('/') + 1);
		
		return file.length() == 0 ? null : file;
	}
	public static boolean canCreate(String filename) {
		int index = filename.lastIndexOf('.');
		
		return (index != -1) && (index < filename.length() - 1);
	}
	public static boolean canCreate(URL url) {
		String filename = Util.getFileName(url);
		
		if (filename != null) {
			return Util.canCreate(filename);
		}
		
		return false;
	}
	
	public static boolean initFolder(File folder) {
		boolean created = false;
		
		if (!folder.exists())
			created = folder.mkdirs();
		else if (folder.isFile()) {
			folder.delete();
			created = folder.mkdirs();
		}
		
		return created;
	}
	public static boolean initFile(File file) throws IOException {
		boolean created = false;
		
		if (!file.exists())
			created = file.createNewFile();
		else if (file.isDirectory()) {
			file.delete();
			created = file.createNewFile();
		}
		
		return created;
	}
	
	public static boolean endsWith(String string, String ending) {
		return string.substring(string.length() - ending.length()).equalsIgnoreCase(ending);
	}
	public static boolean hasFormat(BodyPart part, String format) throws MessagingException {
		String fileName = part.getFileName();
		
		if (fileName != null) {
			try {
				fileName = MimeUtility.decodeText(fileName);
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		if (fileName != null)
			return Util.endsWith(fileName, format);
		
		return false;
	}
}