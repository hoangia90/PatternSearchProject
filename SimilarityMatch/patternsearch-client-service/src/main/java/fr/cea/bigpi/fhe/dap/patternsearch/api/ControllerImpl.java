package fr.cea.bigpi.fhe.dap.patternsearch.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import fr.cea.bigpi.fhe.dap.patternsearch.fhe.FHEGenericService;
import fr.cea.bigpi.fhe.dap.patternsearch.fhe.FHESimilarityMatchingService;
import fr.cea.bigpi.fhe.dap.patternsearch.helper.Tools;
import fr.cea.bigpi.fhe.dap.patternsearch.helper.Zip;
import fr.cea.bigpi.fhe.dap.patternsearch.message.ResponseMessage;
import fr.cea.bigpi.fhe.dap.patternsearch.model.Attribute;
import fr.cea.bigpi.fhe.dap.patternsearch.model.Data;
import fr.cea.bigpi.fhe.dap.patternsearch.model.DataUpdate;
import fr.cea.bigpi.fhe.dap.patternsearch.model.Description;
import fr.cea.bigpi.fhe.dap.patternsearch.model.FHEFileSystem;
import fr.cea.bigpi.fhe.dap.patternsearch.service.DataService;
import fr.cea.bigpi.fhe.dap.patternsearch.service.FilesStorageService;
import fr.cea.bigpi.fhe.dap.patternsearch.service.HMAC;
import io.swagger.annotations.ApiParam;

@Configuration
@RestController
public class ControllerImpl implements Controller {

	@Value("${application.fheServerAnalysis}")
	String fheServerAnalysis;
	@Value("${application.fheServerData}")
	String fheServerData;
	@Value("${application.seal.sealDir}")
	String sealDir;

	@Value("${application.hashkey.sk}")
	private String hashsk;

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ControllerImpl.class);

	@Autowired
	FHEGenericService fheGenericService;

	@Autowired
	FHESimilarityMatchingService fheSimilarityMatchingService;

	@Autowired
	FilesStorageService storageService;

	@Autowired
	DataService dataService;

	@Autowired
	HMAC hmac;

	@Autowired
	Zip zip;

	@Autowired
	Tools tools;

	// Used for DeepLab Demo - Begin
