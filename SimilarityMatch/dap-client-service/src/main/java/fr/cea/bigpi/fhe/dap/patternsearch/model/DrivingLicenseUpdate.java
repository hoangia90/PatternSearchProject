package fr.cea.bigpi.fhe.dap.patternsearch.model;

public class DrivingLicenseUpdate implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer driving_license_id;
	private String driving_license_no;
	private Integer status;
	private String description;
	private String partner_id;
	private String contractId;

	public Integer getDriving_license_id() {
		return driving_license_id;
	}

	public void setDriving_license_id(Integer driving_license_id) {
		this.driving_license_id = driving_license_id;
	}

	public String getDriving_license_no() {
		return driving_license_no;
	}

	public void setDriving_license_no(String driving_license_no) {
		this.driving_license_no = driving_license_no;
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
