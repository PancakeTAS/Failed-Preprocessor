package de.pfannekuchen.preprocessor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;

import de.pfannekuchen.preprocessor.gradle.GradleDownloader;
import de.pfannekuchen.preprocessor.gradle.GradleSubproject;
import de.pfannekuchen.preprocessor.loader.FabricSetup;
import de.pfannekuchen.preprocessor.loader.FabricSetup.Fabric;
import de.pfannekuchen.preprocessor.loader.ForgeSetup;
import de.pfannekuchen.preprocessor.loader.ForgeSetup.Forge;
import de.pfannekuchen.preprocessor.loader.ForgeSetup.Mappings;
import de.pfannekuchen.preprocessor.tasks.BuildAll;

/**
 * Gradle Plugin Entry Point
 * @author Pancake
 */
public class Preprocessor implements Plugin<Project> {
	
	public static Map<String, GradleSubproject> subprojects = new HashMap<>();
	
	@Override
	public void apply(Project project) {
		ModData modData = project.getExtensions().create("mod", ModData.class);
		PluginData pluginData = project.getExtensions().create("plugin", PluginData.class);
		project.afterEvaluate((_project) -> {
			System.out.println("Preparing Subprojects.. This might take while on the first run");
			try {
				/* Create Subprojects for all Versions */
				for (String version : pluginData.getVersions().get()) {
					System.out.println("Settings up " + version);
					// Check whether current version is Forge or Fabric
					if (Integer.parseInt(version.split("\\.")[1]) >= 14) { // XXX: This line would not work with Snapshots
						// Fabric
						GradleSubproject gradle = new GradleSubproject(new File(project.getBuildDir(), "mc/" + version + "/"), GradleDownloader.getGradleVersion("7.1"), pluginData.getJava16().get());
						String dashVersion = version.replaceAll("\\.", "_");
						FabricSetup.setupFabric(gradle, Fabric.valueOf("MC" + dashVersion), pluginData.getYarnMappings().get().get(dashVersion), modData.getModname().get(), modData.getModgroup().get(), modData.getModversion().get(), pluginData.getFabricLoaderVersion().get(), pluginData.getFabricApiVersion().get().get(dashVersion), pluginData.getJava16().get());
						subprojects.put(version, gradle);
					} else {
						// Forge
						GradleSubproject gradle = new GradleSubproject(new File(project.getBuildDir(), "mc/" + version + "/"), GradleDownloader.getGradleVersion("4.10.3"), pluginData.getJava8().get());
						String dashVersion = version.replaceAll("\\.", "_");
						String mappings = pluginData.getMcpMappings().get().get(dashVersion);
						ForgeSetup.setupForge(gradle, Forge.valueOf("MC" + dashVersion), Mappings.valueOf(mappings.split("_")[0].toUpperCase()), mappings.split("_")[1], modData.getModid().get(), modData.getModname().get(), modData.getModgroup().get(), modData.getModauthor().get(), modData.getModversion().get(), modData.getModdescription().get(), pluginData.getJava8().get());
						subprojects.put(version, gradle);
					}
				}
			} catch (Exception e) {
				System.err.println("COULDN'T SETUP PROJECTS");
				e.printStackTrace();
			}
		});
		project.getTasks().register("buildall", BuildAll.class).get().setGroup("build");
	}
	
	
	
	/**
	 * Class that holds all the Data about the Mod
	 * @author Pancake
	 */
	public abstract static class ModData {
		public abstract Property<String> getModid();
		public abstract Property<String> getModname();
		public abstract Property<String> getModversion();
		public abstract Property<String> getModgroup();
		public abstract Property<String> getModauthor();
		public abstract Property<String> getModdescription();
		
	}
	
	/**
	 * Class that holds all the Data for the Subprojects
	 * @author Pancake
	 */
	public abstract static class PluginData {
		public abstract Property<String> getJava8();
		public abstract Property<String> getJava16();
		public abstract Property<String> getFabricLoaderVersion();
		public abstract ListProperty<String> getVersions();
		public abstract MapProperty<String, String> getYarnMappings();
		public abstract MapProperty<String, String> getMcpMappings();
		public abstract MapProperty<String, String> getFabricApiVersion();
	}
	
}
