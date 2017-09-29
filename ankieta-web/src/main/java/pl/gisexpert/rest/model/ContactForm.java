package pl.gisexpert.rest.model;

import org.hibernate.validator.constraints.Email;

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class ContactForm{

	private String name;
	@Email
	private String email;
	private String message;
}
