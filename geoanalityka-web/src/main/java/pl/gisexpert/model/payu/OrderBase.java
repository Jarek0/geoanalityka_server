package pl.gisexpert.model.payu;

import java.util.List;

public class OrderBase {
	
	private String notifyUrl;
	private String customerIp;
	private String merchantPosId;
	private String extOrderId;
	
	private String description;
	private String currencyCode;
	private Integer totalAmount;
	private List<Product> products;
	private Buyer buyer;
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getCustomerIp() {
		return customerIp;
	}
	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}
	public String getMerchantPosId() {
		return merchantPosId;
	}
	public void setMerchantPosId(String merchantPosId) {
		this.merchantPosId = merchantPosId;
	}
	public String getExtOrderId() {
		return extOrderId;
	}
	public void setExtOrderId(String extOrderId) {
		this.extOrderId = extOrderId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public Buyer getBuyer() {
		return buyer;
	}
	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}
	
}
