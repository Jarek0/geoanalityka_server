package pl.gisexpert.rest.model.analysis.demographic;

import pl.gisexpert.cms.model.analysis.AreaType;
import pl.gisexpert.cms.model.analysis.TravelType;
import pl.gisexpert.model.gis.Coordinate;
import pl.gisexpert.rest.model.BaseForm;

public class AdvancedAnalysisForm extends BaseForm {

	private Coordinate point;
	private Integer radius;
	private TravelType travelType;
	private Integer travelTimeOrDistance;
	private Integer[] range;
	private String name;
	private String locationName;
	private AreaType areaType;

	public Coordinate getPoint() {
		return point;
	}

	public void setPoint(Coordinate point) {
		this.point = point;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer[] getRange() {
		return range;
	}

	public void setRange(Integer[] range) {
		this.range = range;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public AreaType getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		switch (areaType) {
		case "radius":
			this.areaType = AreaType.RADIUS;
			break;
		case "travel-time":
			this.areaType = AreaType.TRAVEL_TIME;
			break;
		case "travel-distance":
			this.areaType = AreaType.TRAVEL_DISTANCE;
			break;
		}
	}

	public TravelType getTravelType() {
		return travelType;
	}

	public void setTravelType(String travelType) {
		switch (travelType) {
		case "car":
			this.travelType = TravelType.CAR;
			break;
		case "bicycle":
			this.travelType = TravelType.BICYCLE;
			break;
		case "walk":
			this.travelType = TravelType.WALK;
			break;
		}
	}

	public Integer getTravelTimeOrDistance() {
		return travelTimeOrDistance;
	}

	public void setTravelTimeOrDistance(Integer travelTimeOrDistance) {
		this.travelTimeOrDistance = travelTimeOrDistance;
	}
	
	

}
