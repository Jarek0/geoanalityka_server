package pl.gisexpert.rest.model;

import pl.gisexpert.cms.model.Address;

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class ContactInfo {
	private String companyName;
	private String taxId;
	
	private String street;
	private String houseNumber;
	private String flatNumber;
	private String zipcode;
	private String city;
	private String phone;

	public ContactInfo(Address address) {
		street = address.getStreet();
		houseNumber = address.getHouseNumber();
		flatNumber = address.getFlatNumber();
		zipcode = address.getZipcode();
		city = address.getCity();
	}
}
