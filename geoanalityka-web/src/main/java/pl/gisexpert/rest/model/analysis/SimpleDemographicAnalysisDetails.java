package pl.gisexpert.rest.model.analysis;

import pl.gisexpert.cms.model.analysis.demographic.SimpleDemographicAnalysis;

public class SimpleDemographicAnalysisDetails extends DemographicAnalysisDetails {

	private Integer population;

	public SimpleDemographicAnalysisDetails(SimpleDemographicAnalysis analysis) {
		super(analysis);
		this.population = analysis.getPopulation();
	
	}

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}
	
	


}
