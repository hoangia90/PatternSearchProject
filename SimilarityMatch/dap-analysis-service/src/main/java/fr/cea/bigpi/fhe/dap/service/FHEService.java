package fr.cea.bigpi.fhe.dap.service;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import fr.cea.bigpi.fhe.dap.repository.DataRepository;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

@Service
public class FHEService {

	protected Logger logger = LoggerFactory.getLogger(FHEService.class.getName());
	private String appName; //a way to get the default parameter from application.yml
	
	@Autowired DataRepository drivingLicenseRepository;
//	@Autowired private FHEPatternSearchService seal; 
	
	
	@Autowired
	public FHEService(@Value("${spring.application.name}") String appName)
	{
		this.appName = appName;	
	}
	
	@HystrixCommand(    	
    		groupKey="FHE_Service", commandKey = "FHE_PingService",
    		fallbackMethod = "defaultPing" 
	)
	public String ping(String userName) {		
		
		return appName + " say <" + userName + ">";
	}
	
	public String defaultPing(String name, Throwable exception) {    	
    	logger.info("callling from fallback Ping");
    	logger.error("real exception : {}", exception.getMessage());
		return appName + "RuntimeException ! from instance to say Hello <" + name + ">";
	}

	@HystrixCommand(    	
    		groupKey="FHE_Service", commandKey = "FHE_AplusBService",
    		fallbackMethod = "defaultAplusB" 
	)
	public String fhe_executor(String a, String b) throws CompilationFailedException, IOException {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		Script scrpt = shell.parse(new File("src/main/groovy/executor.groovy"));

		binding.setVariable("a", a);
		binding.setVariable("b", b);
		binding.setVariable("tools", scrpt);

		String res = (String)shell.evaluate("tools.fhe_aplusb(a, b)");
		return res; 
	}	
	
	public String defaultAplusB(String a, String b, Throwable error) {
		logger.error("real exception wheen executing a+b : {}", error.getMessage());
		return null;
	}	
}
