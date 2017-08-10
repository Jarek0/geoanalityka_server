package pl.gisexpert.rest.model;

@lombok.Setter
@lombok.Getter
public class AddressForm extends BaseForm {
	private String street;
	private String buildingNumber;
	private String flatNumber;
	private String zipCode;
	private String city;
	private String phone;
}