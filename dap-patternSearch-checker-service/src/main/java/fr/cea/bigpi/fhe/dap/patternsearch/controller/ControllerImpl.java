package fr.cea.bigpi.fhe.dap.patternsearch.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.cea.bigpi.fhe.dap.patternsearch.helper.Tools;
import fr.cea.bigpi.fhe.dap.patternsearch.helper.Zip;
import fr.cea.bigpi.fhe.dap.patternsearch.model.Data;
//import fr.cea.bigpi.fhe.dap.patternsearch.model.FHEFileSystem;
import fr.cea.bigpi.fhe.dap.patternsearch.service.DataService;
import fr.cea.bigpi.fhe.dap.patternsearch.service.FHEPatternSearchService;
import fr.cea.bigpi.fhe.dap.patternsearch.service.FilesStorageService;
import io.swagger.annotations.ApiParam;

@RestController
public class ControllerImpl implements Controller {

	@Autowired
	private FHEPatternSearchService fhePatternSearchService;

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
	public ResponseEntity<byte[]> checkWithEncryptedFile(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID) {
		int vectorSize = 102;
		// clean result
		fhePatternSearchService.deleteDir(fhePatternSearchService.getResultDir() + "requestID" + ".ct");
		//
		List<Data> allData = dataService.getAllData();
		ArrayList<String> data = new ArrayList<String>();
		for (Data datum : allData) {
			if (datum.getPartnerId().equals(partnerID)) {
//			data.add(datum.getContent() + fhePatternSearchService.getFilename() + ".ct");
				data.add(datum.getContent() + datum.getDataId() + ".ct");
			}
		}
		//

		try {
//			if (data.size() > 0 && data.size() <= vectorSize) {
////				String message = "";
//				String fileName = requestID + ".ct";
//				String encryptedFilePath = storageService.getFileDir() + "/" + fileName;
//				fhePatternSearchService.checkData(encryptedFilePath, data, requestID);
//				String resultPath = fhePatternSearchService.getResultDir() + "/" + requestID + ".ct";
////			Resource resource = storageService.load(resultPath);
//				Path path = Paths.get(resultPath);
//				byte[] returnData = Files.readAllBytes(path);
//				fhePatternSearchService.deleteDir(path.toString());
//				return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
//				//
//			} else if (data.size() > 0 && data.size() > vectorSize) {

			List<List<String>> parts = chopped(data, vectorSize);
			String fileName = requestID + ".ct";
			String encryptedFilePath = storageService.getFileDir() + "/" + fileName;

			String[] resultPaths = new String[parts.size()];
			for (int i = 0; i < parts.size(); i++) {
				try {
					String outputPath = requestID + "." + (i + 1) + "." + parts.size();
					System.out.println(outputPath);
					fhePatternSearchService.checkData(encryptedFilePath, (ArrayList<String>) parts.get(i), outputPath);
					resultPaths[i] = fhePatternSearchService.getResultDir() + "/" + outputPath + ".ct";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			;

			// Check if file exist
//				boolean ready = false;
//				String strCheckPath = resultPath[(parts.size()-1)];
//				Path checkPath = Paths.get(strCheckPath);
//				while (!ready) {
//					checkPath = Paths.get(strCheckPath);
//					System.out.println(checkPath + " : " + Files.exists(checkPath));
//					if (Files.exists(checkPath)) {
//						ready = true;
//					}
//				}

			// Zip result files - start
			boolean isDirCreated = tools.creatDir(fhePatternSearchService.getResultDir() + "/" + requestID);
			if (isDirCreated) {

				String zipResultPath = zip.zipMultipleFiles(fhePatternSearchService.getResultDir() + "/" + requestID,
						requestID, resultPaths);
				// Zip result files - end

				for (int i = 0; i < resultPaths.length; i++) {
					fhePatternSearchService.deleteDir(resultPaths[i]);
				}

				Path path = Paths.get(zipResultPath);
				byte[] returnData = Files.readAllBytes(path);
//				fhePatternSearchService.deleteDir(path.toString());
				return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);

			} else {
				throw new Exception("Could not created the directory");
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

//	@Override
//	public ResponseEntity<byte[]> drivingLicenseCheckByFilewList(
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
//			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID,
//			@ApiParam(name = "files", value = "", example = "", required = true) @RequestParam("files") ArrayList<String> files) {
//		try {
//			// clean result
//			fhePatternSearchService.deleteDir(fhePatternSearchService.getResultDir() + "requestID" + ".ct");
//			//
//			if (files.size() > 0 && files.size() <= 102) {
////				String message = "";
//				String fileName = requestID + ".ct";
//				String encryptedFilePath = storageService.getFileDir() + "/" + fileName;
//				fhePatternSearchService.checkLicense(encryptedFilePath, files, requestID);
//				String resultPath = fhePatternSearchService.getResultDir() + "/" + requestID + ".ct";
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
