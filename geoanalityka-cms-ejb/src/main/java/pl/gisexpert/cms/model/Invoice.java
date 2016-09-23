package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "invoices", indexes = {
		@Index(name = "invoices_date_index", columnList = "date_created", unique = false)
})
@SqlResultSetMapping(name="InvoiceSerialId", classes = {
	    @ConstructorResult(targetClass = String.class, 
	    columns = {@ColumnResult(name="value")})
	})
@NamedQueries({
	@NamedQuery(name = "Invoice.getInvoiceByOrderAndType", query = "SELECT invoice FROM Invoice invoice WHERE invoice.order = :order AND invoice.mimeType = :mimeType AND invoice.original = :original")
})
public class Invoice implements Serializable {

	private static final long serialVersionUID = -2104814516271315615L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 30, name = "serial_id")
	private String serialId;
	
	@Column(name = "date_created")
	@Temporal(TemporalType.DATE)
	private Date dateCreated;
	
	@Column(name = "data")
	private byte[] invoiceData;
	
	@Column(name="mime_type")
	private String mimeType;
	
	@ManyToOne
    @JoinTable(name = "order_invoices",
    joinColumns = {
        @JoinColumn(name = "invoice_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "order_id", referencedColumnName = "id")})
	private Order order;
	
	@Column
	private Boolean original;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getInvoiceData() {
		return invoiceData;
	}

	@Lob
	public void setInvoiceData(byte[] invoice) {
		this.invoiceData = invoice;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Boolean getOriginal() {
		return original;
	}

	public void setOriginal(Boolean original) {
		this.original = original;
	}
	
	
}
