package pl.gisexpert.rest.model.analysis;

import java.util.Date;

import pl.gisexpert.cms.model.analysis.AnalysisStatus;
import pl.gisexpert.cms.model.analysis.AnalysisStatusCode;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;
import pl.gisexpert.model.gis.Coordinate;

public class DemographicAnalysisDetails {
	private Date dateStarted;
	private Date dateFinished;
	private String hash;
	private AnalysisStatus status;
	private AnalysisStatusCode statusCode;
	private String name;
	private Integer radius;
	private String travelType;
	private Integer travelTime;
	private String areaType;
	private Coordinate location;
	private String locationDisplayName;
	private String type;
	private Integer inhabitedPremises;
	private String geojsonArea;

	public DemographicAnalysisDetails(DemographicAnalysis analysis) {
		this.dateStarted = analysis.getDateStarted();
		this.dateFinished = analysis.getDateFinished();
		this.hash = analysis.getHash();
		this.status = analysis.getStatus();
		this.statusCode = analysis.getStatusCode();
		this.name = analysis.getName();
		this.radius = analysis.getRadius();
		this.location = analysis.getLocation();
		this.locationDisplayName = analysis.getLocationDisplayName();
		this.type = analysis.getClass().getSimpleName();
		this.inhabitedPremises = analysis.getInhabitedPremises();

		switch (analysis.getAreaType()) {
		case RADIUS:
			this.areaType = "radius";
			break;
		case TRAVEL_TIME:
			this.areaType = "travel-time";
			this.travelTime = analysis.getTravelTime();
			switch (analysis.getTravelType()) {
			case CAR:
				this.travelType = "car";
				break;
			case BICYCLE:
				this.travelType = "bicycle";
				break;
			case WALK:
				this.travelType = "walk";
				break;
			}
			break;
		}
		
		this.geojsonArea = analysis.getGeojsonArea();
		
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

	public Integer getInhabitedPremises() {
		return inhabitedPremises;
	}

	public void setInhabitedPremises(Integer inhabitedPremises) {
		this.inhabitedPremises = inhabitedPremises;
	}

	public String getTravelType() {
		return travelType;
	}

	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}

	public Integer getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(Integer travelTime) {
		this.travelTime = travelTime;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getGeojsonArea() {
		return geojsonArea;
	}

	public void setGeojsonArea(String geojsonArea) {
		this.geojsonArea = geojsonArea;
	}

	public AnalysisStatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(AnalysisStatusCode statusCode) {
		this.statusCode = statusCode;
	}
}
