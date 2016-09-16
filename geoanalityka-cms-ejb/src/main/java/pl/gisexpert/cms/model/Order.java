package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "orders", indexes = { @Index(name = "order_hash_index", columnList = "order_hash", unique = true) })
public class Order implements Serializable {

	private static final long serialVersionUID = 5383553825661933604L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 36, name = "order_hash")
	private String orderHash;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "account_orders",
    joinColumns = {
        @JoinColumn(name = "order_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "account_id", referencedColumnName = "id")})
	private Account buyer;

	@Column
	private Integer amount;
	
	@Column(nullable = false, name="order_type")
	private OrderType orderType = OrderType.NONE;

	@Column
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date date;

	@Column(length = 32)
	private String payuOrderId;
	
	@Column(length = 1000)
	private String payuPaymentUrl;

	@Column
	private OrderStatus status;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	private List<Invoice> invoices;

	public Account getBuyer() {
		return buyer;
	}

	public void setBuyer(Account buyer) {
		this.buyer = buyer;
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

	public String getPayuOrderId() {
		return payuOrderId;
	}

	public void setPayuOrderId(String payuOrderId) {
		this.payuOrderId = payuOrderId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getOrderHash() {
		return orderHash;
	}

	public void setOrderHash(String orderHash) {
		this.orderHash = orderHash;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getPayuPaymentUrl() {
		return payuPaymentUrl;
	}

	public void setPayuPaymentUrl(String payuPaymentUrl) {
		this.payuPaymentUrl = payuPaymentUrl;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}
	

}
