package fr.cea.bigpi.fhe.dap.patternsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.sleuth.sampler.*;
import org.springframework.context.annotation.Bean;

//import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
//import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableSwagger2
@EnableCircuitBreaker
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//@EnableFeignClients(clients = { AccountClient.class })
public class Application {
	
	@Value("${security.activation.status}")
	private boolean securityActivationStatus;
	
	/**
	 * Using this Postconstruct For cannot deserialized Datetime Format
	 * */
	@Autowired
	private ObjectMapper objectMapper;
		
	@PostConstruct
	public void setUp() {
	    objectMapper.registerModule(new JavaTimeModule());
	    objectMapper.setSerializationInclusion(Include.NON_NULL);
	    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}
	
	private final String filterPatern = "/openapi/.*";
	private final String basePackage = "fr.cea.bigpi.fhe.dap.patternsearch.api"; 
    /**
	 * the metadata are information visualized in the /basepath/swagger-ui.html
	 * interface, only for documentation
	 */	
	private ApiInfo metadata() {
        return new ApiInfoBuilder()
    		  .title("DMP - PARTTERN SEARCH-Service API documentation")
              .description("This is API documentation for working with DMP - PARTTERN SEARCH Function Engine")
    		  .title("FHE - DRIVING LICENSE - Client Service API documentation")
              .description("This is API documentation for working with FHE - DRIVING LICENSE - Client Features")
              .license("CEA 2.0")
              .licenseUrl("http://www.cea.fr/bigpi/licenses/LICENSE-2.0.html")
              .termsOfServiceUrl("")
              .version("1.0.0")
              .contact(new Contact("","", "contact@cea.fr"))
            .build();
    }
	
	/**
	 * Docket is a SwaggerUI configuration component, in particular specifies to
	 * use the V2.0 (SWAGGER_2) of swagger generated interfaces it also tells to
	 * include only paths that are under /v1/. If other rest interfaces are
	 * added with different base path, they won't be included this path selector
	 * can be removed if all interfaces should be documented.
	 */
	@Bean
	public Docket documentation() {	    
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(metadata());
		if (!securityActivationStatus) {
			return docket
					.select()
					.apis(RequestHandlerSelectors.basePackage(basePackage))
					.paths(PathSelectors.regex(filterPatern))
					.build();
		} else {
			return docket
				//.securitySchemes(new ArrayList<ApiKey>(Arrays.asList(new ApiKey("mykey", "api_key", "header"))))
				.securitySchemes(new ArrayList<BasicAuth>(Arrays.asList(new BasicAuth("basicAuth"))))
				.securityContexts(new ArrayList<SecurityContext>(Arrays.asList(securityContext())))
				.select()
					.apis(RequestHandlerSelectors.basePackage(basePackage))				
				.paths(PathSelectors.regex(filterPatern))
				.build()
				//.directModelSubstitute(java.time.LocalDateTime.class, java.sql.Date.class)
                //.directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
                ;
		}
	}
	
	/**
	 * Selector for the paths this security context applies to ("filterPatern)
	 */
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth()).forPaths(PathSelectors.regex(filterPatern))
				.build();
	}
	
	/**
	 * Here we use the same key defined in the security scheme (basicAuth)
	 */
	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		//return new ArrayList<SecurityReference>(Arrays.asList(new SecurityReference("mykey", authorizationScopes)));
		return new ArrayList<SecurityReference>(Arrays.asList(new SecurityReference("basicAuth", authorizationScopes)));
	}
    
}
