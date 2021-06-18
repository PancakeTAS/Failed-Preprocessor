package de.pfannekuchen.preprocessor.forge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.pfannekuchen.preprocessor.gradle.GradleSubproject;

/**
 * Forge Environment Preparer
 * @author Pancake
 */
public class ForgeSetup {
	
	/**
	 * Forge Versions
	 * @author Pancake
	 */
	public static enum Forge {
		MC1_12_2, MC1_11_2, MC1_10_2, MC1_9_4, MC1_8_9;
	}
	
	/**
	 * Mappings Channel
	 * @author Pancake
	 */
	public static enum Mappings {
		SNAPSHOT, STABLE
	}
	
	/**
	 * Sets up a Forge Environment
	 * @param directory Gradle Subproject
	 * @param version Forge Version
	 * @param channel Mappings Channel
	 * @param mappings Mappings Version
	 * @throws IOException Throws whenever Files couldn't be created
	 * @throws MalformedURLException Throws whenever an Invalid Version is being used
	 */
	public static void setupForge(GradleSubproject directory, Forge version, Mappings channel, String mappings, String modid, String modname, String modgroup, String modauthor, String modversion, String moddescription, String... javaHome) throws MalformedURLException, IOException {
		// Remove Build/Bin Folder
		new File(directory.getLocation(), "bin").delete();
		new File(directory.getLocation(), "build").delete();
		// Create important Files
		new File(directory.getLocation(), "src/main/java").mkdirs();
		new File(directory.getLocation(), "src/main/resources").mkdirs();
		new File(directory.getLocation(), "gradle.properties").createNewFile();
		Files.copy(new URL("http://mgnet.work/cfg/" + version.toString()).openStream(), new File(directory.getLocation(), "build.gradle").toPath(), StandardCopyOption.REPLACE_EXISTING);
		// Download gradle.properties, but change values in it
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://mgnet.work/cfg/" + version.toString() + ".properties").openStream()));
		PrintWriter writer = new PrintWriter(new File(directory.getLocation(), "gradle.properties"));
		String _line;
		String forgeVersion = "%MC%-%FORGE%";
		while ((_line = reader.readLine()) != null) {
			if (_line.startsWith("minecraft_version")) forgeVersion = forgeVersion.replaceFirst("%MC%", _line.split("=")[1]);
			if (_line.startsWith("forge_version")) forgeVersion = forgeVersion.replaceFirst("%FORGE%", _line.split("=")[1]);
			writer.println(_line
				.replaceAll("%MODID%", modid)
				.replaceAll("%MODNAME%", modname)
				.replaceAll("%MODGROUP%", modgroup)
				.replaceAll("%MODAUTHOR%", modauthor)
				.replaceAll("%MODVERSION%", modversion)
				.replaceAll("%MODDESCRIPTION%", moddescription)
				.replaceAll("%MAPPINGSCHANNEL%", channel.toString().toLowerCase())
				.replaceAll("%MAPPINGSVERSION%", mappings)
			);
		}
		writer.println("org.gradle.jvmargs=-Xmx1250M");
		writer.println("org.gradle.daemon=false");
		if (javaHome.length == 1) writer.println("org.gradle.java.home=" + javaHome);
		writer.close();
		reader.close();
		// Check whether SetupDecompWorkspace has been executed yet
		if (!new File(System.getProperty("user.home") + "/.gradle/caches/minecraft/net/minecraftforge/forge/" + forgeVersion + "/" + channel.name().toLowerCase() + "/" + mappings + "/").exists()) {
			System.out.println("SetupDecompWorkspace hasn't been executed yet. Running in the background");
			new Thread(() -> {
				try {
					directory.executeTask("setupDecompWorkspace");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
}
