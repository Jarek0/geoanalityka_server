package pl.gisexpert.rest.model.analysis.demographic;

import pl.gisexpert.model.gis.Coordinate;
import pl.gisexpert.rest.model.BaseForm;

public class SumAllInRadiusForm extends BaseForm {
	private Coordinate point;
	private Integer radius;
	private String name;
	private String locationName;
	
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
	
	
}
