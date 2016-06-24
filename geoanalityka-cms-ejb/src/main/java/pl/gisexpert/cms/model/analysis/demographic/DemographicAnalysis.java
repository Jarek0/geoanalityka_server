package pl.gisexpert.cms.model.analysis.demographic;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import pl.gisexpert.cms.model.analysis.Analysis;
import pl.gisexpert.model.gis.Coordinate;

@Entity
@Inheritance
@DiscriminatorColumn(name = "analysis_type")
@Table(name = "demographic_analyses", indexes = {
		@Index(name = "account_index", columnList = "account_id", unique = false),
		@Index(name = "hash_index", columnList = "hash", unique = true) })
@NamedQueries({
		@NamedQuery(name = "DemographicAnalysis.findRecentAnalysesFroAccount", query = "SELECT analysis FROM DemographicAnalysis analysis LEFT OUTER JOIN analysis.creator creator WHERE creator = :account") })
public class DemographicAnalysis extends Analysis implements Serializable {

	private static final long serialVersionUID = -4420992994918486571L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Integer radius;

	@Column
	@Lob
	private Coordinate location;

	@Column
	private String locationDisplayName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public String getLocationDisplayName() {
		return locationDisplayName;
	}

	public void setLocationDisplayName(String locationDisplayName) {
		this.locationDisplayName = locationDisplayName;
	}

}
