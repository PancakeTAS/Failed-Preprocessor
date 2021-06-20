package de.pfannekuchen.preprocessor.tasks;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import de.pfannekuchen.preprocessor.preprocessing.JavaFilePreprocessor;

/**
 * Task for Building all Subprojects
 * @author Pancake
 */
public class BuildAll extends DefaultTask {

	@TaskAction
	public void preprocessAndBuild() {
		/* Preprocess the File */
		JavaFilePreprocessor.processTree(new File(getProject().getProjectDir(), "src/main/java"));
	}
	
}
