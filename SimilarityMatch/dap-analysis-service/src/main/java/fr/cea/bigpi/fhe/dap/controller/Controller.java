package fr.cea.bigpi.fhe.dap.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import fr.cea.bigpi.fhe.dap.model.FHEFileSystem;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/openapi/v1")
public interface Controller {

	final Logger logger = LoggerFactory.getLogger(Controller.class.getName());
	
	@ApiOperation(value = "Upload Encrypted File (.ct)", notes = "This method uploads an encrypted file and return the requestID number", nickname = "uploadEncryptedFile", response = String.class, authorizations = {}, tags = {
			"Data For Client", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/crud-data-master/check/01-uploadEncryptedFile")
	public @ResponseBody ResponseEntity<String> uploadEncryptedFile(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);
	
	@ApiOperation(value = "Download Encrpted File", notes = "This method download an encrpted file", nickname = "downloadEncrptedFile", response = ResponseEntity.class, authorizations = {}, tags = {
			"Data For Client", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response"),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/crud-data-master/check/downloadEncryptedFile")
	public ResponseEntity<byte[]> downloadEncryptedFile(
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);
	
//	@ApiOperation(value = "Check If An Uploaded Encrypted File's Information Is In Database", notes = "This method checks if an uploaded encrypted file's information exists in database and returns an encrypted .ct file result. The file result is decrypted with 04-decryptCheckedResult. Note that: the requestID number is used in this method generated from the 01-uploadFile", nickname = "checkWithEncryptedFile", response = ResponseEntity.class, authorizations = {}, tags = {
//			"Data For Client", })
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/crud-data-master/check/02-checkWithEncryptedFile")
//	public @ResponseBody ResponseEntity<byte[]> checkWithEncryptedFile(
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
//			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID);
	
	@ApiOperation(value = "Check If An Uploaded Encrypted File's Information Is In Database", notes = "This method checks if an uploaded encrypted file's information exists in database and returns an encrypted .ct file result. The file result is decrypted with 04-decryptCheckedResult. Note that: the requestID number is used in this method generated from the 01-uploadFile", nickname = "checkWithEncryptedFile", response = ResponseEntity.class, authorizations = {}, tags = {
			"Data For Client", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/crud-data-master/check/02-checkWithEncryptedFile")
	public @ResponseBody ResponseEntity<byte[]> checkWithEncryptedFile(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID,
			@ApiParam(name = "dataId", value = "", example = "", required = true) @RequestParam("dataId") String dataId);
	
	
//	@ApiOperation(value = "Upload Encrypted Data File (.ct)", notes = "This method uploads a data file and return the requestID number", nickname = "uploadDataFile", response = String.class, authorizations = {}, tags = {
//			"Data Analysis", })
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/analysis-master/01-uploadFile")
//	public @ResponseBody ResponseEntity<String> uploadDataFile(
//			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

//	@ApiOperation(value = "Check If An Uploaded Encrypted Data Is In Database", notes = "This method checks if an uploaded encrypted data exists in database and returns an encrypted .ct file result. The file result is decrypted with 04-decryptCheckedResult. Note that: the requestID number is used in this method generated from the 01-uploadFile", nickname = "drivingLicenseCheckByFile", response = ResponseEntity.class, authorizations = {}, tags = {
//			"Data Analysis", })
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/analysis-master/02-checkByFile")
//	public @ResponseBody ResponseEntity<byte[]> dataCheckByFile(
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
//			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID,
//			@ApiParam(name = "contractID", value = "", example = "", required = true) @RequestParam("contractID") String contractID,
//			@ApiParam(name = "dataID", value = "", example = "", required = true) @RequestParam("dataID") Integer dataID);

//	@ApiOperation(value = "Download Encrpted Data File", notes = "This method download an encrpted data file", nickname = "downloadFile", response = ResponseEntity.class, authorizations = {}, tags = {
//			"Data Analysis", })
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response"),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/analysis-master/downloadFile")
//	public ResponseEntity<byte[]> downloadDataFile(
//			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

//	@ApiOperation(value = "Concate Checked Result Files", notes = "This method concates multiple checked result files", nickname = "concateCheckedResultFiles", response = ResponseEntity.class, authorizations = {}, tags = {
//			"Data Analysis For Distributed Computing", })
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response"),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/analysis-master/concateCheckedResultFiles")
//	public ResponseEntity<FHEFileSystem> concateFiles(
//			@ApiParam(name = "file1", value = "", example = "", required = true) @RequestParam("file1") FHEFileSystem file1,
//			@ApiParam(name = "file2", value = "", example = "", required = true) @RequestParam("file2") FHEFileSystem file2,
//			@ApiParam(name = "filename", value = "", example = "", required = true) @RequestParam("filename") String filename);

//	@ApiOperation(value = "Check If An Uploaded Encrypted Data Is In A List", notes = "This method checks if an uploaded encrypted driving license number exists in the given list. The file result is decrypted with 04-decryptCheckedResult", nickname = "checkByFilewList", response = ResponseEntity.class, authorizations = {}, tags = {
//			"Data Analysis For Distributed Computing", })
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/analysis-master/checkByFilewList")
//	public @ResponseBody ResponseEntity<byte[]> dataCheckByFilewList(
//			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
//			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID,
//			@ApiParam(name = "files", value = "", example = "", required = true) @RequestParam("files") ArrayList<String> files);

}
