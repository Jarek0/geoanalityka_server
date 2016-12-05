package pl.gisexpert.cms.model.analysis.demographic;

/**
 * Created by pkociuba on 2016-09-20.
 */
public class PeopleByWorkingAgeSums {

    private Double poprod;
    private Double prod;
    private Double przedprod;
    
    private Double poprodk;
    private Double prodk;
    private Double przedprodk;
    private Double poprodm;
    private Double prodm;
    private Double przedprodm;

    public Double getPoprod() {
        return poprod;
    }

    public void setPoprod(Double poprod) {
        this.poprod = poprod;
    }

    public Double getProd() {
        return prod;
    }

    public void setProd(Double prod) {
        this.prod = prod;
    }

    public Double getPrzedprod() {
        return przedprod;
    }

    public void setPrzedprod(Double przedprod) {
        this.przedprod = przedprod;
    }

    public Double getPoprodk() {
		return poprodk;
	}

	public void setPoprodk(Double poprodk) {
		this.poprodk = poprodk;
	}

	public Double getProdk() {
		return prodk;
	}

	public void setProdk(Double prodk) {
		this.prodk = prodk;
	}

	public Double getPrzedprodk() {
		return przedprodk;
	}

	public void setPrzedprodk(Double przedprodk) {
		this.przedprodk = przedprodk;
	}

	public Double getPoprodm() {
		return poprodm;
	}

	public void setPoprodm(Double poprodm) {
		this.poprodm = poprodm;
	}

	public Double getProdm() {
		return prodm;
	}

	public void setProdm(Double prodm) {
		this.prodm = prodm;
	}

	public Double getPrzedprodm() {
		return przedprodm;
	}

	public void setPrzedprodm(Double przedprodm) {
		this.przedprodm = przedprodm;
	}
	
	public PeopleByWorkingAgeSums(
			Double przedprod, Double prod, Double poprod,
			Double przedprodk, Double prodk, Double poprodk,
			Double przedprodm, Double prodm, Double poprodm) {
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
