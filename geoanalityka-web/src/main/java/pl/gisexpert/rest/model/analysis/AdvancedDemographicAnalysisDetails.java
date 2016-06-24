package pl.gisexpert.rest.model.analysis;

import java.util.HashMap;

import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;

public class AdvancedDemographicAnalysisDetails extends DemographicAnalysisDetails {

	private HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges;
	private String ageRange;

	public AdvancedDemographicAnalysisDetails(AdvancedDemographicAnalysis analysis) {
		super(analysis);
		this.kobietyAndMezczyzniByAgeRanges = analysis.getKobietyAndMezczyzniByAgeRanges();
		this.ageRange = analysis.getAgeRange();

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

}
