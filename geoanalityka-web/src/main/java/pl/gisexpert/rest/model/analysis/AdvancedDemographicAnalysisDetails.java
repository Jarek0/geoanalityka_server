package pl.gisexpert.rest.model.analysis;

import java.util.HashMap;

import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;

public class AdvancedDemographicAnalysisDetails extends DemographicAnalysisDetails {

	private HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges;
	private String ageRange;

	private Double przedprod;
	private Double prod;
	private Double poprod;
	
	private Double przedprodk;
	private Double prodk;
	private Double poprodk;
	private Double przedprodm;
	private Double prodm;
	private Double poprodm;

	private Integer population_2015;
	private Integer population_2010;
	private Integer population_2005;
	private Integer population_2000;
	private Integer population_1995;

	public AdvancedDemographicAnalysisDetails(AdvancedDemographicAnalysis analysis) {
		super(analysis);
		this.kobietyAndMezczyzniByAgeRanges = analysis.getKobietyAndMezczyzniByAgeRanges();
		this.ageRange = analysis.getAgeRange();
		this.przedprod = analysis.getPrzedprod();
		this.poprod = analysis.getPoprod();
		this.prod = analysis.getProd();
		this.przedprodk = analysis.getPrzedprodk();
		this.poprodk = analysis.getPoprodk();
		this.prodk = analysis.getProdk();
		this.przedprodm = analysis.getPrzedprodm();
		this.poprodm = analysis.getPoprodm();
		this.prodm = analysis.getProdm();
		this.population_2015 = analysis.getPopulation_2015();
		this.population_2010 = analysis.getPopulation_2010();
		this.population_2005 = analysis.getPopulation_2005();
		this.population_2000 = analysis.getPopulation_2000();
		this.population_1995 = analysis.getPopulation_1995();

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

	public Integer getPopulation_2015() {
		return population_2015;
	}

	public void setPopulation_2015(Integer population_2015) {
		this.population_2015 = population_2015;
	}

	public Integer getPopulation_2010() {
		return population_2010;
	}

	public void setPopulation_2010(Integer population_2010) {
		this.population_2010 = population_2010;
	}

	public Integer getPopulation_2005() {
		return population_2005;
	}

	public void setPopulation_2005(Integer population_2005) {
		this.population_2005 = population_2005;
	}

	public Integer getPopulation_2000() {
		return population_2000;
	}

	public void setPopulation_2000(Integer population_2000) {
		this.population_2000 = population_2000;
	}

	public Integer getPopulation_1995() {
		return population_1995;
	}

	public void setPopulation_1995(Integer population_1995) {
		this.population_1995 = population_1995;
	}
}
