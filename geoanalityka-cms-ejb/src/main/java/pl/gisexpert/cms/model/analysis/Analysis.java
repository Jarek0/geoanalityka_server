package pl.gisexpert.cms.model.analysis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import pl.gisexpert.cms.model.Account;

@MappedSuperclass
public class Analysis {
	
	@Column(name = "date_started")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateStarted;
	
	@Column(name = "hash", nullable = false, length = 36)
	@NotNull
	private String hash;

	@Column(name = "date_finished")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFinished;

	@Column
	private AnalysisStatus status;
	
	@Column(name = "status_code")
	private AnalysisStatusCode statusCode;
	
	@Column(length = 255)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private Account creator;

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Date getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(Date dateFinished) {
		this.dateFinished = dateFinished;
	}

	public AnalysisStatus getStatus() {
		return status;
	}

	public void setStatus(AnalysisStatus status) {
		this.status = status;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AnalysisStatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(AnalysisStatusCode statusCode) {
		this.statusCode = statusCode;
	}
	
}
