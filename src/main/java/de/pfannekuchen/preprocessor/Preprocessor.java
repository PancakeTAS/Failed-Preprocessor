package de.pfannekuchen.preprocessor;

import java.io.File;

import de.pfannekuchen.preprocessor.forge.ForgeSetup;
import de.pfannekuchen.preprocessor.forge.ForgeSetup.Forge;
import de.pfannekuchen.preprocessor.forge.ForgeSetup.Mappings;
import de.pfannekuchen.preprocessor.gradle.GradleDownloader;
import de.pfannekuchen.preprocessor.gradle.GradleSubproject;

public class Preprocessor {

	/** 
	 * Method used for Testing the Preprocessor
	 * @param args Ignored Arguments
	 */
	public static void main(String[] args) throws Exception {
		GradleSubproject mc12 = new GradleSubproject(new File("subprojects/1.12.2/"), GradleDownloader.getGradleVersion("4.10.3"));
		GradleSubproject mc11 = new GradleSubproject(new File("subprojects/1.11.2/"), GradleDownloader.getGradleVersion("4.10.3"));
		GradleSubproject mc10 = new GradleSubproject(new File("subprojects/1.10.2/"), GradleDownloader.getGradleVersion("4.10.3"));
		GradleSubproject mc9 = new GradleSubproject(new File("subprojects/1.9.4/"), GradleDownloader.getGradleVersion("4.10.3"));
		GradleSubproject mc8 = new GradleSubproject(new File("subprojects/1.8.9/"), GradleDownloader.getGradleVersion("4.10.3"));
		ForgeSetup.setupForge(mc8, Forge.MC1_8_9, Mappings.STABLE, "22", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod");
		ForgeSetup.setupForge(mc9, Forge.MC1_9_4, Mappings.SNAPSHOT, "20160518", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod");
		ForgeSetup.setupForge(mc10, Forge.MC1_10_2, Mappings.SNAPSHOT, "20161111", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod");
		ForgeSetup.setupForge(mc11, Forge.MC1_11_2, Mappings.SNAPSHOT, "20161220", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod");
		ForgeSetup.setupForge(mc12, Forge.MC1_12_2, Mappings.SNAPSHOT, "20171003", "lotas", "LoTAS", "de.pfannekuchen.lotas", "Pfannekuchen", "1.0.0", "Low Optimization Tool Assisted Speedrun Mod");
		mc12.executeTaskAfter("build");
		mc11.executeTaskAfter("build");
		mc10.executeTaskAfter("build");
		mc9.executeTaskAfter("build");
		mc8.executeTaskAfter("build");
	}
	
}
