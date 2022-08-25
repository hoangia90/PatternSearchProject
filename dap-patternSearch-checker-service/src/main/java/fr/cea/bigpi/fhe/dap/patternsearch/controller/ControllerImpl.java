package fr.cea.bigpi.fhe.dap.patternsearch.controller;

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

	static final Logger logger = LoggerFactory.getLogger(ControllerImpl.class);

	@Override
	public ResponseEntity<String> uploadDrivingLicenseFile(
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
	public ResponseEntity<byte[]> downloadDrivingLicenseFile(
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID) {
		try {
			List<Data> allDrivingLicenses = dataService.getAllDatas();
			for (Data drivingLicense : allDrivingLicenses) {
				if (drivingLicense.getPartnerId().equals(partnerID) && drivingLicense.getDataId().equals(Id)) {
					String fileName = Id + ".ct";
					String encryptedFilePath = drivingLicense.getDataNo() + fileName;
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
	public ResponseEntity<byte[]> drivingLicenseCheckByFile(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID) {
		try {
			// clean result
			fhePatternSearchService.deleteDir(fhePatternSearchService.getResultDir() + "requestID" + ".ct");
			//
			List<Data> allDrivingLicenses = dataService.getAllDatas();
			ArrayList<String> data = new ArrayList<String>();
			for (Data drivingLicense : allDrivingLicenses) {
				if (drivingLicense.getPartnerId().equals(partnerID)) {
//				data.add(drivingLicense.getDriving_license_no() + seal.getFilename() + ".ct");
					data.add(drivingLicense.getDataNo() + drivingLicense.getDataId() + ".ct");
				}
			}
			if (data.size() > 0 && data.size() <= 102) {
//				String message = "";
				String fileName = requestID + ".ct";
				String encryptedFilePath = storageService.getFileDir() + "/" + fileName;
				fhePatternSearchService.checkLicense(encryptedFilePath, data, requestID);
				String resultPath = fhePatternSearchService.getResultDir() + "/" + requestID + ".ct";
//			Resource resource = storageService.load(resultPath);
				Path path = Paths.get(resultPath);
				byte[] returnData = Files.readAllBytes(path);
				fhePatternSearchService.deleteDir(path.toString());
				return new ResponseEntity<byte[]>(returnData, HttpStatus.OK);
			} else {
				throw new Exception("Number of files must be smaller than 102");
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
