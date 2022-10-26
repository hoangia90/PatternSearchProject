package fr.cea.bigpi.fhe.dap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonCreator;
//import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

@Entity
@Table(name = "data")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-06-25T21:18:03.467Z")
public class Data {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("dataId")
//	@Column(name = "data_id")
	private Integer dataId = null;

	@JsonProperty("dataType")
	private Integer dataType = null;

	@JsonProperty("partnerId")
	private String partnerId = null;

	@JsonProperty("contractId")
	private String contractId = null;

	@Column(columnDefinition = "TEXT", name = "content")
	@JsonProperty("content")
	private String content = null;

	@JsonProperty("status")
	private Integer status = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("createdDate")
	@Column(name = "created_date")
	private String createdDate = null;

	@JsonProperty("updatedDate")
	@Column(name = "updated_date")
	private String updatedDate = null;

	public Data dataId(Integer dataId) {
		this.dataId = dataId;
		return this;
	}

	/**
	 * Get dataId
	 * 
	 * @return dataId
	 **/
	@ApiModelProperty(value = "")

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	//
	public Data dataType(Integer dataType) {
		this.dataType = dataType;
		return this;
	}

	/**
	 * Get dataType
	 * 
	 * @return dataType
	 **/
	@ApiModelProperty(value = "")

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	//

	public Data partnerId(String partnerId) {
		this.partnerId = partnerId;
		return this;
	}

	/**
	 * Get partnerId
	 * 
	 * @return partnerId
	 **/
	@ApiModelProperty(value = "")

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * Get contractId
	 * 
	 * @return contractId
	 **/
	@ApiModelProperty(value = "")

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public Data dataNo(String content) {
		this.content = content;
		return this;
	}

	/**
	 * Get content
	 * 
	 * @return content
	 **/
	@ApiModelProperty(value = "")

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Data status(Integer status) {
		this.status = status;
		return this;
	}

	/**
	 * Get status
	 * 
	 * @return status
	 **/
	@ApiModelProperty(value = "")

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Data description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Get description
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "")

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Data createdDate(String createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	/**
	 * Get createdDate
	 * 
	 * @return createdDate
	 **/
	@ApiModelProperty(value = "")

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Data updatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
		return this;
	}

	/**
	 * Get updatedDate
	 * 
	 * @return updatedDate
	 **/
	@ApiModelProperty(value = "")

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Data data = (Data) o;
		return Objects.equals(this.dataId, data.dataId) && Objects.equals(this.dataType, data.dataType)
				&& Objects.equals(this.partnerId, data.partnerId) && Objects.equals(this.contractId, data.contractId)
				&& Objects.equals(this.content, data.content) && Objects.equals(this.status, data.status)
				&& Objects.equals(this.description, data.description)
				&& Objects.equals(this.createdDate, data.createdDate)
				&& Objects.equals(this.updatedDate, data.updatedDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataId, dataType, partnerId, content, status, description, createdDate, updatedDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Data {\n");
		sb.append("    dataId: ").append(toIndentedString(dataId)).append("\n");
		sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
		sb.append("    partnerId: ").append(toIndentedString(partnerId)).append("\n");
		sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
		sb.append("    content: ").append(toIndentedString(content)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
		sb.append("    updatedDate: ").append(toIndentedString(updatedDate)).append("\n");
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
