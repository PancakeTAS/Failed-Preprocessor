package de.pfannekuchen.preprocessor.gradle;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Gradle Subproject is a Folder with a Gradle Project, whose tasks can be executed without the use of anything
 * @author Pancake
 */
public class GradleSubproject {

	private File location;
	private File gradle;
	private boolean locked;
	private Queue<Runnable> queuedTasks = new LinkedList<>();
	
	/**
	 * Initializes a Subproject
	 * @param subprojectFolder Folder of the Subproject
	 */
	public GradleSubproject(File subprojectFolder, File gradle) {
		subprojectFolder.mkdirs(); // Create Subfolder
		this.location = subprojectFolder;
		this.gradle = gradle;
	}

	/**
	 * Executes a Task
	 * @param task Task to Execute
	 * @throws IOException Throws when the Process couldn't be executed
	 */
	public void executeTask(String task) throws IOException {
		if (locked) throw new IOException("Gradle Instance Locked Exception");
		locked = true;
		final File GRADLE = new File(gradle.listFiles()[0], "bin/gradle.bat");
		ProcessBuilder processBuilder = new ProcessBuilder(GRADLE.getAbsolutePath(), task, "--no-daemon");
		processBuilder.directory(location);
		processBuilder.inheritIO();
		processBuilder.start();
		locked = false;
		if (!queuedTasks.isEmpty()) new Thread(queuedTasks.poll(), "Task Worker").run(); // Run next Task if any
	}
	
	/**
	 * Executes a Task after the Project isn't locked anymore
	 * @param task Task to Execute
	 * @throws IOException Throws when the Process couldn't be executed
	 */
	public void executeTaskAfter(String task) throws IOException {
		if (!locked) {
			executeTask(task);
			return;
		}
		queuedTasks.add(() -> {
			try {
				executeTask(task);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	/** @return Locked State */
	public boolean isLocked() {
		return locked;
	}
	
	/** @return Location of the Subproject */
	public File getLocation() {
		return location;
	}

	/** @return Gradle Directory */
	public File getGradle() {
		return gradle;
	}
	
}
