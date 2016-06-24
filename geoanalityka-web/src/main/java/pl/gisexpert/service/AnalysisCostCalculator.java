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
		if (radius <= 1000) {
			return 1.0;
		} else {
			return Math.ceil((radius.doubleValue() / 10000.0) * (Math.log(radius) / Math.log(10)));
		}
	}

	private Double calculateAdvancedDemographic(AdvancedDemographicAnalysis analysis) {

		Integer radius = analysis.getRadius();
		List<String> ageRanges = Lists.newArrayList(Splitter.on("-").split(analysis.getAgeRange()));
		Integer numAgeRanges = (int) Math
				.ceil((Integer.parseInt(ageRanges.get(1)) - Integer.parseInt(ageRanges.get(0))) / 5.0);
		if (radius <= 1000) {
			return numAgeRanges.doubleValue();
		} else {
			return Math.ceil(numAgeRanges * (radius.doubleValue() / 10000.0) * (Math.log(radius) / Math.log(10)));
		}
	}
}
