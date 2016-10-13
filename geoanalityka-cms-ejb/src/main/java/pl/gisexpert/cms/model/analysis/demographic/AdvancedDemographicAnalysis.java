package pl.gisexpert.cms.model.analysis.demographic;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@DiscriminatorValue("advanced")
public class AdvancedDemographicAnalysis extends DemographicAnalysis {

	private static final long serialVersionUID = 6224558629498232285L;

	@Column(name = "analysis_data")
	private HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges;
	
	@Column(name="age_range", length = 7)
	private String ageRange;

	@Column(name = "przedprod")
	protected Double przedprod;

	@Column(name = "prod")
	protected Double prod;



	@Column(name = "poprod")
	protected Double poprod;

	public HashMap<String, HashMap<Integer, Integer>> getKobietyAndMezczyzniByAgeRanges() {
		return kobietyAndMezczyzniByAgeRanges;
	}

	@Lob
	public void setKobietyAndMezczyzniByAgeRanges(HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges) {
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
