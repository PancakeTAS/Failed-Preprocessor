package de.pfannekuchen.preprocessor.gradle;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Gradle Subproject is a Folder with a Gradle Project, whose tasks can be executed without the use of anything
 * @author Pancake
 */
public class GradleSubproject {

	public String JVMARGS;
	private File location;
	private File gradle;
	private String javahome;
	private boolean locked;
	private Queue<Runnable> queuedTasks = new LinkedList<>();
	
	/**
	 * Initializes a Subproject
	 * @param subprojectFolder Folder of the Subproject
	 * @param java8 
	 */
	public GradleSubproject(File subprojectFolder, File gradle, String javahome) {
		subprojectFolder.mkdirs(); // Create Subfolder
		this.location = subprojectFolder;
		this.javahome = javahome;
		this.gradle = gradle;
	}

	/**
	 * Executes a Task
	 * @param task Task to Execute
	 */
	private void executeTask(String task) throws Exception {
		locked = true;
		final File GRADLE = new File(gradle.listFiles()[0], "bin/gradle.bat");
		ProcessBuilder processBuilder = new ProcessBuilder(GRADLE.getAbsolutePath(), task, "--parallel", "--build-cache", "--configure-on-demand", "--no-daemon");
		processBuilder.directory(location);
		processBuilder.environment().put("JAVA_HOME", javahome);
		processBuilder.environment().put("JAVA_OPTS", JVMARGS);
		processBuilder.inheritIO();
		processBuilder.start();
		if (!queuedTasks.isEmpty()) new Thread(queuedTasks.poll(), "Task Worker").run(); // Run next Task if any
		locked = false;
	}
	
	/**
	 * Executes a Task after the Project isn't locked anymore
	 * @param task Task to Execute
	 * @throws IOException Throws when the Process couldn't be executed
	 */
	public void queueTask(String task) throws Exception {
		queuedTasks.add(() -> {
			try {
				executeTask(task);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		if (!locked) new Thread(queuedTasks.poll(), "Task Worker").run();
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

	/** 
	 * Locks the Gradle Instance and queue all new Tasks, can be used if a Task has to be executed async
	 */
	public void lock() {
		locked = true;
	}

	/**
	 * Unlocks the Gradle Instance and executes the next queud Task
	 */
	public void unlock() {
		if (!queuedTasks.isEmpty()) new Thread(queuedTasks.poll(), "Task Worker").run();
	}
	
}
