package pl.gisexpert.payu.model;

public class OrderNotify extends OrderBase {
	
	private PayMethod payMethod;
	private String status;
	private String orderId;
	private String orderCreateDate;
	
	public PayMethod getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(PayMethod payMethod) {
		this.payMethod = payMethod;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderCreateDate() {
		return orderCreateDate;
	}
	public void setOrderCreateDate(String orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}
	

	
}
