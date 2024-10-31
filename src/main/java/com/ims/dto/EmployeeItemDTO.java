package com.ims.dto;

public class EmployeeItemDTO {
	private String employeeName;
	private String itemName;

	public EmployeeItemDTO(String employeeName, String itemName) {
		super();
		this.employeeName = employeeName;
		this.itemName = itemName;
	}

	public EmployeeItemDTO() {
		super();
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
