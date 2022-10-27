package fr.cea.bigpi.fhe.dap.patternsearch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ObjectProperty
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-11-03T21:01:38.571Z")

public class ObjectProperty   {
	@JsonProperty("sessionId")
	  private String sessionId = null;

	  @JsonProperty("description")
	  private String description = null;

	  @JsonProperty("additionalProperties")
	  @Valid
	  private List<Description> additionalProperties = new ArrayList<Description>();

	  public ObjectProperty sessionId(String sessionId) {
	    this.sessionId = sessionId;
	    return this;
	  }

	  /**
	   * Get sessionId
	   * @return sessionId
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull


	  public String getSessionId() {
	    return sessionId;
	  }

	  public void setSessionId(String sessionId) {
	    this.sessionId = sessionId;
	  }

	  public ObjectProperty description(String description) {
	    this.description = description;
	    return this;
	  }

	  /**
	   * Get description
	   * @return description
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull


	  public String getDescription() {
	    return description;
	  }

	  public void setDescription(String description) {
	    this.description = description;
	  }

	  public ObjectProperty additionalProperties(List<Description> additionalProperties) {
	    this.additionalProperties = additionalProperties;
	    return this;
	  }

	  public ObjectProperty addAdditionalPropertiesItem(Description additionalPropertiesItem) {
	    this.additionalProperties.add(additionalPropertiesItem);
	    return this;
	  }

	  /**
	   * Get additionalProperties
	   * @return additionalProperties
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull

	  @Valid

	  public List<Description> getAdditionalProperties() {
	    return additionalProperties;
	  }

	  public void setAdditionalProperties(List<Description> additionalProperties) {
	    this.additionalProperties = additionalProperties;
	  }


	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    ObjectProperty objectProperty = (ObjectProperty) o;
	    return Objects.equals(this.sessionId, objectProperty.sessionId) &&
	        Objects.equals(this.description, objectProperty.description) &&
	        Objects.equals(this.additionalProperties, objectProperty.additionalProperties);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(sessionId, description, additionalProperties);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class ObjectProperty {\n");
	    
	    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
	    sb.append("    description: ").append(toIndentedString(description)).append("\n");
	    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

	  /**
	   * Convert the given object to string with each line indented by 4 spaces
	   * (except the first line).
	   */
	  private String toIndentedString(java.lang.Object o) {
	    if (o == null) {
	      return "null";
	    }
	    return o.toString().replace("\n", "\n    ");
	  }
}

