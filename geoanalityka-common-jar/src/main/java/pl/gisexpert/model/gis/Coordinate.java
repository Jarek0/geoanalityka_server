package pl.gisexpert.model.gis;

import java.io.Serializable;

public class Coordinate implements Serializable {

	private static final long serialVersionUID = -5138413995590331020L;
	
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
