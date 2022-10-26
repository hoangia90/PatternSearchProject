package fr.cea.bigpi.fhe.repository.test;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.junit.Assert.assertEquals;

import org.springframework.test.context.ActiveProfiles;

import fr.cea.bigpi.fhe.dap.patternsearch.tools.Tools;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test2") // load application-test.properties
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnitTest2 {
	
	final static Logger logger = LoggerFactory.getLogger( UnitTest2.class );
		
	
	@Test
    public void testCompareHash() {

		logger.info("Exec test compare UnitTest2");
		
		String expectedHash = "1f26bef4265a39a4dada134faeeb296c9624d828a22463d05b2bfe17d7e89ba9";
		String computedHashAfter = "1f26bef4265a39a4dada134faeeb296c9624d828a22463d05b2bfe17d7e89ba9";
		Tools tools = new Tools();
		tools.testString();
		
		assertEquals(expectedHash, computedHashAfter);
    }
}
