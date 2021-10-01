package de.pfannekuchen.preprocessor.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.pfannekuchen.preprocessor.gradle.GradleSubproject;

/**
 * Forge Environment Preparer
 * @author Pancake
 */
public class ForgeSetup {
	
	public static final String JVMARGS = "-Xmx1G -Xms128M -Xmn128m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseNUMA -XX:+CMSParallelRemarkEnabled -XX:MaxTenuringThreshold=15 -XX:MaxGCPauseMillis=30 -XX:GCPauseIntervalMillis=150 -XX:+UseAdaptiveGCBoundary -XX:-UseGCOverheadLimit -XX:+UseBiasedLocking -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MaxTenuringThreshold=15 -XX:+UseFastAccessorMethods -XX:+UseCompressedOops -XX:+OptimizeStringConcat -XX:+AggressiveOpts -XX:ReservedCodeCacheSize=2048m -XX:+UseCodeCacheFlushing -XX:SoftRefLRUPolicyMSPerMB=2000 -XX:ParallelGCThreads=10";
	
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
	 * @throws Exception Throws whenever Files couldn't be created
	 */
	public static void setupForge(GradleSubproject directory, Forge version, Mappings channel, String mappings, String modid, String modname, String modgroup, String modauthor, String modversion, String moddescription, String... javaHome) throws Exception {
		// Create important Files
		new File(directory.getLocation(), "src/main/java").mkdirs();
		new File(directory.getLocation(), "src/main/resources").mkdirs();
		new File(directory.getLocation(), "gradle.properties").createNewFile();
		Files.copy(new URL("http://data.mgnet.work/preprocessor/" + version.toString()).openStream(), new File(directory.getLocation(), "build.gradle").toPath(), StandardCopyOption.REPLACE_EXISTING);
		// Download gradle.properties, but change values in it
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://data.mgnet.work/preprocessor/" + version.toString() + ".properties").openStream()));
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
		directory.JVMARGS = JVMARGS;
		writer.println("org.gradle.jvmargs=" + JVMARGS);
		if (javaHome.length == 1) writer.println("org.gradle.java.home=" + javaHome[0]);
		writer.close();
		reader.close();
		// Check whether setupCiWorkspace has been executed yet
		// TODO: This not work something weird
		if (!new File(System.getProperty("user.home") + "/.gradle/caches/minecraft/net/minecraftforge/forge/" + forgeVersion + "/" + channel.name().toLowerCase() + "/" + mappings + "/").exists()) {
			System.err.println("Running setupCiWorkspace, please wait a minute or so before building.");
			directory.queueTask("setupCiWorkspace");
		}
	}
	
}
