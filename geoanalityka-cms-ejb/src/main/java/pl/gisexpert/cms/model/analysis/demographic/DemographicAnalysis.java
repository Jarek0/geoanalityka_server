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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import pl.gisexpert.cms.model.analysis.Analysis;
import pl.gisexpert.cms.model.analysis.AreaType;
import pl.gisexpert.cms.model.analysis.TravelType;
import pl.gisexpert.model.gis.Coordinate;

@Entity
@Inheritance
@DiscriminatorColumn(name = "analysis_type")
@Table(name = "demographic_analyses", indexes = {
		@Index(name = "demographic_analysis_account_index", columnList = "account_id", unique = false),
		@Index(name = "demographic_analysis_hash_index", columnList = "hash", unique = true) })
@NamedQueries({
		@NamedQuery(name = "DemographicAnalysis.findRecentAnalysesForAccount", query = "SELECT analysis FROM DemographicAnalysis analysis LEFT OUTER JOIN analysis.creator creator WHERE creator = :account AND analysis.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED ORDER BY analysis.dateStarted DESC"),
		@NamedQuery(name = "DemographicAnalysis.getAnalysesDetailsToCompareForAccount", query = "SELECT analysis FROM DemographicAnalysis analysis LEFT OUTER JOIN analysis.creator creator WHERE creator = :account AND analysis.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED AND analysis.hash IN (:hashes)"),
		@NamedQuery(name = "DemographicAnalysis.setDeletedStatusForAllAnalysesForAccount", query = "UPDATE DemographicAnalysis analysis SET analysis.status = pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED WHERE analysis.id IN (SELECT analysis1.id FROM DemographicAnalysis analysis1 LEFT OUTER JOIN analysis1.creator creator WHERE creator = :account AND analysis1.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED AND analysis1.hash IN (:hashes))"),
		@NamedQuery(name = "DemographicAnalysis.findRecentAnalysesByTypeOrderedByNameDESCForAccount", query = "SELECT analysis FROM DemographicAnalysis analysis LEFT OUTER JOIN analysis.creator creator WHERE (creator = :account AND TYPE(analysis) IN :classes AND analysis.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED) ORDER BY analysis.name DESC, analysis.dateStarted DESC"),
		@NamedQuery(name = "DemographicAnalysis.findRecentAnalysesByTypeOrderedByNameASCForAccount", query = "SELECT analysis FROM DemographicAnalysis analysis LEFT OUTER JOIN analysis.creator creator WHERE (creator = :account AND TYPE(analysis) IN :classes AND analysis.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED) ORDER BY analysis.name ASC, analysis.dateStarted DESC"),
		@NamedQuery(name = "DemographicAnalysis.findRecentAnalysesByTypeOrderedByDateDESCForAccount", query = "SELECT analysis FROM DemographicAnalysis analysis LEFT OUTER JOIN analysis.creator creator WHERE (creator = :account AND TYPE(analysis) IN :classes AND analysis.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED) ORDER BY analysis.dateStarted DESC"),
		@NamedQuery(name = "DemographicAnalysis.findRecentAnalysesByTypeOrderedByDateASCForAccount", query = "SELECT analysis FROM DemographicAnalysis analysis LEFT OUTER JOIN analysis.creator creator WHERE (creator = :account AND TYPE(analysis) IN :classes AND analysis.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED) ORDER BY analysis.dateStarted ASC"),
		@NamedQuery(name = "DemographicAnalysis.updateAnalysisNameForAccount", query = "UPDATE DemographicAnalysis analysis SET analysis.name = :name WHERE analysis.id IN (SELECT analysis1.id FROM DemographicAnalysis analysis1 LEFT OUTER JOIN analysis1.creator creator WHERE creator = :account AND analysis1.status != pl.gisexpert.cms.model.analysis.AnalysisStatus.DELETED AND analysis1.hash IN :hash)") })
public class DemographicAnalysis extends Analysis implements Serializable {

	private static final long serialVersionUID = -4420992994918486571L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@NotNull
	private AreaType areaType;
	
	@Column
	private TravelType travelType;
	
	@Column(columnDefinition = "TEXT")
	private String geojsonArea;
	
	@Column(name = "travel_time_or_distance")
	@Max(100000)
	@Min(1)
	Integer travelTimeOrDistance;

	@Column
	private Integer radius;

	@Column(nullable = false)
	@NotNull
	private Coordinate location;

	@Column
	private String locationDisplayName;

	@Column
	private Integer inhabitedPremises;

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

	@Lob
	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public String getLocationDisplayName() {
		return locationDisplayName;
	}

	public void setLocationDisplayName(String locationDisplayName) {
		this.locationDisplayName = locationDisplayName;
	}

	public AreaType getAreaType() {
		return areaType;
	}

	public void setAreaType(AreaType areaType) {
		this.areaType = areaType;
	}

	public TravelType getTravelType() {
		return travelType;
	}

	public void setTravelType(TravelType travelType) {
		this.travelType = travelType;
	}

	public String getGeojsonArea() {
		return geojsonArea;
	}

	public void setGeojsonArea(String geojsonArea) {
		this.geojsonArea = geojsonArea;
	}

	public Integer getTravelTimeOrDistance() {
		return travelTimeOrDistance;
	}

	public void setTravelTimeOrDistance(Integer travelTimeOrDistance) {
		this.travelTimeOrDistance = travelTimeOrDistance;
	}

	public Integer getInhabitedPremises() {
		return inhabitedPremises;
	}

	public void setInhabitedPremises(Integer inhabitedPremises) {
		this.inhabitedPremises = inhabitedPremises;
	}

}
