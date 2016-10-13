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
	protected double przedprod;

	@Column(name = "prod")
	protected double prod;



	@Column(name = "poprod")
	protected double poprod;

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
