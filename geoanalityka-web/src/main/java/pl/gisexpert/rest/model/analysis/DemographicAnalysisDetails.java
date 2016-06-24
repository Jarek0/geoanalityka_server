package pl.gisexpert.rest.model.analysis;

import java.util.Date;

import pl.gisexpert.cms.model.analysis.AnalysisStatus;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;
import pl.gisexpert.model.gis.Coordinate;

public class DemographicAnalysisDetails {
	private Date dateStarted;
	private Date dateFinished;
	private String hash;
	private AnalysisStatus status;
	private String name;
	private Integer radius;
	private Coordinate location;
	private String locationDisplayName;
	private String type;

	public DemographicAnalysisDetails(DemographicAnalysis analysis) {
		this.dateStarted = analysis.getDateStarted();
		this.dateFinished = analysis.getDateFinished();
		this.hash = analysis.getHash();
		this.status = analysis.getStatus();
		this.name = analysis.getName();
		this.radius = analysis.getRadius();
		this.location = analysis.getLocation();
		this.locationDisplayName = analysis.getLocationDisplayName();
		this.type = analysis.getClass().getSimpleName();
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public AnalysisStatus getStatus() {
		return status;
	}

	public void setStatus(AnalysisStatus status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
