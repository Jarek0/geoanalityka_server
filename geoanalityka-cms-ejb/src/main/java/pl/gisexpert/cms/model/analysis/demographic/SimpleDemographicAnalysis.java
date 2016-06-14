package pl.gisexpert.cms.model.analysis.demographic;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("simple")
public class SimpleDemographicAnalysis extends DemographicAnalysis {

	private static final long serialVersionUID = 6224558629498232285L;

	@Column(name = "total_population")
	private Integer population;

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

}