//	@Override
	public ResponseEntity<Description> createHashData(@RequestParam(name = "content", required = true) String content,
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "contractID", required = false) String contractID,
			@RequestParam(name = "dataType", required = true) Integer dataType,
			@RequestParam(name = "status", required = false) Integer status,
			@RequestParam(name = "description", required = false) String description) {
		try {
//				Path uploadDir = Paths.get(fheGenericService.getSealDir() + "client/upload/");

			String hmacSHA1lgorithm = "HmacSHA1";
			String key = hashsk;
			content = hmac.hmacWithApacheCommons(hmacSHA1lgorithm, content, key);

			String filename = "temp";
			content = fheGenericService.createLicense(content, fheGenericService.getUploadDir(), filename);
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(fheGenericService.getUploadDir() + filename + ".ct"));
			parameters.add("partnerID", partnerID);
			parameters.add("contractID", contractID);
			parameters.add("dataType", dataType);
			parameters.add("status", status);
			parameters.add("description", description);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
//				headers.set("Accept", "application/json");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Description> result = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.POST,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
			fheGenericService.deleteDir(fheGenericService.getUploadDir() + filename + ".ct");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<Description> updateHashData(@RequestBody(required = true) DataUpdate dataUpdate) {
		try {
			Integer Id = dataUpdate.getData_id();

			String hmacSHA1lgorithm = "HmacSHA1";
			String key = hashsk;
			String content = dataUpdate.getContent();
			content = hmac.hmacWithApacheCommons(hmacSHA1lgorithm, content, key);

			dataUpdate.setContent(null);
			String partnerID = dataUpdate.getPartner_id();
			String contractID = dataUpdate.getContract_id();
			Integer status = dataUpdate.getStatus();
			String description = dataUpdate.getDescription();

//			Path uploadDir = Paths.get(fheGenericService.getSealDir() + "client/upload/");
			String filename = "temp";
			fheGenericService.createLicense(content, fheGenericService.getUploadDir(), filename);
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(fheGenericService.getUploadDir() + filename + ".ct"));
			parameters.add("Id", Id);
			parameters.add("partnerID", partnerID);
			parameters.add("contractID", contractID);
			parameters.add("status", status);
			parameters.add("description", description);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "application/xml, application/json");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Description> result = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.PUT,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
			fheGenericService.deleteDir(fheGenericService.getUploadDir() + filename + ".ct");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<Description> deleteHashData(@RequestParam(name = "id", required = true) Integer id,
			@RequestParam(name = "partnerId", required = true) String partnerId) {
		return deleteData(id, partnerId);
	}

//	@Override
	public @ResponseBody ResponseEntity<String> checkHashContentAuto(@RequestParam("content") String content,
			@RequestParam("partnerID") String partnerID) {
		try {
			// encrypt
			String filename = "temp";

			String hmacSHA1lgorithm = "HmacSHA1";
			String key = hashsk;
			content = hmac.hmacWithApacheCommons(hmacSHA1lgorithm, content, key);

			fheGenericService.createLicense(content, fheGenericService.getUploadDir(), filename);
			Path path = Paths.get(fheGenericService.getUploadDir() + filename + ".ct");
			// upload
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(path.toString()));
			parameters.add("partnerID", partnerID);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> res1 = restTemplate.postForEntity(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/01-uploadEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
			fheGenericService.deleteDir(path.toString());
			// check
			if (res1.getStatusCode().is2xxSuccessful()) {
				ResponseEntity<byte[]> res2 = checkWithEncryptedFile(partnerID, res1.getBody().toString());
				// decrypt
				if (res2.getStatusCode().is2xxSuccessful()) {
					String path2 = fheGenericService.getResultDir() + "/" + res1.getBody() + ".ct";
					Files.write(Paths.get(path2), res2.getBody());
					String result2 = fheGenericService.decryptCheckResult(path2);
					fheGenericService.deleteDir(path2);
					return new ResponseEntity<String>(result2, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<String> uploadCSVFunctionFile(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "contractID", required = false) String contractID,
			@RequestParam(name = "dataType", required = true) Integer dataType,
			@RequestParam(name = "status", required = false) Integer status,
			@RequestParam(name = "description", required = false) String description) {
		String message = "";
		String content;
		String[] strArray;
		String[] rows;
		String[] columns;
		try {
			if (!file.isEmpty()) {
				content = new String(file.getBytes());
				rows = content.split("\n");
				strArray = new String[rows.length];
				for (int i = 0; i < rows.length; i++) {
					columns = rows[i].split("#");
					if (columns.length == 3) {
						strArray[i] = columns[1];
						createHashData(strArray[i], partnerID, contractID, dataType, status, description);
						message = message + "\n" + strArray[i];
					} else {
						throw new Exception("The number of columns must be 3!");
					}
				}
			} else {
				throw new Exception("The input file is empty!");
			}
			if (rows.length == 1) {
				message = "Uploaded the file successfully, " + rows.length + " functions was added!" + ": " + message;
			} else {
				message = "Uploaded the file successfully, " + rows.length + " functions were added!" + ": " + message;
			}
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			message = "Error: " + e.toString() + "\n Could not upload the file: " + file.getOriginalFilename() + "!";
			return new ResponseEntity<String>(message, HttpStatus.EXPECTATION_FAILED);
		}
	}
	// Used for DeepLab Demo - End

//	@PostMapping("/uploadfile")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			System.out.println(e.toString());
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

//	@PostMapping("/uploadfiles")
	public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) {
		String message = "";
		try {
			storageService.saveAll(files);
//	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
			message = "Uploaded the files successfully: ";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
//	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			message = "Could not upload all the files !";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

//	@Override
	public @ResponseBody ResponseEntity<Boolean> checkContentAuto(@RequestParam("content") String content,
			@RequestParam("partnerID") String partnerID) {
		try {
			// encrypt
			String filename = "temp";
			fheGenericService.createLicense(content, fheGenericService.getUploadDir(), filename);
			Path path = Paths.get(fheGenericService.getUploadDir() + filename + ".ct");
			// upload
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(path.toString()));
			parameters.add("partnerID", partnerID);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> requestIdResponse = restTemplate.postForEntity(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/01-uploadEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
			fheGenericService.deleteDir(path.toString());
			// check
			if (requestIdResponse.getStatusCode().is2xxSuccessful()) {
				String requestId = requestIdResponse.getBody();
				ResponseEntity<byte[]> zipResultResponse = checkWithEncryptedFile(partnerID, requestId);
				// decrypt
				if (zipResultResponse.getStatusCode().is2xxSuccessful()) {

					boolean isDirCreated = tools.creatDir(fheGenericService.getResultDir() + "/client_" + requestId);
					if (isDirCreated) {
						// Save Zip Result File
						String zipResultPath = fheGenericService.getResultDir() + "/client_" + requestId + "/"
								+ requestId + ".zip";
						Files.write(Paths.get(zipResultPath), zipResultResponse.getBody());
						// Unzip - start
						ArrayList<String> extractedFiles = zip.UnzipFile(zipResultPath,
								fheGenericService.getResultDir() + "/client_" + requestId);
						// Unzip - end
						Integer intResult = 0;
						for (int i = 0; i < extractedFiles.size(); i++) {
							try {
								intResult = intResult + Integer
										.parseInt((fheGenericService.decryptCheckResult(extractedFiles.get(i))).trim());
								System.out.println("halllooooo : " + intResult);
							} catch (NumberFormatException ex) {
								ex.printStackTrace();
							}
						}
						fheGenericService.deleteDir(zipResultPath);
						boolean boolResult = false;
						if (intResult > 0) {
							boolResult = true;
						}
						return new ResponseEntity<Boolean>(boolResult, HttpStatus.OK);
					} else {
						throw new Exception("Could not created the directory");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

//	@Override
	public @ResponseBody ResponseEntity<byte[]> encrypt(
			@RequestParam(name = "content", required = true) String content) {
		try {
			// String resultDir = fheGenericService.getResultDir();
//			Path uploadDir = Paths.get(fheGenericService.getUploadDir());
			String filename = "temp";
			fheGenericService.createLicense(content, fheGenericService.getUploadDir(), filename);
			Path path = Paths.get(fheGenericService.getUploadDir() + filename + ".ct");
			byte[] returnData = Files.readAllBytes(path);
//			fheGenericService.deleteDir(path.toString());
			return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public @ResponseBody ResponseEntity<String> uploadEncryptedFile(
			@RequestParam(name = "file", required = true) MultipartFile file,
			@RequestParam(name = "partnerID", required = true) String partnerID) {
		try {
			if (!file.isEmpty()) {
				byte[] bytes = file.getBytes();
				String filePath = fheGenericService.getUploadDir() + file.getOriginalFilename();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(bytes);
				stream.close();

				MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
				parameters.add("file", new FileSystemResource(filePath));
				parameters.add("partnerID", partnerID);
				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "multipart/form-data");
				headers.set("Accept", "text/plain");

				RestTemplate restTemplate = new RestTemplate();

				ResponseEntity<String> result = restTemplate.postForEntity(
						fheServerAnalysis + "/openapi/v1/crud-data-master/check/01-uploadEncryptedFile",
						new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
				fheGenericService.deleteDir(filePath);
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Could not upload the file!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public @ResponseBody ResponseEntity<byte[]> checkWithEncryptedFile(
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "requestID", required = true) String requestID) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("partnerID", partnerID);
			parameters.add("requestID", requestID);
			HttpHeaders headers = new HttpHeaders();
//		headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			byte[] result = restTemplate.postForObject(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/02-checkWithEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), byte[].class);

			return new ResponseEntity<byte[]>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<String> decryptCheckResult(@RequestParam(name = "file", required = true) MultipartFile file) {
		try {
			ResponseEntity<ResponseMessage> re = uploadFile(file);
			System.out.println(re);
			String source = fheGenericService.getUploadDir() + file.getOriginalFilename();
			System.out.println(source);
			String result = fheGenericService.decryptCheckResult(source);
			fheGenericService.deleteDir(source);
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<String> decryptData(@RequestParam(name = "file", required = true) MultipartFile file) {
		try {
			uploadFile(file);
			String fileextension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename());
			String filename = com.google.common.io.Files.getNameWithoutExtension(file.getOriginalFilename());
			String result = fheGenericService.decrypt(fheGenericService.getUploadDir(), filename, fileextension);
			String source = fheGenericService.getUploadDir() + file.getOriginalFilename();
			fheGenericService.deleteDir(source);
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<byte[]> downloadEncryptedFile(
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			HttpHeaders headers = new HttpHeaders();
			parameters.add("Id", Id);
			parameters.add("partnerID", partnerID);
			headers.set("Accept", "*/*");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<byte[]> result = restTemplate.exchange(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/downloadEncryptedFile", HttpMethod.POST,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), byte[].class);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<List<Data>> getAllData(@RequestParam(name = "partnerID", required = true) String partnerID) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "*/*");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<Data>> allData = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data/all?partnerID=" + partnerID, HttpMethod.GET,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers),
					new ParameterizedTypeReference<List<Data>>() {
					});
			return allData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<Description> createData(@RequestParam(name = "content", required = true) String content,
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "contractID", required = false) String contractID,
			@RequestParam(name = "dataType", required = true) Integer dataType,
			@RequestParam(name = "status", required = false) Integer status,
			@RequestParam(name = "description", required = false) String description) {
		try {
//			Path uploadDir = Paths.get(fheGenericService.getfheGenericServiceDir() + "client/upload/");
			String filename = "temp";
			fheGenericService.createLicense(content, fheGenericService.getUploadDir(), filename);
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(fheGenericService.getUploadDir() + filename + ".ct"));
			parameters.add("partnerID", partnerID);
			parameters.add("contractID", contractID);
			parameters.add("dataType", dataType);
			parameters.add("status", status);
			parameters.add("description", description);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
//			headers.set("Accept", "application/json");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Description> result = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.POST,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
			fheGenericService.deleteDir(fheGenericService.getUploadDir() + filename + ".ct");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<Description> updateData(@RequestBody(required = true) DataUpdate dataUpdate) {
//			@RequestParam(name = "drivingLicenseUpdate", required = true) DrivingLicenseUpdate drivingLicenseUpdate) {
		try {
			Integer Id = dataUpdate.getData_id();
			String content = dataUpdate.getContent();
			dataUpdate.setContent(null);
			String partnerID = dataUpdate.getPartner_id();
			String contractID = dataUpdate.getContract_id();
			Integer status = dataUpdate.getStatus();
			String description = dataUpdate.getDescription();

//			Path uploadDir = Paths.get(fheGenericService.getSealDir() + "client/upload/");
			String filename = "temp";
			fheGenericService.createLicense(content, fheGenericService.getUploadDir(), filename);
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(fheGenericService.getUploadDir() + filename + ".ct"));
			parameters.add("Id", Id);
			parameters.add("partnerID", partnerID);
			parameters.add("contractID", contractID);
			parameters.add("status", status);
			parameters.add("description", description);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "application/xml, application/json");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Description> result = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.PUT,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
			fheGenericService.deleteDir(fheGenericService.getUploadDir() + filename + ".ct");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<Description> deleteData(@RequestParam(name = "id", required = true) Integer id,
			@RequestParam(name = "partnerId", required = true) String partnerId) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("id", id);
			parameters.add("partnerId", partnerId);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "*/*");
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.exchange(fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.DELETE,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@Override
	public ResponseEntity<ArrayList<Integer>> createDataSet(
			@RequestParam(name = "content", required = true) String content,
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "contractID", required = false) String contractID,
			@RequestParam(name = "dataType", required = true) Integer dataType,
			@RequestParam(name = "status", required = false) Integer status,
			@RequestParam(name = "description", required = false) String description) {
		try {
			ArrayList<Integer> result = new ArrayList<Integer>();
//			for (int i = 0; i <= 101; i++) {
			for (int i = 0; i < 5000; i++) {
				ResponseEntity<Description> res = createData(content + "-" + i, partnerID, contractID, dataType, status,
						description);
				result.add(Integer.parseUnsignedInt(res.getBody().getValue()));
			}
			return new ResponseEntity<ArrayList<Integer>>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Data>> getAllDataSM(String partnerID) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "*/*");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<Data>> allData = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data/allEncryptedData?partnerID=" + partnerID,
					HttpMethod.GET, new HttpEntity<MultiValueMap<String, Object>>(parameters, headers),
					new ParameterizedTypeReference<List<Data>>() {
					});
			return allData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Description> createDataSM(@RequestParam(name = "content", required = true) String content,
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "contractID", required = false) String contractID,
			@RequestParam(name = "dataType", required = true) Integer dataType,
			@RequestParam(name = "status", required = false) Integer status,
			@RequestParam(name = "description", required = false) String description) {
		try {
//			Path uploadDir = Paths.get(fheSimilarityMatchingService.getfheSimilarityMatchingServiceDir() + "client/upload/");
			String filename = "temp";
			fheSimilarityMatchingService.encrypt(content, fheSimilarityMatchingService.getUploadDir(), filename);

			System.out.println("debugg!!!! " + content);
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file",
					new FileSystemResource(fheSimilarityMatchingService.getUploadDir() + filename + ".ct"));
			parameters.add("partnerID", partnerID);
			parameters.add("contractID", contractID);
			parameters.add("dataType", dataType);
			parameters.add("status", status);
			parameters.add("description", description);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
//			headers.set("Accept", "application/json");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Description> result = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.POST,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
			fheSimilarityMatchingService.deleteDir(fheSimilarityMatchingService.getUploadDir() + filename + ".ct");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Description> updateDataSM(@RequestBody(required = true) DataUpdate dataUpdate) {
//		@RequestParam(name = "drivingLicenseUpdate", required = true) DrivingLicenseUpdate drivingLicenseUpdate) {
		try {
			Integer Id = dataUpdate.getData_id();
			String content = dataUpdate.getContent();
			dataUpdate.setContent(null);
			String partnerID = dataUpdate.getPartner_id();
			String contractID = dataUpdate.getContract_id();
			Integer dataType = dataUpdate.getDataType();
			Integer status = dataUpdate.getStatus();
			String description = dataUpdate.getDescription();

//		Path uploadDir = Paths.get(.getSealDir() + "client/upload/");
			String filename = "temp";
			fheSimilarityMatchingService.encrypt(content, fheSimilarityMatchingService.getUploadDir(), filename);
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file",
					new FileSystemResource(fheSimilarityMatchingService.getUploadDir() + filename + ".ct"));
			parameters.add("Id", Id);
			parameters.add("partnerID", partnerID);
			parameters.add("contractID", contractID);
			parameters.add("dataType", dataType);
			parameters.add("status", status);
			parameters.add("description", description);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "application/xml, application/json");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Description> result = restTemplate.exchange(
					fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.PUT,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
			fheSimilarityMatchingService.deleteDir(fheSimilarityMatchingService.getUploadDir() + filename + ".ct");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Description> deleteDataSM(@RequestParam(name = "id", required = true) Integer id,
			@RequestParam(name = "partnerId", required = true) String partnerId) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("id", id);
			parameters.add("partnerId", partnerId);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "*/*");
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.exchange(fheServerData + "/openapi/v1/crud-data-master/data", HttpMethod.DELETE,
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), Description.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public @ResponseBody ResponseEntity<byte[]> checkWithEncryptedFilePathSM(
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "requestID", required = true) String requestID,
			@RequestParam("dataPath") String dataPath) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("partnerID", partnerID);
			parameters.add("requestID", requestID);
			parameters.add("dataPath", dataPath);
			HttpHeaders headers = new HttpHeaders();
//		headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			byte[] result = restTemplate.postForObject(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/02-checkWithEncryptedFilePath",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), byte[].class);

			return new ResponseEntity<byte[]>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public @ResponseBody ResponseEntity<byte[]> checkWithEncryptedFileIdSM(
			@RequestParam(name = "partnerID", required = true) String partnerID,
			@RequestParam(name = "requestID", required = true) String requestID,
			@RequestParam("dataId") String dataId) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("partnerID", partnerID);
			parameters.add("requestID", requestID);
			parameters.add("dataId", dataId);
			HttpHeaders headers = new HttpHeaders();
//		headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			byte[] result = restTemplate.postForObject(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/02-checkWithEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), byte[].class);

			return new ResponseEntity<byte[]>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<String> checkSMContentAuto2(@RequestParam("content") String content,
			@RequestParam("partnerID") String partnerID, @RequestParam("dataPath") String dataPath) {
		try {
			// encrypt
			String filename = "temp";
			fheSimilarityMatchingService.encrypt(content, fheSimilarityMatchingService.getUploadDir(), filename);
			Path path = Paths.get(fheSimilarityMatchingService.getUploadDir() + filename + ".ct");
			// upload
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(path.toString()));
			parameters.add("partnerID", partnerID);
			parameters.add("dataId", dataPath);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> requestIdResponse = restTemplate.postForEntity(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/01-uploadEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
			fheSimilarityMatchingService.deleteDir(path.toString());
			// check
			if (requestIdResponse.getStatusCode().is2xxSuccessful()) {
				String requestId = requestIdResponse.getBody();
				ResponseEntity<byte[]> zipResultResponse = checkWithEncryptedFilePathSM(partnerID, requestId, dataPath);
				// decrypt
				if (zipResultResponse.getStatusCode().is2xxSuccessful()) {

					boolean isDirCreated = tools
							.creatDir(fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
					if (isDirCreated) {
						// Save Zip Result File
						String zipResultPath = fheSimilarityMatchingService.getResultDir() + "/client_" + requestId
								+ "/" + requestId + ".zip";
						Files.write(Paths.get(zipResultPath), zipResultResponse.getBody());
						// Unzip - start
						ArrayList<String> extractedFiles = zip.UnzipFile(zipResultPath,
								fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
						// Unzip - end

						String strResult = "";
						for (int i = 0; i < extractedFiles.size(); i++) {
							try {
								strResult = strResult
										+ fheSimilarityMatchingService.decryptCheckResult(extractedFiles.get(i)).trim();
								System.out.println("halllooooo : " + strResult);
							} catch (NumberFormatException ex) {
								ex.printStackTrace();
							}
						}
						fheSimilarityMatchingService.deleteDir(zipResultPath);
						return new ResponseEntity<String>(strResult, HttpStatus.OK);
					} else {
						throw new Exception("Could not created the directory");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> checkSMContentAuto(@RequestParam("content") String content,
			@RequestParam("partnerID") String partnerID, @RequestParam("dataId") String dataId) {
		try {
			// encrypt
			String filename = "temp";
			fheSimilarityMatchingService.encrypt(content, fheSimilarityMatchingService.getUploadDir(), filename);
			Path path = Paths.get(fheSimilarityMatchingService.getUploadDir() + filename + ".ct");
			// upload
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(path.toString()));
			parameters.add("partnerID", partnerID);
			parameters.add("dataId", dataId);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> requestIdResponse = restTemplate.postForEntity(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/01-uploadEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
			fheSimilarityMatchingService.deleteDir(path.toString());
			// check
			if (requestIdResponse.getStatusCode().is2xxSuccessful()) {
				String requestId = requestIdResponse.getBody();
				ResponseEntity<byte[]> zipResultResponse = checkWithEncryptedFileIdSM(partnerID, requestId, dataId);
				// decrypt
				if (zipResultResponse.getStatusCode().is2xxSuccessful()) {

					boolean isDirCreated = tools
							.creatDir(fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
					if (isDirCreated) {
						// Save Zip Result File
						String zipResultPath = fheSimilarityMatchingService.getResultDir() + "/client_" + requestId
								+ "/" + requestId + ".zip";
						Files.write(Paths.get(zipResultPath), zipResultResponse.getBody());
						// Unzip - start
						ArrayList<String> extractedFiles = zip.UnzipFile(zipResultPath,
								fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
						// Unzip - end

						String strResult = "";
						for (int i = 0; i < extractedFiles.size(); i++) {
							try {
								strResult = strResult
										+ fheSimilarityMatchingService.decryptCheckResult(extractedFiles.get(i)).trim();
								System.out.println("halllooooo : " + strResult);
							} catch (NumberFormatException ex) {
								ex.printStackTrace();
							}
						}
						fheSimilarityMatchingService.deleteDir(zipResultPath);
						return new ResponseEntity<String>(strResult, HttpStatus.OK);
					} else {
						throw new Exception("Could not created the directory");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> checkMatchedPercentage2(@RequestParam("partnerID") String partnerID,
			@RequestParam("dataPath") String dataPath, @RequestBody List<Attribute> attributes) {
		try {
			// encrypt
			String filename = "temp";
			// Get only 40 items here !!!!!
			String content = attributes2ContentConverter(attributes);
			fheSimilarityMatchingService.encrypt(content, fheSimilarityMatchingService.getUploadDir(), filename);
			Path path = Paths.get(fheSimilarityMatchingService.getUploadDir() + filename + ".ct");
			// upload
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(path.toString()));
			parameters.add("partnerID", partnerID);
			parameters.add("dataPath", dataPath);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> requestIdResponse = restTemplate.postForEntity(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/01-uploadEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
			fheSimilarityMatchingService.deleteDir(path.toString());
			// check
			if (requestIdResponse.getStatusCode().is2xxSuccessful()) {
				String requestId = requestIdResponse.getBody();
				ResponseEntity<byte[]> zipResultResponse = checkWithEncryptedFilePathSM(partnerID, requestId, dataPath);
				// decrypt
				if (zipResultResponse.getStatusCode().is2xxSuccessful()) {

					boolean isDirCreated = tools
							.creatDir(fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
					if (isDirCreated) {
						// Save Zip Result File
						String zipResultPath = fheSimilarityMatchingService.getResultDir() + "/client_" + requestId
								+ "/" + requestId + ".zip";
						Files.write(Paths.get(zipResultPath), zipResultResponse.getBody());
						// Unzip - start
						ArrayList<String> extractedFiles = zip.UnzipFile(zipResultPath,
								fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
						// Unzip - end

						String strResult = "";
						for (int i = 0; i < extractedFiles.size(); i++) {
							try {
								strResult = strResult
										+ fheSimilarityMatchingService.decryptCheckResult(extractedFiles.get(i)).trim();
								System.out.println("halllooooo : " + strResult);
							} catch (NumberFormatException ex) {
								ex.printStackTrace();
							}
						}
						fheSimilarityMatchingService.deleteDir(zipResultPath);
						return new ResponseEntity<String>(strResult, HttpStatus.OK);
					} else {
						throw new Exception("Could not created the directory");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> checkMatchedPercentage(@RequestParam("partnerID") String partnerID,
			@RequestParam("dataId") String dataId, @RequestBody List<Attribute> attributes) {
		try {
			// encrypt
			String filename = "temp";
			// Get only 40 items here !!!!!
			String content = attributes2ContentConverter(attributes);
			fheSimilarityMatchingService.encrypt(content, fheSimilarityMatchingService.getUploadDir(), filename);
			Path path = Paths.get(fheSimilarityMatchingService.getUploadDir() + filename + ".ct");
			// upload
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(path.toString()));
			parameters.add("partnerID", partnerID);
			parameters.add("dataId", dataId);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> requestIdResponse = restTemplate.postForEntity(
					fheServerAnalysis + "/openapi/v1/crud-data-master/check/01-uploadEncryptedFile",
					new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
			fheSimilarityMatchingService.deleteDir(path.toString());
			// check
			if (requestIdResponse.getStatusCode().is2xxSuccessful()) {
				String requestId = requestIdResponse.getBody();
				ResponseEntity<byte[]> zipResultResponse = checkWithEncryptedFileIdSM(partnerID, requestId, dataId);
				// decrypt
				if (zipResultResponse.getStatusCode().is2xxSuccessful()) {

					boolean isDirCreated = tools
							.creatDir(fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
					if (isDirCreated) {
						// Save Zip Result File
						String zipResultPath = fheSimilarityMatchingService.getResultDir() + "/client_" + requestId
								+ "/" + requestId + ".zip";
						Files.write(Paths.get(zipResultPath), zipResultResponse.getBody());
						// Unzip - start
						ArrayList<String> extractedFiles = zip.UnzipFile(zipResultPath,
								fheSimilarityMatchingService.getResultDir() + "/client_" + requestId);
						// Unzip - end

						String strResult = "";
						for (int i = 0; i < extractedFiles.size(); i++) {
							try {
								strResult = strResult
										+ fheSimilarityMatchingService.decryptCheckResult(extractedFiles.get(i)).trim();
								System.out.println("halllooooo : " + strResult);
							} catch (NumberFormatException ex) {
								ex.printStackTrace();
							}
						}
						fheSimilarityMatchingService.deleteDir(zipResultPath);
						return new ResponseEntity<String>(strResult, HttpStatus.OK);
					} else {
						throw new Exception("Could not created the directory");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public String attributes2ContentConverter(List<Attribute> attributes) {
		attributes.sort(Comparator.comparing(a -> a.getIndex()));
		String content = "";
		for (int i = 0; i < attributes.size(); i++) {
			System.out.println(attributes.get(i).getIndex());
			content = content + attributes.get(i).getValue() + " ";
		}

		if (content != null && content.length() > 0) {
			content = content.substring(0, content.length() - 1);
		}

		System.out.println(content);
		return content;
	}

//	@Override
//	public ResponseEntity<String> decryptCheckResults(
//			@RequestParam(name = "file", required = true) MultipartFile file) {
//		try {
//			FHEFileSystem ffsf = new FHEFileSystem();
//			ffsf.setContentBase64(file.getBytes());
//
//			String[] strFiles = ffsf.getContentBase64().split("#####");
//
//			String path = storageService.getFileDir().toString() + "/temp.ct";
//			for (String strFile : strFiles) {
//				FHEFileSystem ffs = new FHEFileSystem();
//				ffs.setContentBase64(strFile);
//
//				Files.write(Paths.get(path), ffs.getContentByteArray());
////		        System.out.println(res);
//				String result = fheGenericService.decryptCheckResult(path);
//				if (result.equals("1")) {
//					fheGenericService.deleteDir(path);
//					return new ResponseEntity<String>("1", HttpStatus.OK);
//				}
//			}
//			fheGenericService.deleteDir(path);
//			return new ResponseEntity<String>("0", HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	@Override
//	public ResponseEntity<Data> getDecryptedDrivingLicense(@RequestParam(name = "Id") Integer Id) {
//		try {
//			Data data = dataService.getDataById(Id);
//			data.setDataNo(fheGenericService.decrypt(data.getDataNo(),
//					data.getDataId().toString()));
//
//			return new ResponseEntity<Data>(data, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	String cookieSession;

//	@Override
//	public ResponseEntity<Description> servicelogin(String partnerLoginURL, String userName, String pw) {
//		try {
//			RestTemplate testRestTemplate = new RestTemplate();
//
//			logger.debug("Testing Login & ping to partner {}", partnerLoginURL);
//
//			MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
//			form.add("username", userName);
//			form.add("password", pw);
//			ResponseEntity<String> response = testRestTemplate.postForEntity(partnerLoginURL, form, String.class);
//
//			assert (response.getStatusCode() == HttpStatus.FOUND);
//
//			String cookieSession = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
//			logger.debug("Response Code ({}) and body = ({}) ", response.getStatusCode(), response.getBody());
//			logger.debug("Response =  " + response.getHeaders().toString());
//			logger.debug("Cookie : " + cookieSession);
//
//			Description headers = new Description().message("Cookie").value(cookieSession);
//
//			this.cookieSession = cookieSession;
//
//			return new ResponseEntity<Description>(headers, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	@Override
//	public ResponseEntity<Object> proxyGet(String URL, String cookieSession) {
//		try {
//			if (cookieSession == null) {
//				cookieSession = this.cookieSession;
//			}
//
////			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
//			HttpHeaders headers = new HttpHeaders();
////			headers.set("Accept", "*/*");
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			headers.add("Cookie", cookieSession);
//			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<Object> response = restTemplate.exchange(URL, HttpMethod.GET,
//					new HttpEntity<Object>(headers), Object.class);
//			return response;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

	// Get All Users
//	@GetMapping("/users")
//	private List<Users> getAllUsers() {
//		return userService.getAllUsers();
//	}

	// Error handling
//	public static long checkDrivingLicenseNoInput(String drivingLicenseNoString) throws Exception {
//		long drivingLicenseNo;
//		try {
//			drivingLicenseNo = Long.parseLong(drivingLicenseNoString);
//		} catch (Exception ex) {
////			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//			throw new Exception("Driving License Number should contain only numbers and not be empty or blank", ex);
//		}
//		return drivingLicenseNo;
//	}
//
//	public static boolean isNumeric(String strNum) {
//		if (strNum == null) {
//			return false;
//		}
//		try {
//			@SuppressWarnings("unused")
//			double d = Double.parseDouble(strNum);
//		} catch (NumberFormatException nfe) {
//			return false;
//		}
//		return true;
//	}

//	@PostMapping("/openapi/v1/client/drivingLicense/03-check")
//	//public ResponseEntity<ByteArrayResource> checkLicense(@RequestParam("file") MultipartFile file) throws Exception {
//	public @ResponseBody byte[] checkLicense(@RequestParam("file") MultipartFile file) throws Exception {
//		ResponseEntity<ResponseMessage> re = uploadFile(file);
//		String source = storageService.getFileDir() + "/" + file.getOriginalFilename();
//		List<Users> allUsers = userService.getAllUsers();
//		ArrayList<String> data = new ArrayList<String>();
//		for (Users user : allUsers) {
//			data.add(user.getDrivingLicenseNo() + fheGenericService.getFilename() + ".ct");
//		}
//		boolean result = fheGenericService.checkLicense(source, data);
//		System.out.println(result);
//		Path path = Paths.get(fheGenericService.getResultDir() + fheGenericService.getFilename() + ".ct");
//		byte[] returnData = Files.readAllBytes(path);
//		return returnData;
//	}

//	@PutMapping("/user/{userid}")
//	private Users update(@RequestBody Users user, @PathVariable("userid") int userid) {
//		Users oldUser = userService.getUserById(user.getUserId());
//		
//		if(!user.getDrivingLicenseNo().equals(oldUser.getDrivingLicenseNo()) && isNumeric(user.getDrivingLicenseNo())) {		
//			cin.createLicense(Long.parseLong(user.getDrivingLicenseNo()), oldUser.getDrivingLicenseNo());
//			user.setDrivingLicenseNo(oldUser.getDrivingLicenseNo());
//		}
//		
//		userService.update(user, userid);
//		return user;
//	}

//	@GetMapping("/drivinglicense")
//	private Users checkLicense(@RequestParam("no") long no) {
//		List<Users> allUsers = userService.getAllUsers();
//		for (Users user : allUsers) {
//			cin.exec1(no, user.getDrivingLicenseNo());
//			if (cin.decryptBit()) {
//				cin.deleteResult();
//				return user;
//			}
//		}
//		return null;
//	}

//	// Not so convenient
//	@GetMapping("/files")
//	public ResponseEntity<List<FileInfo>> getListFiles() {
//		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
//			String filename = path.getFileName().toString();
//			String url = MvcUriComponentsBuilder
//					.fromMethodName(Controller.class, "getFile", path.getFileName().toString()).build().toString();
//
//			return new FileInfo(filename, url);
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//	}

//	@GetMapping("/files/{filename:.+}")
//	@ResponseBody
//	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//		Resource file = storageService.load(filename);
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}

//	// Get a specific user
//	@GetMapping("/user/{id}")
//	private Users getUser(@PathVariable("id") int id) throws Exception {
//		Users user = userService.getUserById(id);
//		System.out.println(user.getDrivingLicenseNo());
//		user.setDrivingLicenseNo(fheGenericService.decrypt(user.getDrivingLicenseNo()));
//		return user;
//	}

//	// Create a user with auto-Incrementing id or update a user with its id
//	@PostMapping("/user")
//	private Users createOrSaveUser(@RequestBody Users user) {
//
//		String uniqueID = fheGenericService.createLicense(user.getDrivingLicenseNo());
//		user.setDrivingLicenseNo(uniqueID);
//		userService.saveOrUpdate(user);
//		return user;
//
////		version 1
////		long drivingLicenseNo = checkDrivingLicenseNoInput(user.getDrivingLicenseNo());
////		
////		String uniqueID = cin.createLicense(drivingLicenseNo);
////		user.setDrivingLicenseNo(uniqueID);
////		userService.saveOrUpdate(user);
////		return user;
//	}

	// Update a user, for updating the driving license number filed, a new input
	// license number will be update automatically
//	@PutMapping("/user")
//	private Users update(@RequestBody Users user) {
//		Users oldUser = userService.getUserById(user.getUserId());
//		if (!user.getDrivingLicenseNo().equals(oldUser.getDrivingLicenseNo())) {
//			fheGenericService.createLicense(user.getDrivingLicenseNo(), oldUser.getDrivingLicenseNo());
//			user.setDrivingLicenseNo(oldUser.getDrivingLicenseNo());
//		}
//		userService.saveOrUpdate(user);
//		return user;
//	}
//	@PutMapping("/user")
//	private Users update(@RequestBody Users user) {
//		Users oldUser = userService.getUserById(user.getUserId());
//
//		long drivingLicenseNo = checkDrivingLicenseNoInput(user.getDrivingLicenseNo());
//
//		if (!user.getDrivingLicenseNo().equals(oldUser.getDrivingLicenseNo())) {
//			cin.createLicense(drivingLicenseNo, oldUser.getDrivingLicenseNo());
//			user.setDrivingLicenseNo(oldUser.getDrivingLicenseNo());
//			System.out.println("bugggg");
//		}
//
//		userService.saveOrUpdate(user);
//		return user;
//	}

//	// Delete a specified user
//	@DeleteMapping("/user/{id}")
//	private void deleteUser(@PathVariable("id") int id) {
//		Users user = userService.getUserById(id);
//		fheGenericService.deleteDir(user.getDrivingLicenseNo());
//		userService.delete(id);
//	}

	// Check a user driving license without knowing it by indicating the folder name
	// which contains it's tfhe encrypted files
	// The folder located in upload folder - folder name input required
//	@GetMapping("/check/{dir}")
//	private Users checkLicense(@PathVariable("dir") String dir) throws Exception {
//		List<Users> allUsers = userService.getAllUsers();
//		for (Users user : allUsers) {
////			System.out.println(dir + " vs " +user.getDrivingLicenseNo());
//			String path = "upload/" + dir + "/";
////			boolean result = fheGenericService.checkLicense(path, user.getDrivingLicenseNo().toString());
////			System.out.println(result);
////			if (result) {
////				return user;
////			}
//		}
//		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//		// return null;
//	}

}

//@RestController
//public class Controller {
//	// private static final String template = "Hello, %s!";
//	// private final AtomicLong counter = new AtomicLong();
//
//	private Cingulata cin = new Cingulata();
//
//	// autowire the UsersService class
//	@Autowired
//	Service userService;
//
//	//@Autowired
//	FilesStorageService storageService;
//
//	@RequestMapping(value="/hello", method=RequestMethod.GET)
//	private String hello() {
//		return "Hello";
//	}
//
//	// Get All Users
//	@GetMapping("/users")
//	private List<Users> getAllUsers() {
//		return userService.getAllUsers();
//	}
//
//	// Get a specific user
//	@GetMapping("/user/{id}")
//	private Users getUser(@PathVariable("id") int id) throws Exception {
//		Users user = userService.getUserById(id);
//		System.out.println(user.getDrivingLicenseNo());
//		user.setDrivingLicenseNo(cin.decryptMultiBitArray(user.getDrivingLicenseNo()));
//		return user;
//	}
//
//	// Create a user with auto-Incrementing id or update a user with its id
//	@PostMapping("/user")
//	private Users createOrSaveUser(@RequestBody Users user) {
//		
//		String uniqueID = cin.createLicense(user.getDrivingLicenseNo());
//		user.setDrivingLicenseNo(uniqueID);
//		userService.saveOrUpdate(user);
//		return user;
//		
////		version 1
////		long drivingLicenseNo = checkDrivingLicenseNoInput(user.getDrivingLicenseNo());
////		
////		String uniqueID = cin.createLicense(drivingLicenseNo);
////		user.setDrivingLicenseNo(uniqueID);
////		userService.saveOrUpdate(user);
////		return user;
//	}
//
//	// Update a user, for updating the driving license number filed, a new input
//	// license number will be update automatically
//	@PutMapping("/user")
//	private Users update(@RequestBody Users user) {
//		Users oldUser = userService.getUserById(user.getUserId());
//		if (!user.getDrivingLicenseNo().equals(oldUser.getDrivingLicenseNo())) {
//			cin.createLicense(user.getDrivingLicenseNo(), oldUser.getDrivingLicenseNo());
//			user.setDrivingLicenseNo(oldUser.getDrivingLicenseNo());
//		}
//		userService.saveOrUpdate(user);
//		return user;
//	}
////	@PutMapping("/user")
////	private Users update(@RequestBody Users user) {
////		Users oldUser = userService.getUserById(user.getUserId());
////
////		long drivingLicenseNo = checkDrivingLicenseNoInput(user.getDrivingLicenseNo());
////
////		if (!user.getDrivingLicenseNo().equals(oldUser.getDrivingLicenseNo())) {
////			cin.createLicense(drivingLicenseNo, oldUser.getDrivingLicenseNo());
////			user.setDrivingLicenseNo(oldUser.getDrivingLicenseNo());
////			System.out.println("bugggg");
////		}
////
////		userService.saveOrUpdate(user);
////		return user;
////	}
//
//	// Delete a specified user
//	@DeleteMapping("/user/{id}")
//	private void deleteUser(@PathVariable("id") int id) {
//		Users user = userService.getUserById(id);
//		cin.deleteDir(user.getDrivingLicenseNo());
//		userService.delete(id);
//	}
//
//	// Check a user driving license without knowing it by indicating the folder name
//	// which contains it's tfhe encrypted files
//	// The folder located in upload folder - folder name input required
//	@GetMapping("/check/{dir}")
//	private Users checkLicense(@PathVariable("dir") String dir) throws Exception {
//		List<Users> allUsers = userService.getAllUsers();
//		for (Users user : allUsers) {
////			System.out.println(dir + " vs " +user.getDrivingLicenseNo());
//			String path = "upload/" + dir + "/";
//			boolean result = cin.checkLicense(path, user.getDrivingLicenseNo().toString());
//			System.out.println(result);
//			if (result) {
//				return user;
//			}
//		}
//		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//		// return null;
//	}
//
//	// Upload a license number in upload folder for checking - folder name and
//	// license number iputs are required
//	@GetMapping("/upload")
//	private HashMap<String, String> uploadLicense(@RequestParam("number") String number,
//			@RequestParam("dir") String dir) {
//		String path = "upload/" + dir + "/";
//		cin.createLicense(number, path);
//
//		HashMap<String, String> map = new HashMap<>();
//		map.put("path", path);
//
//		return map;
//	}
//
//	// Error handling
//	public static long checkDrivingLicenseNoInput(String drivingLicenseNoString) {
//		long drivingLicenseNo;
//		try {
//			drivingLicenseNo = Long.parseLong(drivingLicenseNoString);
//		} catch (Exception ex) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//					"Driving License Number should contain only numbers and not be empty or blank", ex);
//		}
//		return drivingLicenseNo;
//	}
//
//	public static boolean isNumeric(String strNum) {
//		if (strNum == null) {
//			return false;
//		}
//		try {
//			@SuppressWarnings("unused")
//			double d = Double.parseDouble(strNum);
//		} catch (NumberFormatException nfe) {
//			return false;
//		}
//		return true;
//	}
//	
////	@PutMapping("/user/{userid}")
////	private Users update(@RequestBody Users user, @PathVariable("userid") int userid) {
////		Users oldUser = userService.getUserById(user.getUserId());
////		
////		if(!user.getDrivingLicenseNo().equals(oldUser.getDrivingLicenseNo()) && isNumeric(user.getDrivingLicenseNo())) {		
////			cin.createLicense(Long.parseLong(user.getDrivingLicenseNo()), oldUser.getDrivingLicenseNo());
////			user.setDrivingLicenseNo(oldUser.getDrivingLicenseNo());
////		}
////		
////		userService.update(user, userid);
////		return user;
////	}
//
////	@GetMapping("/drivinglicense")
////	private Users checkLicense(@RequestParam("no") long no) {
////		List<Users> allUsers = userService.getAllUsers();
////		for (Users user : allUsers) {
////			cin.exec1(no, user.getDrivingLicenseNo());
////			if (cin.decryptBit()) {
////				cin.deleteResult();
////				return user;
////			}
////		}
////		return null;
////	}
//
//	// Not so convenient
//	@GetMapping("/files")
//	public ResponseEntity<List<FileInfo>> getListFiles() {
//		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
//			String filename = path.getFileName().toString();
//			String url = MvcUriComponentsBuilder
//					.fromMethodName(Controller.class, "getFile", path.getFileName().toString()).build().toString();
//
//			return new FileInfo(filename, url);
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//	}
//
//	@GetMapping("/files/{filename:.+}")
//	@ResponseBody
//	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//		Resource file = storageService.load(filename);
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}
//
//	@PostMapping("/uploadfile")
//	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
//		String message = "";
//		try {
//			storageService.save(file);
//
//			message = "Uploaded the file successfully: " + file.getOriginalFilename();
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//		} catch (Exception e) {
//			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//		}
//	}
//
//	@PostMapping("/uploadfiles")
//	public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) {
//		String message = "";
//		try {
//			storageService.saveAll(files);
//
////	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
//			message = "Uploaded the files successfully: ";
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//		} catch (Exception e) {
////	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//			message = "Could not upload all the files !";
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//		}
//	}
//
//}
