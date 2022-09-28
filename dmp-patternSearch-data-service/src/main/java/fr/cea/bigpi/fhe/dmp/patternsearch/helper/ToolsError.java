package fr.cea.bigpi.fhe.dmp.patternsearch.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ToolsError {

	public HttpStatus getHttpStatus(String message) {
		int errorCode = 0;
		try {
			errorCode = Integer.parseInt(message.replace("error", "").trim());
		} catch (NumberFormatException ex) { // handle your exception
			// Print something
			System.out.println("Error !!!!");
		}
		System.out.println("Error code = " + errorCode);

		HttpStatus httpStatus = HttpStatus.valueOf(errorCode);
		return httpStatus;
	}

	public String createError(HttpStatus httpStatus) {
		return "error " + httpStatus.value();
	}

	public boolean isError(String message) {
		return message != null && message.contains("error");
	}
}
