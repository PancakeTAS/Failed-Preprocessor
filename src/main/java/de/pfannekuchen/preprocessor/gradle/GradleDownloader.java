package de.pfannekuchen.preprocessor.gradle;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import net.lingala.zip4j.ZipFile;

/**
 * Downloads gradle versions and caches local ones
 * @author Pancake
 */
public class GradleDownloader {

	/** URL of the Gradle Download */
	private static final String URL_PATTERN = "https://services.gradle.org/distributions/gradle-version-bin.zip";
	
	/**
	 * Downloads a Gradle Distribution
	 * @param version Gradle version to download
	 * @throws Exception Thrown whenever the Version is incorrect or a File couldn't be created
	 */
	public static File getGradleVersion(String version) throws Exception {
		// Create new Temporary File for Gradle Zip File
		final File gradledestination = new File(System.getProperty("user.home"), "gradle-" + version + "/");
		final File gradlezip = new File(System.getProperty("user.home"), "gradle-" + version + ".zip");
		gradlezip.deleteOnExit();
		if (!gradledestination.mkdirs()) return gradledestination; // Return Gradle if it is already downloaded.
		// Download Gradle and Unzip Gradle
		Files.copy(new URL(URL_PATTERN.replaceFirst("version", version)).openStream(), gradlezip.toPath());
		new ZipFile(gradlezip).extractAll(gradledestination.getAbsolutePath());
		return gradledestination;
	}

}
