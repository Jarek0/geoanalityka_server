package pl.gisexpert.rest.model;

import pl.gisexpert.cms.model.Address;

public class ContactInfo {
	private String companyName;
	private String taxId;
	
	private String street;
	private String houseNumber;
	private String flatNumber;
	private String zipcode;
	private String city;
	private String phone;
	
	public ContactInfo(){
		
	}

	public ContactInfo(Address address) {
		street = address.getStreet();
		houseNumber = address.getHouseNumber();
		flatNumber = address.getFlatNumber();
		zipcode = address.getZipcode();
		city = address.getCity();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zip) {
		this.zipcode = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
