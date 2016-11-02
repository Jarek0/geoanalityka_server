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
	
	@Column(name = "przedprodk")
	protected Double przedprodk;

	@Column(name = "prodk")
	protected Double prodk;

	@Column(name = "poprodk")
	protected Double poprodk;
	
	@Column(name = "przedprodm")
	protected Double przedprodm;

	@Column(name = "prodm")
	protected Double prodm;

	@Column(name = "poprodm")
	protected Double poprodm;

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

	public Double getPrzedprodk() {
		return przedprodk;
	}

	public void setPrzedprodk(Double przedprodk) {
		this.przedprodk = przedprodk;
	}

	public Double getProdk() {
		return prodk;
	}

	public void setProdk(Double prodk) {
		this.prodk = prodk;
	}

	public Double getPoprodk() {
		return poprodk;
	}

	public void setPoprodk(Double poprodk) {
		this.poprodk = poprodk;
	}

	public Double getPrzedprodm() {
		return przedprodm;
	}

	public void setPrzedprodm(Double przedprodm) {
		this.przedprodm = przedprodm;
	}

	public Double getProdm() {
		return prodm;
	}

	public void setProdm(Double prodm) {
		this.prodm = prodm;
	}

	public Double getPoprodm() {
		return poprodm;
	}

	public void setPoprodm(Double poprodm) {
		this.poprodm = poprodm;
	}

}
