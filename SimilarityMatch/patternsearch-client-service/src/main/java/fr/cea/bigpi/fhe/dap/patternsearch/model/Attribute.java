package fr.cea.bigpi.fhe.dap.patternsearch.model;

public class Attribute implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int index;
	private String attribute;
	private int value;

	// Getter Methods

	public int getIndex() {
		return index;
	}

	public String getAttribute() {
		return attribute;
	}

	public int getValue() {
		return value;
	}

	// Setter Methods

	public void setIndex(int index) {
		this.index = index;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setValue(int value) {
		this.value = value;
	}
}