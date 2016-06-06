package pl.gisexpert.model.gis;

public class Coordinate {
	
	private Double x;
	private Double y;
	private Integer epsgCode = 4326;
	
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public Integer getEpsgCode() {
		return epsgCode;
	}
	public void setEpsgCode(Integer epsgCode) {
		this.epsgCode = epsgCode;
	}
	
	
}
