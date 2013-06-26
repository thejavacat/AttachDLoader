package schedmail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unpacker {
	public static List<File> unzip(ZipInputStream input, File folder) throws IOException {
		List<File> files = new Vector<>();
		
		ZipEntry entry = null;
		File file = null;
		
		while ((entry = input.getNextEntry()) != null) {
			files.add(file = new File(folder, entry.getName()));
			
			if (entry.isDirectory()) {
				Util.initFolder(file);
			}
			else {
				Util.initFile(file);
				
				FileOutputStream output = new FileOutputStream(file);
					Util.copy(input, output);
				output.close();
			}
			
			input.closeEntry();
		}
		
		return files.size() == 0 ? null : files;
	}
	public static List<File> unzip(ZipInputStream input, String folder) throws IOException {
		return Unpacker.unzip(input, new File(folder));
	}
	public static List<File> unzip(ZipInputStream input) throws IOException {
		return Unpacker.unzip(input, Settings.getInstance().getDefaultFolder());
	}
	public static List<File> unzip(File file, File folder) throws IOException {
		List<File> files = null;
		
		ZipInputStream input = new ZipInputStream(new FileInputStream(file));
			files = Unpacker.unzip(input, folder);
		input.close();
		
		return files;
	}
	public static List<File> unzip(String file, File folder) throws IOException {
		return Unpacker.unzip(new File(file), folder);
	}
	public static List<File> unzip(File file, String folder) throws IOException {
		return Unpacker.unzip(file, new File(folder));
	}
	public static List<File> unzip(String file, String folder) throws IOException {
		return Unpacker.unzip(new File(file), new File(folder));
	}
	public static List<File> unzip(File file) throws IOException {
		return Unpacker.unzip(file, new File(Settings.getInstance().getDefaultFolder(), file.getName()));
	}
	public static List<File> unzip(String file) throws IOException {
		return Unpacker.unzip(new File(file));
	}
}