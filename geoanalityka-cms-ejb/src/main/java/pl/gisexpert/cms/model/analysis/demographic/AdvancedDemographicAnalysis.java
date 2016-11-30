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

    @Column(name = "age_range", length = 7)
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

    @Column(name = "population_2015")
    protected Integer population_2015;

    @Column(name = "population_2010")
    protected Integer population_2010;

    @Column(name = "population_2005")
    protected Integer population_2005;

    @Column(name = "population_2000")
    protected Integer population_2000;

    @Column(name = "population_1995")
    protected Integer population_1995;


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

    public void setPopulation(Integer population, Integer year){
        switch (year){
            case 2015:
                setPopulation_2015(population);
                break;
            case 2010:
                setPopulation_2010(population);
                break;
            case 2005:
                setPopulation_2005(population);
                break;
            case 2000:
                setPopulation_2000(population);
                break;
            case 1995:
                setPopulation_1995(population);
                break;
        }
    }
}
