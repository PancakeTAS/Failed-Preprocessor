package de.pfannekuchen.preprocessor;

import java.io.File;

import de.pfannekuchen.preprocessor.gradle.GradleDownloader;
import de.pfannekuchen.preprocessor.gradle.GradleSubproject;
import de.pfannekuchen.preprocessor.loader.FabricSetup;
import de.pfannekuchen.preprocessor.loader.FabricSetup.Fabric;
import de.pfannekuchen.preprocessor.loader.ForgeSetup;
import de.pfannekuchen.preprocessor.loader.ForgeSetup.Forge;
import de.pfannekuchen.preprocessor.loader.ForgeSetup.Mappings;

public class Preprocessor {

	public static final String JAVA16 = "C:/Program Files/AdoptOpenJDK/jdk-16.0.1.9-hotspot/";
	public static final String JAVA8 = "C:/Program Files/AdoptOpenJDK/jdk-8.0.275.1-openj9/";
	
	/** 
	 * Method used for Testing the Preprocessor
	 * @param args Ignored Arguments
	 */
	public static void main(String[] args) throws Exception {
		// Test Forge
		GradleSubproject mc12 = new GradleSubproject(new File("subprojects/1.12.2/"), GradleDownloader.getGradleVersion("4.10.3"), JAVA8);
		GradleSubproject mc11 = new GradleSubproject(new File("subprojects/1.11.2/"), GradleDownloader.getGradleVersion("4.10.3"), JAVA8);
		GradleSubproject mc10 = new GradleSubproject(new File("subprojects/1.10.2/"), GradleDownloader.getGradleVersion("4.10.3"), JAVA8);
		GradleSubproject mc9 = new GradleSubproject(new File("subprojects/1.9.4/"), GradleDownloader.getGradleVersion("4.10.3"), JAVA8);
		GradleSubproject mc8 = new GradleSubproject(new File("subprojects/1.8.9/"), GradleDownloader.getGradleVersion("4.10.3"), JAVA8);
		ForgeSetup.setupForge(mc8, Forge.MC1_8_9, Mappings.STABLE, "22", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod", JAVA8);
		ForgeSetup.setupForge(mc9, Forge.MC1_9_4, Mappings.SNAPSHOT, "20160518", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod", JAVA8);
		ForgeSetup.setupForge(mc10, Forge.MC1_10_2, Mappings.SNAPSHOT, "20161111", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod", JAVA8);
		ForgeSetup.setupForge(mc11, Forge.MC1_11_2, Mappings.SNAPSHOT, "20161220", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod", JAVA8);
		ForgeSetup.setupForge(mc12, Forge.MC1_12_2, Mappings.SNAPSHOT, "20171003", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod", JAVA8);
		mc12.queueTask("build");
		mc11.queueTask("build");
		mc10.queueTask("build");
		mc9.queueTask("build");
		mc8.queueTask("build");
		// Test Fabric
		GradleSubproject mc14 = new GradleSubproject(new File("subprojects/1.14.4/"), GradleDownloader.getGradleVersion("7.1"), JAVA16);
		GradleSubproject mc15 = new GradleSubproject(new File("subprojects/1.15.2/"), GradleDownloader.getGradleVersion("7.1"), JAVA16);
		GradleSubproject mc16 = new GradleSubproject(new File("subprojects/1.16.1/"), GradleDownloader.getGradleVersion("7.1"), JAVA16);
		GradleSubproject mc16_2 = new GradleSubproject(new File("subprojects/1.16.5/"), GradleDownloader.getGradleVersion("7.1"), JAVA16);
		GradleSubproject mc17 = new GradleSubproject(new File("subprojects/1.17/"), GradleDownloader.getGradleVersion("7.1"), JAVA16);
		FabricSetup.setupFabric(mc17, Fabric.MC1_17, "1.17+build.13", "LoTAS", "de.pfannekuchen.lotas", "1.0.0", "0.11.6", "0.35.2+1.17", JAVA16);
		FabricSetup.setupFabric(mc16_2, Fabric.MC1_16_5, "1.16.5+build.10", "LoTAS", "de.pfannekuchen.lotas", "1.0.0", "0.11.6", "0.35.1+1.16", JAVA16);
		FabricSetup.setupFabric(mc16, Fabric.MC1_16_1, "1.16.1+build.21", "LoTAS", "de.pfannekuchen.lotas", "1.0.0", "0.11.6", "0.18.0+build.387-1.16.1", JAVA16);
		FabricSetup.setupFabric(mc15, Fabric.MC1_15_2, "1.15.2+build.17", "LoTAS", "de.pfannekuchen.lotas", "1.0.0", "0.11.6", "0.28.5+1.15", JAVA16);
		FabricSetup.setupFabric(mc14, Fabric.MC1_14_4, "1.14.4+build.18", "LoTAS", "de.pfannekuchen.lotas", "1.0.0", "0.11.6", "0.28.5+1.14", JAVA16);
		mc17.queueTask("remapJar");
		mc16_2.queueTask("remapJar");
		mc16.queueTask("remapJar");
		mc15.queueTask("remapJar");
		mc14.queueTask("remapJar");
		System.out.println("All Tasks queued, this might take a while");
	}
	
}
