package pl.gisexpert.rest.model.analysis;

import java.util.HashMap;

import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;

public class AdvancedDemographicAnalysisDetails extends DemographicAnalysisDetails {

	private HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges;
	private String ageRange;

	private Double przedprod;
	private Double prod;
	private Double poprod;

	public AdvancedDemographicAnalysisDetails(AdvancedDemographicAnalysis analysis) {
		super(analysis);
		this.kobietyAndMezczyzniByAgeRanges = analysis.getKobietyAndMezczyzniByAgeRanges();
		this.ageRange = analysis.getAgeRange();
		this.przedprod = analysis.getPrzedprod();
		this.poprod = analysis.getPoprod();
		this.prod = analysis.getProd();

	}

	public HashMap<String, HashMap<Integer, Integer>> getKobietyAndMezczyzniByAgeRanges() {
		return kobietyAndMezczyzniByAgeRanges;
	}

	public void setKobietyAndMezczyzniByAgeRanges(
			HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges) {
		this.kobietyAndMezczyzniByAgeRanges = kobietyAndMezczyzniByAgeRanges;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public Double getPrzedprod() {
		return przedprod;
	}

	public void setPrzedprod(Double przedprod) {
		this.przedprod = przedprod;
	}

	public Double getProd() {
		return prod;
	}

	public void setProd(Double prod) {
		this.prod = prod;
	}

	public Double getPoprod() {
		return poprod;
	}

	public void setPoprod(Double poprod) {
		this.poprod = poprod;
	}

}
