package fr.cea.bigpi.fhe.dap.patternsearch.helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.cea.bigpi.fhe.dap.patternsearch.model.Description;
import fr.cea.bigpi.fhe.dap.patternsearch.model.ObjectProperty;


@Component
public class Tools {
	
	final static Logger logger = LoggerFactory.getLogger( Tools.class );
	
	@Autowired
	public Tools()
	{}
	
	public void testString()
	{
		logger.info("Test of testString methode");
	}
	
	public String extractValue(String key, ObjectProperty objProp) {
		for (Description element : objProp.getAdditionalProperties()) {
			if(element.getMessage().equals(key))
				return element.getValue(); 
		}
		return null;
	}
}
