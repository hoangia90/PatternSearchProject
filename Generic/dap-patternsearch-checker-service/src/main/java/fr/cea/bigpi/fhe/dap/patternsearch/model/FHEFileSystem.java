package fr.cea.bigpi.fhe.dap.patternsearch.model;

import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @implSpec the file content if use would like to store in plain/text, user should get String and store it directly
 * if use want to store in byte, then the content will be convert to Base64 string and save it   
 * */

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * FHEFileSystem
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-10-06T13:08:21.134Z")
public class FHEFileSystem   {
  @JsonProperty("fileName")
  private String fileName = null;

  @JsonProperty("format")
  private String format = null;

  @JsonProperty("contentBase64")
  private String contentBase64 = null;

  public FHEFileSystem fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  /**
   * Get fileName
   * @return fileName
  **/
  @ApiModelProperty(example = "filename can be empty or not if defined", required = true, value = "")
  @NotNull


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public FHEFileSystem format(String format) {
    this.format = format;
    return this;
  }

  /**
   * Get format
   * @return format
  **/
  @ApiModelProperty(example = "version supported for 'zip', 'tar', '.gz' format", value = "")


  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public FHEFileSystem contentBase64(String contentBase64) {
    this.contentBase64 = contentBase64;
    return this;
  }

  /**
   * Get contentBase64
   * @return contentBase64
  **/
  @ApiModelProperty(example = "U3dhZ2dlciByb2Nrcw== (CONTENT in BINARY format :  base64-encoded characters)", required = true, value = "")
  @NotNull


  public String getContentBase64() {
    return contentBase64;
  }

  public void setContentBase64(String contentBase64) {
    this.contentBase64 = contentBase64;
  }



	@JsonIgnore
	public byte [] getContentByteArray () {
		return Base64.getDecoder().decode(contentBase64.getBytes());
	}
	
	@JsonIgnore
	public void setContentBase64(byte[] content) {
		this.contentBase64 = Base64.getEncoder().encodeToString(content);
	}
	
	public FHEFileSystem contentByteArray(byte[] content) {
		this.contentBase64 = Base64.getEncoder().encodeToString(content);
		return this;
	}

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FHEFileSystem fhEFileSystem = (FHEFileSystem) o;
    return Objects.equals(this.fileName, fhEFileSystem.fileName) &&
        Objects.equals(this.format, fhEFileSystem.format) &&
        Objects.equals(this.contentBase64, fhEFileSystem.contentBase64);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileName, format, contentBase64);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FHEFileSystem {\n");
    
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    contentBase64: ").append(toIndentedString(contentBase64)).append("\n");
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
