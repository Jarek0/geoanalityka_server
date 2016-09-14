package pl.gisexpert.cms.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "companies", indexes = { @Index(name = "company_name_index", columnList = "company_name", unique = false),
		@Index(name = "company_taxid_index", columnList = "tax_id", unique = false),
		@Index(name = "company_name_taxid_index", columnList = "company_name,tax_id", unique = false) })
public class Company implements Serializable {

	private static final long serialVersionUID = 4364895109590100127L;

	@Id
	@NotAudited
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "company_name", nullable = false, length = 100)
	private String companyName;

	@Column(name = "tax_id", nullable = false, length = 20)
	private String taxId;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	@NotAudited
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "company")
	private CompanyAccount account;
	
	@Column(length = 20)
	private String phone;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public CompanyAccount getAccount() {
		return account;
	}

	public void setAccount(CompanyAccount account) {
		this.account = account;
	}
}
