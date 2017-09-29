package pl.gisexpert.rest.model;

@lombok.Setter
@lombok.Getter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class AddressForm{
	private String street;
	private String buildingNumber;
	private String flatNumber;
	private String zipCode;
	private String city;
	private String phone;
}