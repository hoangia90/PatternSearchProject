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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.bind.annotation.CrossOrigin;

//import com.fasterxml.jackson.core.JsonProcessingException;

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
	@GetMapping("/client/drivingLicense/01-encrypt")
	@ApiOperation(value = "Encrypt Driving License", notes = "This method encrypts a driving license number into encrypted .ct file", tags = {
			"Driving License - Analysis", })
	public @ResponseBody ResponseEntity<byte[]> encryptLicense(
			@ApiParam(name = "number", value = "Any Character", example = "", required = true) @RequestParam("number") String number);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/drivingLicense/02-upload")
	@ApiOperation(value = "Upload Encrypted Driving License Number File (.ct)", notes = "This method uploads a driving license number file and return the requestID number", tags = {
			"Driving License - Analysis", })
	public @ResponseBody ResponseEntity<String> uploadLicense(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@GetMapping("/client/drivingLicense/03-check")
	@ApiOperation(value = "Check Uploaded Encrypted Driving License Number In Database", notes = "This method checks if an uploaded encrypted driving license number is stored in database and returns an encrypted .ct file result. The file result is decrypted with 04-decryptCheckedResult. Note that: the requestID number is used in this method generated from the 02-upload method", tags = {
			"Driving License - Analysis", })
	public @ResponseBody ResponseEntity<byte[]> checkLicense(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID,
			@ApiParam(name = "requestID", value = "", example = "", required = true) @RequestParam("requestID") String requestID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/drivingLicense/04-decryptCheckedResult")
	@ApiOperation(value = "Decrypted Checked Result File", notes = "This method decrypts a result file from 02-checkByFile method", tags = {
			"Driving License - Analysis", })
	public ResponseEntity<String> decryptCheckedResult(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/drivingLicense/04-decryptCheckedResults")
	@ApiOperation(value = "Decrypted Checked Result File", notes = "This method decrypts results", tags = {
			"Driving License - Analysis", })
	public ResponseEntity<String> decryptCheckedResults(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiOperation(value = "Get All Driving License In Database Without Showing Number", nickname = "getAllDrivingLicenses", notes = "Get All Driving License Encrypted Numbers", response = String.class, authorizations = {}, tags = {
			"Driving License - CRUD", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = List.class),
			@ApiResponse(code = 400, message = "Bad request", response = Description.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Description.class),
			@ApiResponse(code = 404, message = "Not found", response = Description.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = Description.class) })
	@GetMapping("/client/drivingLicenses")
	ResponseEntity<List<Data>> getAllDrivingLicenses(
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/drivingLicense")
	@ApiOperation(value = "Create Driving License Number", notes = "This method creates a driving license number", tags = {
			"Driving License - CRUD", })
	public ResponseEntity<Description> createDrivingLicense(
			@ApiParam(name = "drivingLicenseNo", value = "Any Character", example = "") @RequestParam(name = "drivingLicenseNo", required = true) String drivingLicenseNo,
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
	@PutMapping("/client/drivingLicense")
	@ApiOperation(value = "Update Driving License Number", notes = "This method modifies a driving license number", tags = {
			"Driving License - CRUD", })
	public ResponseEntity<Description> updateDrivingLicense(
			@ApiParam(name = "drivingLicenseUpdate", value = "", example = "", required = true) @RequestBody DrivingLicenseUpdate drivingLicenseUpdate);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@DeleteMapping("/client/drivingLicense")
	@ApiOperation(value = "Delete Driving License Number", notes = "This method deletes a driving license number", tags = {
			"Driving License - CRUD", })
	public ResponseEntity<Description> deleteDrivingLicense(
			@ApiParam(name = "id", value = "", example = "", required = true) @RequestParam(name = "id", required = true) Integer id,
			@ApiParam(name = "partnerId", value = "", example = "", required = true) @RequestParam(name = "partnerId", required = true) String partnerId);

	@ApiOperation(value = "Download Encrpted Driving License Number File (.ct)", notes = "This method download an encrpted driving license number file", nickname = "downloadFile", response = ResponseEntity.class, authorizations = {}, tags = {
			"Driving License - Analysis", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response"),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/drivingLicense/download")
	public ResponseEntity<byte[]> downloadLicense(
			@ApiParam(name = "Id", value = "", example = "", required = true) @RequestParam(name = "Id") Integer Id,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/drivingLicense/decrypt")
	@ApiOperation(value = "Decrypted Driving License File", notes = "This method decrypts a driving license number file", tags = {
			"Driving License - Analysis", })
	public ResponseEntity<String> decryptLicense(
			@ApiParam(name = "file", value = "", example = "", required = true) @RequestParam("file") MultipartFile file);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/drivingLicense/05-checkByNo-Auto")
	@ApiOperation(value = "Check An Encrypted Driving License Number In Database", notes = "This method checks if an uploaded encrypted driving license number is stored in database and returns an result", tags = {
			"Driving License - Analysis", })
	public ResponseEntity<String> checkDrivingLicenseByNoAuto(
			@ApiParam(name = "number", value = "Any Character", example = "", required = true) @RequestParam("number") String number,
			@ApiParam(name = "partnerID", value = "", example = "", required = true) @RequestParam("partnerID") String partnerID);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Server response", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = String.class),
			@ApiResponse(code = 404, message = "Not found", response = String.class),
			@ApiResponse(code = 500, message = "Error for HTTPS call trustAnchors", response = String.class) })
	@PostMapping("/client/test/drivingLicense")
	@ApiOperation(value = "Create Driving License Numbers", notes = "This method creates automatically driving license numbers with suffix (-0 -> -101)", tags = {
			"Driving License - Testing", })
	public ResponseEntity<ArrayList<Integer>> createDrivingLicenseData(
			@ApiParam(name = "drivingLicenseNo", value = "Any Character", example = "") @RequestParam(name = "drivingLicenseNo", required = true) String drivingLicenseNo,
			@ApiParam(name = "partnerID", value = "", example = "") @RequestParam(name = "partnerID", required = true) String partnerID,
			@ApiParam(name = "contractID", value = "", example = "") @RequestParam(name = "contractID", required = true) String contractID,
			@ApiParam(name = "dataType", value = "Data Type", example = "12345") @RequestParam(name = "dataType", required = true) Integer dataType,
			@ApiParam(name = "status", value = "0,1,2, etc.", example = "") @RequestParam(name = "status", required = false) Integer status,
			@ApiParam(name = "description", value = "good, banned, etc.", example = "good") @RequestParam(name = "description", required = false) String description);

//	@ApiOperation(value = "Get All Driving License In Database With Showing Number",
//    		nickname = "applySecureComputAPlusB", notes = "get all decrypted driving license",
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
