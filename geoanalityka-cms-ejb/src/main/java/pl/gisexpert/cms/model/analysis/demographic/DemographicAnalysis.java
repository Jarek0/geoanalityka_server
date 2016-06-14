package pl.gisexpert.cms.model.analysis.demographic;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.analysis.AnalysisStatus;

@Entity
@Inheritance
@DiscriminatorColumn(name = "analysis_type")
@Table(name = "demographic_analyses", indexes = {
		@Index(name = "account_index", columnList = "account_id", unique = false) })
public class DemographicAnalysis implements Serializable {

	private static final long serialVersionUID = -4420992994918486571L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="date_started")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateStarted;

	@Column(name="date_finished")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFinished;

	@Column
	private AnalysisStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private Account creator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
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

}
