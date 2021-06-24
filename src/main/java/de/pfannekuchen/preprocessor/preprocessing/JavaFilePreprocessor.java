package de.pfannekuchen.preprocessor.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
			File fileOut = new File(gradleproject.getLocation(), "src/main/java/" + localPath.toString());
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
		try {
			BufferedReader _reader = new BufferedReader(new FileReader(source));
			List<String> lines = _reader.lines().toList();
			List<String> out = new ArrayList<>();
			/* Loop trough all Lines and apply the Preprocessing to them */
			int isWorking = -1;
			int checkType = 0; // 0 = lower | 1 = lower or equal | 2 = exact | 3 = higher | 4 = higher or equal | 5 = not equal
			for (String line : lines) {
				String firstWord = line.trim().split(" ")[0].toLowerCase();
				if (firstWord.equalsIgnoreCase("//#if")) {
					isWorking = Integer.parseInt(line.split("\\/\\/#if ")[1].split(" ")[1]);
					String checkMessage = line.split("\\/\\/#if ")[1].split(" ")[0];
					switch (checkMessage) {
						case "<": {
							checkType = 0;
							break;
						} case "<=": {
							checkType = 1;
							break;
						} case "==": {
							checkType = 2;
							break;
						} case ">": {
							checkType = 3;
							break;
						} case ">=": {
							checkType = 4;
							break;
						} case "!=": {
							checkType = 5;
							break;
						}
					}
					System.out.println("Starting to drop lines...");
				} else if (firstWord.equalsIgnoreCase("//#endif") && isWorking != -1) {
					isWorking = -1;
					System.out.println("End-if found...");
				} else if (isWorking != -1) {
					if (checkType == 0 && version < isWorking) {
						out.add(line.replaceFirst("\\/\\/\\$\\$", ""));
					} else if (checkType == 1 && version <= isWorking) {
						out.add(line.replaceFirst("\\/\\/\\$\\$", ""));
					} else if (checkType == 2 && version == isWorking) {
						out.add(line.replaceFirst("\\/\\/\\$\\$", ""));
					} else if (checkType == 3 && version > isWorking) {
						out.add(line.replaceFirst("\\/\\/\\$\\$", ""));
					} else if (checkType == 4 && version >= isWorking) {
						out.add(line.replaceFirst("\\/\\/\\$\\$", ""));
					} else if (checkType == 5 && version != isWorking) {
						out.add(line.replaceFirst("\\/\\/\\$\\$", ""));
					}
				} else {
					out.add(line);
				}
				
			}
			_reader.close();
			System.out.println(destination.getAbsolutePath());
			destination.createNewFile();
			PrintWriter writer = new PrintWriter(new FileWriter(destination, false));
			for (String line : out) {
				writer.println(line);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
