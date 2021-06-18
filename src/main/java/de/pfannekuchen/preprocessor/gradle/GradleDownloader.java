package de.pfannekuchen.preprocessor.gradle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
	 * @throws IOException Thrown whenever the Version is incorrect or a File couldn't be created
	 */
	public static File getGradleVersion(String version) throws IOException {
		// Create new Temporary File for Gradle Zip File
		final File zipdestination = File.createTempFile("gradle-download", version);
		zipdestination.deleteOnExit();
		final File gradledestination = new File(zipdestination.getParentFile(), "gradle-" + version + "/");
		if (!gradledestination.mkdirs()) return gradledestination; // Return Gradle if it is already downloaded.
		// Download Gradle to there
		final URL GRADLE_URL = new URL(URL_PATTERN.replaceFirst("version", version));
		Files.copy(GRADLE_URL.openStream(), zipdestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		// Unzip Gradle
		final ZipFile zipFile = new ZipFile(zipdestination);
		zipFile.extractAll(gradledestination.getAbsolutePath());
		return gradledestination;
	}
	
}
