package pl.gisexpert.cms.model.analysis.demographic;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@DiscriminatorValue("advanced")
public class AdvancedDemographicAnalysis extends DemographicAnalysis {

	private static final long serialVersionUID = 6224558629498232285L;

	@Lob
	@Column(name = "analysis_data")
	Map<String, Map<Integer, Integer>> kobietyAndMezczyzniByAgeRanges;

	public Map<String, Map<Integer, Integer>> getKobietyAndMezczyzniByAgeRanges() {
		return kobietyAndMezczyzniByAgeRanges;
	}

	public void setKobietyAndMezczyzniByAgeRanges(Map<String, Map<Integer, Integer>> kobietyAndMezczyzniByAgeRanges) {
		this.kobietyAndMezczyzniByAgeRanges = kobietyAndMezczyzniByAgeRanges;
	}

}
