package pl.gisexpert.service;

import java.security.InvalidParameterException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import pl.gisexpert.cms.model.analysis.Analysis;
import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;
import pl.gisexpert.cms.model.analysis.demographic.SimpleDemographicAnalysis;

@ApplicationScoped
public class AnalysisCostCalculator {

	private final Double BASE_VALUE = 10.0;

	public Double calculate(Analysis analysis) {

		if (analysis instanceof SimpleDemographicAnalysis) {
			return calculateSimpleDemographic((SimpleDemographicAnalysis) analysis);
		} else if (analysis instanceof AdvancedDemographicAnalysis) {
			return calculateAdvancedDemographic((AdvancedDemographicAnalysis) analysis);
		}

		throw new InvalidParameterException();
	}

	private Double calculateSimpleDemographic(SimpleDemographicAnalysis analysis) {
		Integer radius = analysis.getRadius();
		if (radius <= 2000) {
			return BASE_VALUE;
		} else {
			return Math.round((radius.doubleValue() / 1000.0) / (Math.log(radius / 1000.0) / Math.log(2.0)))
					* BASE_VALUE;
		}
	}

	private Double calculateAdvancedDemographic(AdvancedDemographicAnalysis analysis) {

		Integer radius = analysis.getRadius();
		List<String> ageRanges = Lists.newArrayList(Splitter.on("-").split(analysis.getAgeRange()));
		Integer numAgeRanges = (int) Math
				.ceil((Integer.parseInt(ageRanges.get(1)) - Integer.parseInt(ageRanges.get(0))) / 5.0);
		if (radius <= 2000) {
			return numAgeRanges * BASE_VALUE;
		} else {
			return Math.round(((radius.doubleValue() / 1000.0) / (Math.log(radius / 1000.0) / Math.log(2.0)))
					* numAgeRanges.doubleValue()) * BASE_VALUE;
		}
	}
}
