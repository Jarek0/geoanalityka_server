package pl.gisexpert.cms.model.analysis.demographic;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by pkociuba on 2016-09-20.
 */
public class PeopleByWorkingAgeSums {

    private double poprod;
    private double prod;
    private double przedprod;
    
    private double poprodk;
    private double prodk;
    private double przedprodk;
    private double poprodm;
    private double prodm;
    private double przedprodm;

    public double getPoprod() {
        return poprod;
    }

    public void setPoprod(double poprod) {
        this.poprod = poprod;
    }

    public double getProd() {
        return prod;
    }

    public void setProd(double prod) {
        this.prod = prod;
    }

    public double getPrzedprod() {
        return przedprod;
    }

    public void setPrzedprod(double przedprod) {
        this.przedprod = przedprod;
    }

    public double getPoprodk() {
		return poprodk;
	}

	public void setPoprodk(double poprodk) {
		this.poprodk = poprodk;
	}

	public double getProdk() {
		return prodk;
	}

	public void setProdk(double prodk) {
		this.prodk = prodk;
	}

	public double getPrzedprodk() {
		return przedprodk;
	}

	public void setPrzedprodk(double przedprodk) {
		this.przedprodk = przedprodk;
	}

	public double getPoprodm() {
		return poprodm;
	}

	public void setPoprodm(double poprodm) {
		this.poprodm = poprodm;
	}

	public double getProdm() {
		return prodm;
	}

	public void setProdm(double prodm) {
		this.prodm = prodm;
	}

	public double getPrzedprodm() {
		return przedprodm;
	}

	public void setPrzedprodm(double przedprodm) {
		this.przedprodm = przedprodm;
	}
	
	public PeopleByWorkingAgeSums(
			double przedprod, double prod, double poprod,
			double przedprodk, double prodk, double poprodk,
			double przedprodm, double prodm, double poprodm) {
		this.poprod = poprod;
        this.prod = prod;
        this.przedprod = przedprod;
        this.poprodk = poprodk;
        this.prodk = prodk;
        this.przedprodk = przedprodk;
        this.poprodm = poprodm;
        this.prodm = prodm;
        this.przedprodm = przedprodm;
    }
	
	public PeopleByWorkingAgeSums() {
    }
}
