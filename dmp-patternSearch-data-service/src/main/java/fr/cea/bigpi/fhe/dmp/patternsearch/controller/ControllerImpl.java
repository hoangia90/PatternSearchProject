package fr.cea.bigpi.fhe.dmp.patternsearch.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.cea.bigpi.fhe.dmp.patternsearch.helper.Tools;
import fr.cea.bigpi.fhe.dmp.patternsearch.model.Data;
import fr.cea.bigpi.fhe.dmp.patternsearch.model.Description;
import fr.cea.bigpi.fhe.dmp.patternsearch.service.DataService;
import fr.cea.bigpi.fhe.dmp.patternsearch.service.FHEPatternSearchService;
import fr.cea.bigpi.fhe.dmp.patternsearch.service.FHEService;
import fr.cea.bigpi.fhe.dmp.patternsearch.service.FilesStorageService;
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
	Tools tools;

	@Autowired
	FHEService fheService;

	private static final Logger logger = LoggerFactory.getLogger(ControllerImpl.class);

	@Override
	public ResponseEntity<String> hello(@PathVariable("username") String userName) {
		String res = String.format("{ \"Response\": \"%s\" }", fheService.ping(userName));
		logger.info("Response from HelloWorld : " + res);
		return new ResponseEntity<String>(res, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Data>> getAllData(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID) {
		try {
			List<Data> allData = dataService.getAllData();
			ArrayList<Data> data = new ArrayList<Data>();
			for (Data element : allData) {
				if (element.getPartnerId().equals(partnerID)) {
					data.add(element);
				}
			}
			return new ResponseEntity<List<Data>>(data, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Data>> getAllDecryptedData() {
		try {
			List<Data> data = dataService.getAllData();
			for (Data drivinglicense : data) {
//			drivinglicense.setDataNo(seal.decrypt(drivinglicense.getDataNo()));
				drivinglicense.setDataNo(fhePatternSearchService.decrypt(drivinglicense.getDataNo(),
						drivinglicense.getDataId().toString()));
			}
			return new ResponseEntity<List<Data>>(data, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

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
		try {
			// clean result
			fhePatternSearchService.deleteDir(fhePatternSearchService.getResultDir() + "requestID" + ".ct");
			//
			List<Data> allData = dataService.getAllData();
			ArrayList<String> data = new ArrayList<String>();
			for (Data drivingLicense : allData) {
				if (drivingLicense.getPartnerId().equals(partnerID)) {
//				data.add(drivingLicense.getDriving_license_no() + seal.getFilename() + ".ct");
					data.add(drivingLicense.getDataNo() + drivingLicense.getDataId() + ".ct");
				}
			}
			if (data.size() > 0) {
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Description> createData(
			@ApiParam(name = "file", value = "", example = "") @RequestParam(name = "file", required = true) MultipartFile encryptedFile,
			@ApiParam(name = "partnerID", value = "Partner ID", example = "12345") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "Contract ID", example = "12345") @RequestParam(name = "contractID", required = true) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") @RequestParam(name = "dataType", required = true) Integer dataType,
			@ApiParam(name = "status", value = "0 = inactive, 1 = active - but no used in driving license case.", example = "1") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "driving licences of category A, B, C, etc.", example = "Category B") @RequestParam(name = "description", required = false) String description) {
		try {
			Data drivingLicense = new Data();
			drivingLicense.setStatus(status);
			drivingLicense.setDescription(description);
			drivingLicense.setCreatedDate(tools.dateTimeToStringWithTime(LocalDateTime.now()));
			drivingLicense.setPartnerId(partnerID);
			drivingLicense.setContractId(contractID);
			drivingLicense.setDataType(dataType);
			drivingLicense = dataService.saveOrUpdate(drivingLicense);
			int id = drivingLicense.getDataId();
			String fileDirStr = fhePatternSearchService
					.createLicenseDir(partnerID + File.separator + contractID + File.separator + String.valueOf(id));

			drivingLicense.setDataNo(fileDirStr);
			drivingLicense = dataService.saveOrUpdate(drivingLicense);

			Path fileDir = Paths.get(fileDirStr);

			storageService.setFileDir(fileDir);
			storageService.save(encryptedFile, String.valueOf(id) + ".ct");

			return new ResponseEntity<Description>(new Description().value("" + drivingLicense.getDataId()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Description> updateData(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam(name = "file") MultipartFile file,
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "12345", required = true) @RequestParam(name = "partnerID") String partnerID,
			@ApiParam(name = "contractID", value = "Contract ID", example = "12345", required = true) @RequestParam(name = "contractID", required = false) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") @RequestParam(name = "dataType", required = true) Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "", required = false) @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "banned", required = false) @RequestParam(name = "description", required = false) String description) {
		try {
			Data oldData = dataService.getDataById(Id);
			if (oldData != null) {

				if (partnerID.equals(oldData.getPartnerId()) && contractID.equals(oldData.getContractId())) {
					if (dataType != null && !dataType.equals(oldData.getDataType())) {
						oldData.setStatus(dataType);
					}
					if (status != null && !status.equals(oldData.getStatus())) {
						oldData.setStatus(status);
					}
					if (description != null && !description.equals(oldData.getDescription())) {
						oldData.setDescription(description);
					}
					if (file != null) {
//						Path fileDir = Paths.get(oldData.getDataNo());
//						storageService.setFileDir(fileDir);

						String oldFilePath = oldData.getDataNo() + oldData.getDataId() + ".ct";
						fhePatternSearchService.deleteDir(oldFilePath);
						storageService.save(file, oldData.getDataId() + ".ct", oldData.getDataNo());

					}
					oldData.setUpdatedDate(tools.dateTimeToStringWithTime(LocalDateTime.now()));
					dataService.saveOrUpdate(oldData);
				}
//				return new ResponseEntity<Data>(oldData, HttpStatus.OK);
				return new ResponseEntity<Description>(new Description().value("" + oldData.getDataId()),
						HttpStatus.OK);
			}
		} catch (Exception e) {
//			System.out.println(e.toString());
//			message = "Could not upload the file: " + encryptedFile.getOriginalFilename() + "!";
//			System.out.println(message);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Description> deleteData(
			@ApiParam(name = "id", value = "", example = "", required = true) @RequestParam(value = "id") Integer id,
			@ApiParam(name = "partnerId", value = "", example = "", required = true) @RequestParam(value = "partnerId") String partnerId) {
		try {
			Data drivinglicense = dataService.getDataById(id);
			if (drivinglicense.getPartnerId().equals(partnerId)) {
				fhePatternSearchService.deleteDir(drivinglicense.getDataNo());
				dataService.delete(id);
//				return new ResponseEntity<String>(drivinglicense.getDataNo(), HttpStatus.OK);
				return new ResponseEntity<Description>(new Description().value("Deleted sucessfully !"), HttpStatus.OK);
			}
//			return "Could not delete the driving license id :" + id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
