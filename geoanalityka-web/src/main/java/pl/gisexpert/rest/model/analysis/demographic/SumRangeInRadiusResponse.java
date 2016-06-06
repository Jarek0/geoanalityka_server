package pl.gisexpert.rest.model.analysis.demographic;

import java.util.Map;

import pl.gisexpert.rest.model.BaseResponse;

public class SumRangeInRadiusResponse extends BaseResponse {
	
	private Map<Integer, Integer> kobiety;
	private Map<Integer, Integer> mezczyzni;
	
	public Map<Integer, Integer> getKobiety() {
		return kobiety;
	}
	public void setKobiety(Map<Integer, Integer> kobiety) {
		this.kobiety = kobiety;
	}
	public Map<Integer, Integer> getMezczyzni() {
		return mezczyzni;
	}
	public void setMezczyzni(Map<Integer, Integer> mezczyzni) {
		this.mezczyzni = mezczyzni;
	}
	


}
