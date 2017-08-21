package pl.gisexpert.rest.model;

import javax.ws.rs.core.Response.Status;

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class BaseResponse {
	private Status responseStatus;
	private String message;
}
