package pl.gisexpert.rest.model;

public class CompanyAddressForm extends AddressForm  {

	private String taxId;
	private String companyName;
	
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
	
	
	
}
