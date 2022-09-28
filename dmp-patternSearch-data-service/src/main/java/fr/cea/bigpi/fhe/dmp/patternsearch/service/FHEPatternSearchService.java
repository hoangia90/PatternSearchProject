package fr.cea.bigpi.fhe.dmp.patternsearch.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.cea.bigpi.fhe.dmp.patternsearch.fhe.CommandExecution;

@Service
public class FHEPatternSearchService {
	@Value("${application.seal.sealDir}")
	private String sealDir;
	@Value("${application.seal.dataDir}")
	private String dataDir;
	@Value("${application.seal.resultDir}")
	private String resultDir;
	@Value("${application.seal.keyDir}")
	private String keyDir;
	@Value("${application.seal.uploadDir}")
	private String uploadDir;
	@Value("${application.seal.sample}")
	private int sample;
	@Value("${application.seal.filename}")
	private String filename;
	@Value("${application.seal.encryptPath}")
	private String encryptPath;
	@Value("${application.seal.decryptPath}")
	private String decryptPath;
	@Value("${application.seal.decrypCheckedResulttPath}")
	private String decrypCheckedResulttPath;
	@Value("${application.seal.evaluatePath}")
	private String evaluatePath;

	CommandExecution ce;

	@PostConstruct
	public void init() {
		this.ce = new CommandExecution();
	}

	public String encrypt(String content, String dir, String filename) {
		System.out.println("HE Engine: Encrypting vector.... start");
		List<String> command = Arrays.asList(encryptPath, content, filename, dir, String.valueOf(sample), keyDir);
		System.out.println("HE Engine: Encrypting vector.... end");
		return ce.execCm2(command, sealDir);
	}

	public String decrypt(String dir, String filename) throws Exception {
		System.out.println("HE Engine: Decrypting vector.... start");
		List<String> command = Arrays.asList(decryptPath, dir + filename + ".ct", String.valueOf(sample), keyDir);
		String result_string = ce.execCm2(command, sealDir);
		if (!result_string.isEmpty()) {
			System.out.println(result_string);
			result_string = convertASCII2Data(result_string);
			System.out.println("HE Engine: Decrypting vector.... end");
			return result_string;
		} else {
			System.out.println("The return result is empty! Vector decrypting failed! \n "
					+ "HE Engine: Decrypting vector....end");
			return dir;
		}
	}

	public String createDir(String dir) {
		System.out.println("HE Engine: Creating directories....");
		List<String> command = Arrays.asList("mkdir", "-p", dir);
		return ce.execCm2(command, sealDir);
	}

	public String deleteDir(String dir) {
		System.out.println("HE Engine: Deleting " + dir + "....");
		List<String> command = Arrays.asList("rm", "-r", dir);
		return ce.execCm2(command, sealDir);
	}

	public String createDataDir(String uniqueID) {
		String path = dataDir + uniqueID + "/";
		createDir(path);
		System.out.println("HE Engine: Creating directory: " + path + "....");
		return path;
	}

	// Create data with randomUUID name
	public String createUUID4Data(String content, String filename) {
		String uniqueID = UUID.randomUUID().toString();
		String path = dataDir + uniqueID + "/";
		createDir(path);
		System.out.println("HE Engine: Creating directory: " + path + "....");
		content = convertData2ASCII(content);
		System.out.println(content);
		encrypt(content, path, filename);
		return path;
	}

	// Create DBID for data
	public String createID4Data(String content, int id) {
		String uniqueID = String.valueOf(id);
		String path = dataDir + uniqueID + "/";
		createDir(path);
		System.out.println("HE Engine: Creating data in " + path + "....");
		content = convertData2ASCII(content);
		System.out.println(content);
		encrypt(content, path, uniqueID);
		return path;
	}

	public String createData(String content, String dir, String filename) {
		System.out.println("HE Engine: Creating data in " + dir + "....");
		createDir(dir);
		content = convertData2ASCII(content);
		encrypt(content, dir, filename);
		return dir;
	}

	public static String convertData2ASCII(String content) {
		System.out.println("HE Engine: Converting content string to ASCII....");
		byte[] ascii = content.getBytes(StandardCharsets.US_ASCII);
		String asciiString = Arrays.toString(ascii);
		asciiString = asciiString.replace("[", "").replace("]", "").replace(", ", " ");
		return asciiString;
	}

	public static String convertASCII2Data(String asciiString) {
		System.out.println("HE Engine: Converting ASCII to content string...." + asciiString);
		String[] asciiStringSplited = asciiString.replace("\n", "").split("\\s+");
		int size = asciiStringSplited.length;
		byte[] asciiArray = new byte[size];

		for (int i = 0; i < size; i++) {
			// Try to fix this warning later !!!!
			try {
				asciiArray[i] = (byte) Integer.parseInt(asciiStringSplited[i]);
			} catch (NumberFormatException ex) { // handle your exception
				// Print something
			}
		}

		String content = new String(asciiArray, StandardCharsets.US_ASCII);
		// remove zero padding
		content = content.replaceAll("\u0000", "");
		return content;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String decryptCheckResult(String dir) throws Exception {
		System.out.println("HE Engine: Decrypting HE muti-bit array.... start");
		List<String> command = Arrays.asList(decrypCheckedResulttPath, dir, String.valueOf(sample), keyDir);
		String result_string = ce.execCm2(command, sealDir);
		if (!result_string.isEmpty()) {
			System.out.println(result_string);
			System.out.println("HE Engine: Decrypting vector.... end");
			return result_string;
		} else {
			System.out.println("The return result is empty! Vector decrypting failed! \n "
					+ "HE Engine: Decrypting vector....end");
			return dir;
		}
	}

	public boolean checkData(String source, ArrayList<String> data, String filename) throws Exception {
		System.out.println("HE Engine: Checking data in " + source + " and " + data + "....");
		ArrayList<String> cm = new ArrayList<>();
		cm.add(evaluatePath);
		cm.add(source);
		cm.addAll(data);
		cm.add(filename);
		cm.add(resultDir);
		cm.add(String.valueOf(sample));
		cm.add(keyDir);
		List<String> command = cm;
		System.out.println(resultDir);
		String result_string = ce.execCm2(command, sealDir);
		if (result_string == "done") {
			return true;
		} else {
			return false;
		}
	}

	public String getSealDir() {
		return sealDir;
	}

	public void setSealDir(String sealDir) {
		this.sealDir = sealDir;
	}

	public String getResultDir() {
		return resultDir;
	}

	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public String getKeyDir() {
		return keyDir;
	}

	public void setKeyDir(String keyDir) {
		this.keyDir = keyDir;
	}

	public int getSample() {
		return sample;
	}

	public void setSample(int sample) {
		this.sample = sample;
	}

}
