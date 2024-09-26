package com.ims.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String category;

	@Column(unique = true, nullable = false)
	private String serialNumber;

	@Column(unique = true, nullable = false)
	private String billNumber;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private LocalDate dateOfPurchase;

	@Column(name = "warranty", nullable = false)
	private String warranty;

	public Item(int id, String name, String category, String serialNumber, String billNumber, LocalDate dateOfPurchase,
			String warranty) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.serialNumber = serialNumber;
		this.billNumber = billNumber;
		this.dateOfPurchase = dateOfPurchase;
		this.warranty = warranty;
	}

	public Item() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public LocalDate getDateOfPurchase() {
		return dateOfPurchase;
	}

	public void setDateOfPurchase(LocalDate dateOfPurchase) {
		this.dateOfPurchase = dateOfPurchase;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

}
