package pl.gisexpert.rest.model;

import java.util.List;

public class BillingHistoryResponse extends BaseResponse {
	private List<OrderInfo> orders;

	public List<OrderInfo> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderInfo> orders) {
		this.orders = orders;
	}
}
