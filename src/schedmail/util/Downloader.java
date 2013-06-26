package schedmail.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class Downloader {
	public static File download(InputStream input, File file) throws IOException {
		Util.initFile(file);
		
		FileOutputStream output = new FileOutputStream(file);
			Util.copy(input, output);
		output.close();
		
		return file;
	}
	public static File download(InputStream input, String file) throws IOException {
		return Downloader.download(input, new File(file));
	}
	public static File download(URL url, File folder) throws IOException {
		String file = Util.getFileName(url);
		
		if (file != null) {
			File f = new File(folder, file);
			
			InputStream input = url.openStream();
				f = Downloader.download(input, f);
			input.close();
			
			return f;
		}
		
		return null;
	}
	public static File download(URL url, String folder) throws IOException {
		return Downloader.download(url, new File(folder));
	}
	public static File download(String url, File folder) throws IOException {
		return Downloader.download(new URL(url), folder);
	}
	public static File download(String url, String folder) throws IOException {
		return Downloader.download(new URL(url), new File(folder));
	}
}