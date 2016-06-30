package pl.gisexpert.model.payu;

import java.util.List;

public class CreateOrderNotify {
	private OrderNotify order;
	private String localReceiptDateTime;
	private List<Property> properties;
	
	public OrderNotify getOrder() {
		return order;
	}
	public void setOrder(OrderNotify order) {
		this.order = order;
	}
	public String getLocalReceiptDateTime() {
		return localReceiptDateTime;
	}
	public void setLocalReceiptDateTime(String localReceiptDateTime) {
		this.localReceiptDateTime = localReceiptDateTime;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	
}
