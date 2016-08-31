package pl.gisexpert.rest.model;


import java.util.Date;

import pl.gisexpert.cms.model.Order;
import pl.gisexpert.cms.model.OrderStatus;
import pl.gisexpert.cms.model.OrderType;

public class OrderInfo {

	private String orderHash;
	private Integer amount;
	private Date date;
	private OrderStatus status;
	private OrderType orderType;
	
	public OrderInfo () {
		
	}
	
	public OrderInfo(Order order) {
		orderHash = order.getOrderHash();
		amount = order.getAmount();
		date = order.getDate();
		status = order.getStatus();
		orderType = order.getOrderType();
	}

	public String getOrderHash() {
		return orderHash;
	}

	public void setOrderHash(String orderHash) {
		this.orderHash = orderHash;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	
}
