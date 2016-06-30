package pl.gisexpert.model.payu;

public class CreateOrderResponse {
	
	private String orderId;
	private String extOrderId;
	private CreateOrderStatus status;
	private String redirectUri;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getExtOrderId() {
		return extOrderId;
	}
	public void setExtOrderId(String extOrderId) {
		this.extOrderId = extOrderId;
	}
	public CreateOrderStatus getStatus() {
		return status;
	}
	public void setStatus(CreateOrderStatus status) {
		this.status = status;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	
}
