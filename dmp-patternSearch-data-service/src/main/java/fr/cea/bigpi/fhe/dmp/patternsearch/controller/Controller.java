package fr.cea.bigpi.fhe.dmp.patternsearch.controller;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import fr.cea.bigpi.fhe.dmp.patternsearch.model.Data;
import fr.cea.bigpi.fhe.dmp.patternsearch.model.Description;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/openapi/v1")
public interface Controller {

//	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@ApiOperation(value = "call hello and simulating failException", nickname = "hello", notes = "call helloWorld", response = String.class, authorizations = {}, tags = {
			"hello", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@RequestMapping(value = "/name/{username}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<String> hello(@PathVariable("username") String userName);

	@ApiOperation(value = "Get All Data In Database Without Showing Their Information", nickname = "getAllEncryptedData", notes = "Get All Data with their storage path", response = String.class, authorizations = {}, tags = {
			"Data", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = List.class),
			@ApiResponse(code = 400, message = "Bad request", response = Description.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Description.class),
			@ApiResponse(code = 404, message = "Not found", response = Description.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = Description.class) })
	@RequestMapping(value = "/crud-data-master/data/all", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Data>> getAllData(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiOperation(value = "Get All Data In Database With Showing Their Information", nickname = "getAllDecryptedData", notes = "get all decrypted data", response = String.class, tags = {
			"Data", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = Data.class),
			@ApiResponse(code = 400, message = "Bad request", response = Description.class),
			@ApiResponse(code = 500, message = "Internal Error", response = Description.class) })
	@RequestMapping(value = "/crud-data-master/data/all/decrypted", produces = {
			"application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Data>> getAllDecryptedData();

	@ApiOperation(value = "Upload Encrypted Encrypted File (.ct)", notes = "This method uploads a encrypted file and return the requestID number", nickname = "uploadEncryptedDataFile", response = String.class, authorizations = {}, tags = {
			"Data", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/prepare-data-master/drivingLicense/01-uploadFile")
	public @ResponseBody ResponseEntity<String> uploadEncryptedFile(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiOperation(value = "Check Uploaded Encrypted Info Is In Database", notes = "This method checks if an uploaded infomation is stored in database and returns an encrypted .ct file result. The file result is decrypted with 04-decryptCheckedResult. Note that: the requestID number is used in this method generated from the 01-uploadFile", nickname = "checkByFile", response = ResponseEntity.class, authorizations = {}, tags = {
			"Driving License", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/prepare-data-master/drivingLicense/02-checkByFile")
	public @ResponseBody ResponseEntity<byte[]> checkWithEncryptedFile(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID);

	@ApiOperation(value = "Create Data", notes = "This method creates an encrypted file into database", nickname = "createData", response = Data.class, authorizations = {}, tags = {
			"Data", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/crud-data-master/data")
	public ResponseEntity<Description> createData(
			@ApiParam(name = "file", value = "", example = "") @RequestParam(name = "file") MultipartFile encryptedFile,
			@ApiParam(name = "partnerID", value = "Partner ID", example = "12345") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "contract ID", example = "12345") @RequestParam(name = "contractID", required = true) String contractId,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") @RequestParam(name = "dataType", required = true) Integer dataType,
			@ApiParam(name = "status", value = "0 = inactive, 1 = active - but no used in driving license case.", example = "1") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "A, B, C, etc.", example = "Category B") @RequestParam(name = "description", required = false) String description);

	@ApiOperation(value = "Update Data", notes = "This method updates the information of an encrypted file", nickname = "updateData", response = Data.class, authorizations = {}, tags = {
			"Data", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PutMapping("/crud-data-master/data")
	public ResponseEntity<Description> updateData(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam(name = "file") MultipartFile file,
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "12345", required = true) @RequestParam(name = "partnerID") String partnerID,
			@ApiParam(name = "contractID", value = "contract Id", example = "12345", required = true) @RequestParam(name = "contractID", required = true) String contractId,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") @RequestParam(name = "dataType", required = true) Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "", required = false) @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "banned", required = false) @RequestParam(name = "description", required = false) String description);

	@ApiOperation(value = "Delete Data", notes = "This method deletes an ecrypted file in database", nickname = "deleteData", response = String.class, authorizations = {}, tags = {
			"Data", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@DeleteMapping("/crud-data-master/data")
	public ResponseEntity<Description> deleteData(
			@ApiParam(name = "id", value = "", example = "", required = true) @RequestParam(value = "id") Integer id,
			@ApiParam(name = "partnerId", value = "", example = "", required = true) @RequestParam(value = "partnerId") String partnerId);

}
