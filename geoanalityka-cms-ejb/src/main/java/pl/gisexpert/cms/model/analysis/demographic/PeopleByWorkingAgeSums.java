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

    public PeopleByWorkingAgeSums(double przedprod, double prod, double poprod) {
        this.poprod = poprod;
        this.prod = prod;
        this.przedprod = przedprod;
    }
}
