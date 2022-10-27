package fr.cea.bigpi.fhe.dap.patternsearch.fhe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

public class CommandExecution {

	private String sysPath;
	private String OS;
	
	public ProcessBuilder getProcessBuilder() {
		return processBuilder;
	}

	public void setProcessBuilder(ProcessBuilder processBuilder) {
		this.processBuilder = processBuilder;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	private ProcessBuilder processBuilder;
	private Process process;

	public CommandExecution() {
		this.sysPath = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
		this.OS = System.getProperty("os.name");
		
		this.processBuilder = new ProcessBuilder();
	}

	public String getSysPath() {
		return sysPath;
	}

	public void setSysPath(String sysPath) {
		this.sysPath = sysPath;
	}

	public String getOS() {
		return OS;
	}

	public void setOS(String oS) {
		OS = oS;
	}

	public boolean isWindows() {
		return getOS().startsWith("Windows");
	}

	public boolean isUnix() {
		return getOS().startsWith("Linux");
	}

	
//	public String execCm(String command) {
//		String content = "";
//		try {
//			Process process = Runtime.getRuntime().exec(command);
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			String line;
//			while ((line = reader.readLine()) != null) {
//				System.out.println(line);
//				content = content + "\n" + line;
//			}
//			reader.close();
//
//			// for debugging
////			BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
////			while ((line = errorReader.readLine()) != null) {
////				System.out.println(line);
////				content = content + "\n" + line;
////			}
////			errorReader.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return content;
//	}

	
	public String execCm2(List <String> command, String workDir) {
		String content = "";
		
		processBuilder.command(command);
        processBuilder.directory(new File(workDir));
		
		try {
			
			process = processBuilder.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				content = content + "\n" + line;
			}

			int exitCode = process.waitFor();
			System.out.println("\nExited with error code : " + exitCode);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return content;
	}
	
	
	// note: write unit tests later
	public static void main(String[] args) {
		
		List<String> command = new ArrayList<>();
		command.add("./tfhe-license_check-encrypt");
		command.add("7475");
		command.add("data/1/");
		command.add("tfhe.sk");
		
		
		CommandExecution ce = new CommandExecution();
		ce.execCm2(command, "/home/hgnguyen/workspace-spring-tool-suite-4-4.9.0.RELEASE/DrivingLicenseCheckingProject/DrivingLicenseDataService/cingulata");
		
//		String content = "";
		
//		ProcessBuilder processBuilder = new ProcessBuilder();
//		processBuilder.command("./tfhe-license_check-encrypt", "8475591", "data/1/", "tfhe.sk");
//        processBuilder.directory(new File("/home/hgnguyen/Bureau/DrivingLicenseCheck/cingulata"));
        
	}

}
