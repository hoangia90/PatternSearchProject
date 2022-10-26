package fr.cea.bigpi.fhe.dap.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.cea.bigpi.fhe.dap.helper.Tools;
import fr.cea.bigpi.fhe.dap.helper.Zip;
import fr.cea.bigpi.fhe.dap.model.Data;
import fr.cea.bigpi.fhe.dap.service.DataService;
import fr.cea.bigpi.fhe.dap.service.FHESimilarityMatchService;
import fr.cea.bigpi.fhe.dap.service.FilesStorageService;
import io.swagger.annotations.ApiParam;

@RestController
public class ControllerImpl implements Controller {

	@Autowired
	private FHESimilarityMatchService fheSimilarityMatchService;

	@Autowired
	DataService dataService;

	@Autowired
	FilesStorageService storageService;

	@Autowired
	Zip zip;

	@Autowired
	Tools tools;

	static final Logger logger = LoggerFactory.getLogger(ControllerImpl.class);

	@Override
	public ResponseEntity<String> uploadEncryptedFile(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID) {
		String message = "";
		try {
			String requestID = UUID.randomUUID().toString();
			String filename = requestID + ".ct";

			byte[] bytes = file.getBytes();
			String path = storageService.getFileDir().toString() + "/" + filename;
			Files.write(Paths.get(path), bytes);
			message = requestID;
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			e.printStackTrace();
		}
		return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<byte[]> downloadEncryptedFile(
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID) {
		try {
			List<Data> allData = dataService.getAllData();
			for (Data datum : allData) {
				if (datum.getPartnerId().equals(partnerID) && datum.getDataId().equals(Id)) {
					String fileName = Id + ".ct";
					String encryptedFilePath = datum.getContent() + fileName;
					Path path = Paths.get(encryptedFilePath);
					byte[] returnData = Files.readAllBytes(path);
					return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<byte[]> checkWithEncryptedFile(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID,
			@ApiParam(name = "dataId", value = "", example = "", required = true) @RequestParam("dataId") String dataId) {
		int vectorSize = 102;
		// clean result
		fheSimilarityMatchService.deleteDir(fheSimilarityMatchService.getResultDir() + "requestID" + ".ct");
		//
		List<Data> allData = dataService.getAllData();
		ArrayList<String> data = new ArrayList<String>();
		for (Data datum : allData) {
			if (datum.getPartnerId().equals(partnerID)) {
				if (datum.getDataId().toString().equals(dataId)) {
//					System.out.println("debuggg!!!!" + datum.getContent() + datum.getDataId() + ".ct");
//					data.add(datum.getContent() + fhePatternSearchService.getFilename() + ".ct");
					data.add(datum.getContent() + datum.getDataId() + ".ct");
				}
			}
		}
		//
		try {
			if (!data.isEmpty()) {
				List<List<String>> parts = chopped(data, vectorSize);
				String fileName = requestID + ".ct";
				String encryptedFilePath = storageService.getFileDir() + "/" + fileName;

				String[] resultPaths = new String[parts.size()];
				for (int i = 0; i < parts.size(); i++) {
					try {
						String outputPath = requestID + "." + (i + 1) + "." + parts.size();
						System.out.println(outputPath);
						fheSimilarityMatchService.checkData(encryptedFilePath, (ArrayList<String>) parts.get(i),
								outputPath);
						resultPaths[i] = fheSimilarityMatchService.getResultDir() + "/" + outputPath + ".ct";
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				;
				// Zip result files - start
				boolean isDirCreated = tools.creatDir(fheSimilarityMatchService.getResultDir() + "/" + requestID);
//			if (isDirCreated) {

				String zipResultPath = zip.zipMultipleFiles(fheSimilarityMatchService.getResultDir() + "/" + requestID,
						requestID, resultPaths);
				// Zip result files - end
				for (int i = 0; i < resultPaths.length; i++) {
					fheSimilarityMatchService.deleteDir(resultPaths[i]);
				}
				Path path = Paths.get(zipResultPath);
				byte[] returnData = Files.readAllBytes(path);
				return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
			} else {
				throw new Exception("Could not created the result");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// chops a list into non-view sublists of length L
	static <T> List<List<T>> chopped(List<T> list, final int L) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += L) {
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}

//	@Override
//	public ResponseEntity<byte[]> checkWithEncryptedFile(
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
//			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID) {
//		int vectorSize = 102;
//		// clean result
//		fheSimilarityMatchService.deleteDir(fheSimilarityMatchService.getResultDir() + "requestID" + ".ct");
//		//
//		List<Data> allData = dataService.getAllData();
//		ArrayList<String> data = new ArrayList<String>();
//		for (Data datum : allData) {
//			System.out.println("debuggg!!!!" + datum.getContent() + datum.getDataId() + ".ct");
//			if (datum.getPartnerId().equals(partnerID)) {
////			data.add(datum.getContent() + fhePatternSearchService.getFilename() + ".ct");
//				data.add(datum.getContent() + datum.getDataId() + ".ct");
//			}
//		}
//		//
//
//		try {
////			if (data.size() > 0 && data.size() <= vectorSize) {
//////				String message = "";
////				String fileName = requestID + ".ct";
////				String encryptedFilePath = storageService.getFileDir() + "/" + fileName;
////				fhePatternSearchService.checkData(encryptedFilePath, data, requestID);
////				String resultPath = fhePatternSearchService.getResultDir() + "/" + requestID + ".ct";
//////			Resource resource = storageService.load(resultPath);
////				Path path = Paths.get(resultPath);
////				byte[] returnData = Files.readAllBytes(path);
////				fhePatternSearchService.deleteDir(path.toString());
////				return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
////				//
////			} else if (data.size() > 0 && data.size() > vectorSize) {
//
//			List<List<String>> parts = chopped(data, vectorSize);
//			String fileName = requestID + ".ct";
//			String encryptedFilePath = storageService.getFileDir() + "/" + fileName;
//
//			String[] resultPaths = new String[parts.size()];
//			for (int i = 0; i < parts.size(); i++) {
//				try {
//					String outputPath = requestID + "." + (i + 1) + "." + parts.size();
//					System.out.println(outputPath);
//					fheSimilarityMatchService.checkData(encryptedFilePath, (ArrayList<String>) parts.get(i),
//							outputPath);
//					resultPaths[i] = fheSimilarityMatchService.getResultDir() + "/" + outputPath + ".ct";
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			;
//
//			// Check if file exist
////				boolean ready = false;
////				String strCheckPath = resultPath[(parts.size()-1)];
////				Path checkPath = Paths.get(strCheckPath);
////				while (!ready) {
////					checkPath = Paths.get(strCheckPath);
////					System.out.println(checkPath + " : " + Files.exists(checkPath));
////					if (Files.exists(checkPath)) {
////						ready = true;
////					}
////				}
//
//			// Zip result files - start
//			boolean isDirCreated = tools.creatDir(fheSimilarityMatchService.getResultDir() + "/" + requestID);
////			if (isDirCreated) {
//
//			String zipResultPath = zip.zipMultipleFiles(fheSimilarityMatchService.getResultDir() + "/" + requestID,
//					requestID, resultPaths);
//			// Zip result files - end
//
//			for (int i = 0; i < resultPaths.length; i++) {
//				fheSimilarityMatchService.deleteDir(resultPaths[i]);
//			}
//
//			Path path = Paths.get(zipResultPath);
//			byte[] returnData = Files.readAllBytes(path);
////				fhePatternSearchService.deleteDir(path.toString());
//			return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
//
////			} else {
////				throw new Exception("Could not created the directory");
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//
//	// chops a list into non-view sublists of length L
//	static <T> List<List<T>> chopped(List<T> list, final int L) {
//		List<List<T>> parts = new ArrayList<List<T>>();
//		final int N = list.size();
//		for (int i = 0; i < N; i += L) {
//			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
//		}
//		return parts;
//	}

//	@Override
//	public ResponseEntity<String> uploadDataFile(
//			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID) {
//		String message = "";
//		try {
//			String requestID = UUID.randomUUID().toString();
//			String filename = requestID + ".ct";
//
//			byte[] bytes = file.getBytes();
//			String path = storageService.getFileDir().toString() + "/" + filename;
//			Files.write(Paths.get(path), bytes);
//			message = requestID;
//			return new ResponseEntity<String>(message, HttpStatus.OK);
//		} catch (Exception e) {
//			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//			e.printStackTrace();
//		}
//		return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	@Override
//	public ResponseEntity<byte[]> downloadDataFile(
//			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID) {
//		try {
//			List<Data> allDrivingLicenses = dataService.getAllData();
//			for (Data drivingLicense : allDrivingLicenses) {
//				if (drivingLicense.getPartnerId().equals(partnerID) && drivingLicense.getDataId().equals(Id)) {
//					String fileName = Id + ".ct";
//					String encryptedFilePath = drivingLicense.getDataNo() + fileName;
//					Path path = Paths.get(encryptedFilePath);
//					byte[] returnData = Files.readAllBytes(path);
//					return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	@Override
//	public ResponseEntity<byte[]> dataCheckByFile(
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
//			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID,
//			@ApiParam(name = "contractID", value = "", example = "", required = true) @RequestParam("contractID") String contractID,
//			@ApiParam(name = "dataID", value = "", example = "", required = true) @RequestParam("dataID") Integer dataID) {
//		try {
//			// clean result
//			fheSimilarityMatchService.deleteDir(fheSimilarityMatchService.getResultDir() + "requestID" + ".ct");
//			//
//			List<Data> allDrivingLicenses = dataService.getAllDatas();
//			ArrayList<String> data = new ArrayList<String>();
//			for (Data drivingLicense : allDrivingLicenses) {
//				if (drivingLicense.getPartnerId().equals(partnerID)) {
//					if (drivingLicense.getPartnerId().equals(contractID)) {
////				data.add(drivingLicense.getDriving_license_no() + seal.getFilename() + ".ct");
////					data.add(drivingLicense.getDataNo() + drivingLicense.getDataId() + ".ct");
////						data.add(dataService.getDataById(dataID).getDataNo() + dataID + ".ct");
//						data.add(drivingLicense.getDataNo() + dataID + ".ct");
//						System.out.println(drivingLicense.getDataNo() + drivingLicense.getDataId() + ".ct");
//					}
//				}
//			}
////			System.out.println(dataService.getDataById(dataID).getDataNo() + dataID + ".ct");
////			data.add(dataService.getDataById(dataID).getDataNo() + dataID + ".ct");
////			System.out.println(drivingLicense.getDataNo() + drivingLicense.getDataId() + ".ct");
////			if (data.size() > 0 && data.size() <= 102) {
////				String message = "";
//				String fileName = requestID + ".ct";
//				String encryptedFilePath = storageService.getFileDir() + "/" + fileName;
//				fheSimilarityMatchService.checkData(encryptedFilePath, data, requestID);
//				String resultPath = fheSimilarityMatchService.getResultDir() + "/" + requestID + ".ct";
////			Resource resource = storageService.load(resultPath);
//				Path path = Paths.get(resultPath);
//				byte[] returnData = Files.readAllBytes(path);
////				fheSimilarityMatchService.deleteDir(path.toString());
//				return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
////			} else {
////				throw new Exception("Number of files must be smaller than 102");
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	@Override
//	public ResponseEntity<byte[]> dataCheckByFilewList(
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
//			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID,
//			@ApiParam(name = "files", value = "", example = "", required = true) @RequestParam("files") ArrayList<String> files) {
//		try {
//			// clean result
//			fheSimilarityMatchService.deleteDir(fheSimilarityMatchService.getResultDir() + "requestID" + ".ct");
//			//
//			if (files.size() > 0 && files.size() <= 102) {
////				String message = "";
//				String fileName = requestID + ".ct";
//				String encryptedFilePath = storageService.getFileDir() + "/" + fileName;
//				fheSimilarityMatchService.checkData(encryptedFilePath, files, requestID);
//				String resultPath = fheSimilarityMatchService.getResultDir() + "/" + requestID + ".ct";
////			Resource resource = storageService.load(resultPath);
//				Path path = Paths.get(resultPath);
//				byte[] returnData = Files.readAllBytes(path);
////				fhePatternSearchService.deleteDir(path.toString());
//				return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
//			} else {
//				throw new Exception("Number of files must be smaller than 102");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	@Override
//	public ResponseEntity<FHEFileSystem> concateFiles(
//			@ApiParam(name = "file1", value = "", example = "", required = true) @RequestParam("file1") FHEFileSystem file1,
//			@ApiParam(name = "file2", value = "", example = "", required = true) @RequestParam("file2") FHEFileSystem file2,
//			@ApiParam(name = "filename", value = "", example = "", required = true) @RequestParam("filename") String filename) {
//		System.out.println("debug 0");
//		logger.info("debug 0");
//		try {
//			if (file1.getFormat().equals(file2.getFormat())) {
//				FHEFileSystem ffs = new FHEFileSystem();
//				System.out.println("debug 1");
//				logger.info("debug 1");
//				ffs.setContentBase64(file1.getContentBase64() + "#####" + file2.getContentBase64());
//				System.out.println("debug 2");
//				logger.info("debug 2");
//				ffs.setFileName(filename);
//				System.out.println("debug 3");
//				logger.info("debug 3");
//				ffs.setFormat(file1.getFormat());
//				System.out.println("debug 4");
//				logger.info("debug 4");
//				return new ResponseEntity<FHEFileSystem>(ffs, HttpStatus.OK);
//			} else {
//				throw new Exception("The format must be the same");
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}

}
