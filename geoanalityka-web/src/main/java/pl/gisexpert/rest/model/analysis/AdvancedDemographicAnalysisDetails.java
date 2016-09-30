package pl.gisexpert.rest.model.analysis;

import java.util.HashMap;

import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;

public class AdvancedDemographicAnalysisDetails extends DemographicAnalysisDetails {

	private HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges;
	private String ageRange;

	private double przedprod;


	private double prod;

	private double poprod;

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

	public double getPrzedprod() {
		return przedprod;
	}

	public void setPrzedprod(double przedprod) {
		this.przedprod = przedprod;
	}

	public double getProd() {
		return prod;
	}

	public void setProd(double prod) {
		this.prod = prod;
	}

	public double getPoprod() {
		return poprod;
	}

	public void setPoprod(double poprod) {
		this.poprod = poprod;
	}

}
