package fr.cea.bigpi.fhe.dap.patternsearch.model;

public class DataUpdate implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer data_id;
	private String content;
	private Integer status;
	private String description;
	private String partner_id;
	private String contractId;

	public Integer getData_id() {
		return data_id;
	}

	public void setData_id(Integer data_id) {
		this.data_id = data_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getContract_id() {
		return contractId;
	}

	public void setContract_id(String contractId) {
		this.contractId = contractId;
	}

}
