package fr.cea.bigpi.fhe.dap.patternsearch.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.cea.bigpi.fhe.dap.patternsearch.model.Data;
import fr.cea.bigpi.fhe.dap.patternsearch.model.Description;
import fr.cea.bigpi.fhe.dap.patternsearch.model.DrivingLicenseUpdate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/openapi/v1")
public interface Controller {

	final Logger logger = LoggerFactory.getLogger(Controller.class.getName());

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@GetMapping("/client/data/01-encrypt")
	@ApiOperation(value = "Encrypt Data", notes = "This method encrypts a Data number into encrypted .ct file", tags = {
			"Data - Analysis", })
	public @ResponseBody ResponseEntity<byte[]> encryptLicense(
			@ApiParam(name = "number", value = "Any Character", example = "", required = true) @RequestParam("number") String number);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/02-upload")
	@ApiOperation(value = "Upload Encrypted Data Number File (.ct)", notes = "This method uploads a Data number file and return the requestID number", tags = {
			"Data - Analysis", })
	public @ResponseBody ResponseEntity<String> uploadLicense(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@GetMapping("/client/data/03-check")
	@ApiOperation(value = "Check Uploaded Encrypted Data Number In Database", notes = "This method checks if an uploaded encrypted Data number is stored in database and returns an encrypted .ct file result. The file result is decrypted with 04-decryptCheckedResult. Note that: the requestID number is used in this method generated from the 02-upload method", tags = {
			"Data - Analysis", })
	public @ResponseBody ResponseEntity<byte[]> checkLicense(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/04-decryptCheckedResult")
	@ApiOperation(value = "Decrypted Checked Result File", notes = "This method decrypts a result file from 02-checkByFile method", tags = {
			"Data - Analysis", })
	public ResponseEntity<String> decryptCheckedResult(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/04-decryptCheckedResults")
	@ApiOperation(value = "Decrypted Checked Result File", notes = "This method decrypts results", tags = {
			"Data - Analysis", })
	public ResponseEntity<String> decryptCheckedResults(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiOperation(value = "Get All Data In Database Without Showing Number", nickname = "getAllData", notes = "Get All Data Encrypted Numbers", response = String.class, authorizations = {}, tags = {
			"Data - CRUD", })
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
	@ApiOperation(value = "Create Data Number", notes = "This method creates a Data number", tags = {
			"Data - CRUD", })
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
	@ApiOperation(value = "Update Data Number", notes = "This method modifies a Data number", tags = {
			"Data - CRUD", })
	public ResponseEntity<Description> updateData(
			@ApiParam(name = "dataUpdate", value = "", example = "", required = true) @RequestBody DataUpdate dataUpdate);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@DeleteMapping("/client/data")
	@ApiOperation(value = "Delete Data Number", notes = "This method deletes a Data number", tags = {
			"Data - CRUD", })
	public ResponseEntity<Description> deleteData(
			@ApiParam(name = "id", value = "", example = "", required = true) @RequestParam(name = "id", required = true) Integer id,
			@ApiParam(name = "partnerId", value = "", example = "", required = true) @RequestParam(name = "partnerId", required = true) String partnerId);

	@ApiOperation(value = "Download Encrpted Data Number File (.ct)", notes = "This method download an encrpted Data number file", nickname = "downloadFile", response = ResponseEntity.class, authorizations = {}, tags = {
			"Data - Analysis", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response"),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/download")
	public ResponseEntity<byte[]> downloadLicense(
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/decrypt")
	@ApiOperation(value = "Decrypted Data File", notes = "This method decrypts a Data number file", tags = {
			"Data - Analysis", })
	public ResponseEntity<String> decryptLicense(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/data/05-checkByNo-Auto")
	@ApiOperation(value = "Check An Encrypted Data Number In Database", notes = "This method checks if an uploaded encrypted Data number is stored in database and returns an result", tags = {
			"Data - Analysis", })
	public ResponseEntity<String> checkDataByNoAuto(
			@ApiParam(name = "number", value = "Any Character", example = "", required = true) @RequestParam("number") String number,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/test/data")
	@ApiOperation(value = "Create Data Numbers", notes = "This method creates automatically Data numbers", tags = {
			"Data - Testing", })
	public ResponseEntity<ArrayList<Integer>> createData(
			@ApiParam(name = "content", value = "Any Character", example = "") @RequestParam(name = "content", required = true) String content,
			@ApiParam(name = "partnerID", value = "", example = "") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "", example = "") @RequestParam(name = "contractID", required = true) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345")  @RequestParam(name = "dataType", required = true) Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "good") @RequestParam(name = "description", required = false) String description);

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
