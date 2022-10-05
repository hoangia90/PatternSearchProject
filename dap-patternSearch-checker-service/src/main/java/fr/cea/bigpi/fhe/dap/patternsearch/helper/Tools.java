package fr.cea.bigpi.fhe.dap.patternsearch.helper;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.cea.bigpi.fhe.dap.patternsearch.model.Description;
import fr.cea.bigpi.fhe.dap.patternsearch.model.ObjectProperty;

@Component
public class Tools {

	final static Logger logger = LoggerFactory.getLogger(Tools.class);

	private static final Logger log = LoggerFactory.getLogger(Tools.class);

	@Value("${spring.application.datetimeFormat:yyyy-MM-dd HH:mm:ss}")
	private String dateTimeFormat; //

	@Value("${spring.application.dateFormat:yyyy-MM-dd}")
	private String dateFormat; //

	public Tools() {
		dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
		dateFormat = "yyyy-MM-dd";
	}
	
	public void testString()
	{
		logger.info("Test of testString methode");
	}

	public boolean creatDir(String dirPath) {
		File f = new File(dirPath);
		// check if the directory can be created
		// using the specified path name
		if (f.mkdir() == true) {
			System.out.println("Directory has been created successfully");
			return true;
		} else {
			System.out.println("Directory cannot be created");
			return false;
		}
	}

	public String extractValue(String key, ObjectProperty objProp) {
		for (Description element : objProp.getAdditionalProperties()) {
			if (element.getMessage().equals(key))
				return element.getValue();
		}
		return null;
	}

//	public String formatInteger(int integer)
//	{
//		return integer >= 10 ? "" + integer : "0" + integer;
//	}

//	public String dateTimeToString(OffsetDateTime currentDate)
//	{
//		 DateTimeFormatter formatter =
//	                DateTimeFormatter.ofPattern(dateTimeFormat);
//		 return currentDate.format(formatter);
//	}	

//	public OffsetDateTime stringToOffsetDateTime(String currentDate)
//	{
//		try {
//			DateTimeFormatter formatter =
//	                DateTimeFormatter.ofPattern(dateTimeFormat);
//			return OffsetDateTime.parse(currentDate, formatter);
//		} catch (DateTimeParseException e) {
//			// TODO: handle exception
//		}
//		return null;		
//	}

//	public String dateTimeToStringWithTime(LocalDateTime currentDate)
//	{	
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
//		return currentDate.format(formatter);	
//	}	

//	public LocalDateTime stringToLocalDateTime(String currentDate)
//	{		
//		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//		try {
//			Date dateToConvert = sdf.parse(currentDate);
//			return Instant.ofEpochMilli(dateToConvert.getTime())
//		      .atZone(ZoneId.systemDefault())
//		      .toLocalDateTime();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}		

//	public String dateTimeToString(LocalDateTime currentDate)
//	{
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
//		return currentDate.format(formatter);	
//	}	

//	public LocalDateTime stringToLocalDateWithTime(String currentDate)
//	{		
//		return stringToDateTime(currentDate, dateTimeFormat);
//	}

//	private LocalDateTime stringToDateTime(String currentDate, String dateTimeformater)
//	{
//		LocalDateTime date = null;
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeformater);
//
//		try {
//			date =  LocalDateTime.parse(currentDate, formatter);			
//		} catch (DateTimeParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return date;
//	}

	/**
	 * @implSpec: get the week of year from the current date
	 * @param currentDate: this date is format yyyy-MM-dd, the method is not
	 *                     supported to the date format yyyy-MM-dd HH:mm:ss
	 */
//	public int getWeekOfYear(String currentDate)
//	{
//		LocalDateTime lcCurrentDate = stringToLocalDateTime(currentDate);
//		WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
//		int weekNumber = lcCurrentDate.get(weekFields.weekOfWeekBasedYear());		
//		return weekNumber;
//	}

//	public LocalDateTime getMonDayFromCurrentDate(LocalDateTime lcCurrentDate)
//	{
//		return lcCurrentDate.with(DayOfWeek.MONDAY);
//	}

//	public LocalDateTime getFirstDayOfCurrentTrimester(Integer year, Integer trimester) {
//		String monthDate = "01-01"; //MM-dd 
//		switch (trimester) {
//		case 1:
//			monthDate = "01-01"; //MM-dd
//			break;
//		case 2:
//			monthDate = "04-01"; //MM-dd
//			break;
//		case 3:
//			monthDate = "07-01"; //MM-dd
//			break;
//		case 4:
//			monthDate = "10-01"; //MM-dd
//			break;	
//		}
//		return stringToLocalDateTime(year + "-"+monthDate);
//	}	

//	public String genNewPassword() 
//	{
//		int minLength = 8;
//		
//	    String generatedString = RandomStringUtils.randomAlphanumeric(minLength);
//	    log.debug("String random gen {}", generatedString);
//	    
//	    return generatedString;	
//	}

//	public String extractValue(String key, ObjectProperty objProp) {
//		for (Description element : objProp.getAdditionalProperties()) {
//			if(element.getMessage().equals(key))
//				return element.getValue(); 
//		}
//		return null;
//	}
}
