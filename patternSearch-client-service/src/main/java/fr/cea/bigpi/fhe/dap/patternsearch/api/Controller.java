package fr.cea.bigpi.fhe.dap.patternsearch.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.bind.annotation.CrossOrigin;

//import com.fasterxml.jackson.core.JsonProcessingException;

import fr.cea.bigpi.fhe.dap.patternsearch.model.Data;
import fr.cea.bigpi.fhe.dap.patternsearch.model.DataUpdate;
import fr.cea.bigpi.fhe.dap.patternsearch.model.Description;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/openapi/v1")
public interface Controller {

	final Logger logger = LoggerFactory.getLogger(Controller.class.getName());

	// Used for DeepLab - Begin
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/hashData")
	@ApiOperation(value = "Create An Encrypted File In The Database", notes = "This method encrypts a given information into an encrypted file and then stores it into the database", tags = {
			"DeepLab - Analysis", })
	public ResponseEntity<Description> createHashData(
			@ApiParam(name = "content", value = "Any Character", example = "") @RequestParam(name = "content", required = true) String content,
			@ApiParam(name = "partnerID", value = "", example = "") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "", example = "") @RequestParam(name = "contractID", required = true) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "good") @RequestParam(name = "description", required = false) String description);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PutMapping("/client/hashData")
	@ApiOperation(value = "Update An Encrypted File In The Database", notes = "This method updates the information of an encrypted file in the database", tags = {
			"DeepLab - Analysis", })
	public ResponseEntity<Description> updateHashData(
			@ApiParam(name = "dataUpdate", value = "", example = "", required = true) @RequestBody DataUpdate dataUpdate);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@DeleteMapping("/client/hashData")
	@ApiOperation(value = "Delete An Encrypted File In The Database", notes = "Delete an encrypted file in the database", tags = {
			"DeepLab - Analysis", })
	public ResponseEntity<Description> deleteHashData(
			@ApiParam(name = "id", value = "", example = "", required = true) @RequestParam(name = "id", required = true) Integer id,
			@ApiParam(name = "partnerId", value = "", example = "", required = true) @RequestParam(name = "partnerId", required = true) String partnerId);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/generic/checkHashContent-Auto")
	@ApiOperation(value = "Check Content In Database Automatically", notes = "This method checks if an uploaded encrypted file's information is in database", tags = {
			"DeepLab - Analysis", })
	public ResponseEntity<String> checkHashContentAuto(
			@ApiParam(name = "content", value = "Any Character", example = "", required = true) @RequestParam("content") String content,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiOperation(value = "Upload A List Of Functions (.CSV)", notes = "This method uploads a list of functions", nickname = "uploadCSVFunctionFile", response = String.class, authorizations = {}, tags = {
			"DeepLab - Analysis", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/generic/uploadCSVFunctionFile")
	public @ResponseBody ResponseEntity<String> uploadCSVFunctionFile(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
			@ApiParam(name = "partnerID", value = "", example = "") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "", example = "") @RequestParam(name = "contractID", required = true) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "good") @RequestParam(name = "description", required = false) String description);
	// Used for DeepLab - End

	@ApiOperation(value = "Get All Data From Database Without Showing Its Content", nickname = "getAllData", notes = "Get all data from database without showing its content", response = String.class, authorizations = {}, tags = {
			"Generic - CRUD", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = List.class),
			@ApiResponse(code = 400, message = "Bad request", response = Description.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Description.class),
			@ApiResponse(code = 404, message = "Not found", response = Description.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = Description.class) })
	@GetMapping("/client/data")
	ResponseEntity<List<Data>> getAllData(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data")
	@ApiOperation(value = "Create An Encrypted File In The Database", notes = "This method encrypts a given information into an encrypted file and then stores it into the database", tags = {
			"Generic - CRUD", })
	public ResponseEntity<Description> createData(
			@ApiParam(name = "content", value = "Any Character", example = "") @RequestParam(name = "content", required = true) String content,
			@ApiParam(name = "partnerID", value = "", example = "") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "", example = "") @RequestParam(name = "contractID", required = true) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "good") @RequestParam(name = "description", required = false) String description);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PutMapping("/client/data")
	@ApiOperation(value = "Update An Encrypted File In The Database", notes = "This method updates the information of an encrypted file in the database", tags = {
			"Generic - CRUD", })
	public ResponseEntity<Description> updateData(
			@ApiParam(name = "dataUpdate", value = "", example = "", required = true) @RequestBody DataUpdate dataUpdate);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@DeleteMapping("/client/data")
	@ApiOperation(value = "Delete An Encrypted File In The Database", notes = "Delete an encrypted file in the database", tags = {
			"Generic - CRUD", })
	public ResponseEntity<Description> deleteData(
			@ApiParam(name = "id", value = "", example = "", required = true) @RequestParam(name = "id", required = true) Integer id,
			@ApiParam(name = "partnerId", value = "", example = "", required = true) @RequestParam(name = "partnerId", required = true) String partnerId);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@GetMapping("/client/generic/01-encrypt")
	@ApiOperation(value = "Encrypt Information", notes = "This method encrypts a piece of information into an encrypted .ct file", tags = {
			"Generic - Analysis", })
	public @ResponseBody ResponseEntity<byte[]> encrypt(
			@ApiParam(name = "number", value = "Any Character", example = "", required = true) @RequestParam("number") String number);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/generic/02-upload")
	@ApiOperation(value = "Upload An Encrypted File (.ct)", notes = "This method uploads an encrypted file and return the requestID number", tags = {
			"Generic - Analysis", })
	public @ResponseBody ResponseEntity<String> uploadEncryptedFile(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@GetMapping("/client/generic/03-check")
	@ApiOperation(value = "Check If The Uploaded Encrypted File's Information Is In Database", notes = "This method checks if an uploaded encrypted's information is stored in database and returns an encrypted .ct file result. The file result is decrypted with 04-decryptCheckedResult. Note that: the requestID number is used in this method generated from the 02-upload method", tags = {
			"Generic - Analysis", })
	public @ResponseBody ResponseEntity<byte[]> checkWithEncryptedFile(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/generic/04-decryptCheckResult")
	@ApiOperation(value = "Decrypt Check Result File", notes = "This method decrypts a result file from 02-checkByFile method", tags = {
			"Generic - Analysis", })
	public ResponseEntity<String> decryptCheckResult(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/client/generic/04-decryptCheckResults")
//	@ApiOperation(value = "Decrypted Check Result Files", notes = "This method decrypts results", tags = {
//			"Generic - Analysis", })
//	public ResponseEntity<String> decryptCheckResults(
//			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/generic/05-checkContent-Auto")
	@ApiOperation(value = "Check Content In Database", notes = "This method checks if an uploaded encrypted file's information is stored in database and then returns an result", tags = {
			"Generic - Analysis", })
	public ResponseEntity<String> checkContentAuto(
			@ApiParam(name = "number", value = "Any Character", example = "", required = true) @RequestParam("number") String number,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiOperation(value = "Download An Encrpted File In Database (.ct)", notes = "This method is used for downloading an encrypted file in database", nickname = "downloadEncryptedFile", response = ResponseEntity.class, authorizations = {}, tags = {
			"Generic - Analysis", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response"),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/downloadEncryptedFile")
	public ResponseEntity<byte[]> downloadEncryptedFile(
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/decrypt")
	@ApiOperation(value = "Decrypt An Encrypted File", notes = "This method decrypts an encrypted file", tags = {
			"Generic - Analysis", })
	public ResponseEntity<String> decryptData(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/test/createDataSet")
	@ApiOperation(value = "Create A Data Set", notes = "This method creates automatically a set of data in which the content concates the suffix from -0 to -101)", tags = {
			"For Testing Purposes", })
	public ResponseEntity<ArrayList<Integer>> createDataSet(
			@ApiParam(name = "content", value = "Any Character", example = "") @RequestParam(name = "content", required = true) String content,
			@ApiParam(name = "partnerID", value = "", example = "") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "", example = "") @RequestParam(name = "contractID", required = true) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") @RequestParam(name = "dataType", required = true) Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "good") @RequestParam(name = "description", required = false) String description);

//	@ApiOperation(value = "Get All Data In Database With Showing Number",
//    		nickname = "applySecureComputAPlusB", notes = "get all decrypted data",
//    		response = String.class, tags={ "Driving License", })
//    @ApiResponses(value = { 
//        @ApiResponse(code = 200, message = "Server response", response = Data.class),
//        @ApiResponse(code = 400, message = "Bad request", response = Description.class),
//        @ApiResponse(code = 500, message = "Internal Error", response = Description.class) })
//    @RequestMapping(value = "/crud-data-master/data/decrypteddrivingLicenses",
//        produces = { "application/json" },
//        method = RequestMethod.GET)
//	ResponseEntity<List<Data>> getAllDecryptedDatas();

//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@PostMapping("/client/login")
//	@ApiOperation(value = "Login", notes = "Login",
//			tags={ "Login", })
//	public ResponseEntity<Description> servicelogin(
//			@ApiParam(name = "partnerLoginURL", value = "URL", example = "") @RequestParam(name = "partnerLoginURL", required = true) String partnerLoginURL,
//			@ApiParam(name = "userName", value = "", example = "") @RequestParam(name = "userName", required = true) String userName,
//			@ApiParam(name = "pw", value = "", example = "") @RequestParam(name = "pw", required = true) String pw);

//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
//			@ApiResponse(code = 400, message = "Bad request", response = String.class),
//			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
//			@ApiResponse(code = 404, message = "Not found", response = String.class),
//			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
//	@GetMapping("/client/proxy")
//	@ApiOperation(value = "Proxy", notes = "Proxy",
//			tags={ "Proxy", })
//	public ResponseEntity<Object> proxyGet(
//			@ApiParam(name = "URL", value = "URL", example = "") @RequestParam(name = "URL", required = true) String URL,
//	@ApiParam(name = "cookieSession", value = "cookieSession", example = "") @RequestParam(name = "cookieSession", required = true) String cookieSession);

//	ArrayList<Integer> createDrivingLicenseData(String drivingLicenseNo, String partnerID, Integer status,String description);

}
