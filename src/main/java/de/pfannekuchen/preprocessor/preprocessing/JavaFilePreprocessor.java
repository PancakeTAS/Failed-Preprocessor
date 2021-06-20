package de.pfannekuchen.preprocessor.preprocessing;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;

import de.pfannekuchen.preprocessor.Preprocessor;

/**
 * A Preprocessor that reads through all Lines of a File, and modifies it to a Minecraft Version
 * @author Pancake
 */
public class JavaFilePreprocessor {

	/**
	 * Preprocess all Java Files in a directory and it's subdirectories
	 * @param srcFolder Source Folder (src/main/java)
	 */
	public static void processTree(File srcFolder) {
		srcFolder.listFiles(new FilenameFilter() {
			@Override public boolean accept(File dir, String name) {
				File file = new File(dir, name);
				if (file.isDirectory() && !srcFolder.getAbsolutePath().equals(file.getAbsolutePath())) file.listFiles(this);
				else if (name.toLowerCase().endsWith(".java")) processJavaFile(srcFolder, file);
				return true;
			}
		});
	}
	
	/**
	 * Process a Java File for every Minecraft Version
	 * @param srcFolderIn Source Folder of Input File
	 * @param fileIn Java File to preprocess
	 */
	public static void processJavaFile(File srcFolderIn, File fileIn) {
		Preprocessor.subprojects.forEach((version, gradleproject) -> {
			// XXX: This line would not work with Snapshots
			int versionNumber = Integer.parseInt(version.split("\\.")[0] + String.format("%02d", Integer.parseInt(version.split("\\.")[1])) + String.format("%02d", Integer.parseInt(version.split("\\.")[2])));
			Path localPath = srcFolderIn.toPath().relativize(fileIn.toPath());
			File fileOut = localPath.toFile();
			fileOut.getParentFile().mkdirs(); // Create Parent Directory
			processJavaFileForVersion(fileIn, fileOut, versionNumber);
		});
	}
	
	/**
	 * This Method will preprocess a File to a Minecraft Version
	 * @param source Source of the File
	 * @param destination Where the File should be stored
	 * @param version Minecraft Version of the new File X.XX.XX
	 */
	private static void processJavaFileForVersion(File source, File destination, int version) {
		// TODO
	}
	
}
