package fr.cea.bigpi.fhe.repository.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import fr.cea.bigpi.fhe.dap.patternsearch.Application;
import fr.cea.bigpi.fhe.dap.patternsearch.service.FHEPatternSearchService;
import fr.cea.bigpi.fhe.dap.patternsearch.service.FHEService;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = Application.class,
		webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test") // load application-test.properties (or .yml)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleRepositoryTest {

	//@Autowired MetadataRepository repository;
	
	@Autowired FHEService service;
	@Autowired FHEPatternSearchService fhePatternService; 
	
	final static Logger logger = LoggerFactory.getLogger( SampleRepositoryTest.class );
	
	@Test
    public void test01get() throws Exception {
        
		logger.info("Testing 1");
		String res  = service.ping("test01get");
		logger.info("REsponse " + res);		
		//fhePatternService.checkLicense(res, null, res)
    }
	
	@Test
	public void testAddAccount() {
		logger.info("Testing a+b groovy ");
		String restul = null; 
		try {
			restul = service.fhe_executor("21", "12");
		} catch (CompilationFailedException | IOException e) {
			e.printStackTrace();
		}
		logger.info("Testing a+b groovy result : {}", restul);
		assert(restul.isBlank() == false);

//		Account a = new Account();
//		a.setNumber("12345678909");
//		a.setBalance(1232);
//		a.setCustomerId("234353464576586464");
//		repository.save(a);
	}
	
//	@Test
//  public void test02get() throws Exception {
//      
//      String param = "checker";
//      //expected output structure = "{\"name\":\"aName\",\"id\":\"anIdValue\",\"path\":\"aPath\",\"version\":\"1\"}";
//      
//      System.out.println("Testing get DSA ID"); 
//		this.mockMvc.perform(
//    		MockMvcRequestBuilders.fileUpload(endPoint)
//            .file(firstFile)
//            .file(secondFile)
//            .param("dsaId", "DSA-2d072942-9ed8-4258-95c2-da8c4a1a2e9a")
//            .param("hashCode", "1")
//                .accept(MediaType.APPLICATION_JSON)
//             )
//            .andDo(MockMvcResultHandlers.print())
//            .andExpect(status().isOk())
//            ;//  }
	
}
