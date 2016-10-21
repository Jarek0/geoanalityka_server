package pl.gisexpert.cms.model.analysis.demographic;

import java.util.Date;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.analysis.AnalysisStatus;
import pl.gisexpert.cms.model.analysis.AreaType;
import pl.gisexpert.cms.model.analysis.TravelType;
import pl.gisexpert.model.gis.Coordinate;

public class DemographicAnalysisBuilder {
	
	private DemographicAnalysisType type;
	private AreaType areaType;
	private TravelType travelType;
	private Integer radius;
	private Integer travelTime;
	private String ageRange;
	private Account creator;
	private Coordinate location;
	private String locationDisplayName;
	private String name;
	
	public DemographicAnalysisBuilder(DemographicAnalysisType type) {
		this.type = type;
	}
	
	public static DemographicAnalysisBuilder advanced() {
		return new DemographicAnalysisBuilder(DemographicAnalysisType.ADVANCED);
	}
	
	public static DemographicAnalysisBuilder simple() {
		return new DemographicAnalysisBuilder(DemographicAnalysisType.SIMPLE);
	}
	
	public DemographicAnalysisBuilder travelTime() {
		return this;
	}
	
	public DemographicAnalysisBuilder areaType(AreaType areaType) {
		this.areaType = areaType;
		return this;
	}
	
	public DemographicAnalysisBuilder travelType(TravelType travelType) {
		this.travelType = travelType;
		return this;
	}
	
	public DemographicAnalysisBuilder radius(Integer radius) {
		this.radius = radius;
		return this;
	}
	
	public DemographicAnalysisBuilder travelTime(Integer travelTime) {
		this.travelTime = travelTime;
		return this;
	}
	
	public DemographicAnalysisBuilder ageRange(String ageRange) {
		this.ageRange = ageRange;
		return this;
	}
	
	public DemographicAnalysisBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public DemographicAnalysisBuilder creator(Account creator) {
		this.creator = creator;
		return this;
	}
	
	public DemographicAnalysisBuilder location(Coordinate location) {
		this.location = location;
		return this;
	}
	
	public DemographicAnalysisBuilder locationDisplayName(String locationDisplayName) {
		this.locationDisplayName = locationDisplayName;
		return this;
	}
	
	public DemographicAnalysis build() {
		DemographicAnalysis analysis;
		
		switch (this.type) {
		case ADVANCED:
			analysis = new AdvancedDemographicAnalysis();
			AdvancedDemographicAnalysis advancedAnalysis = ((AdvancedDemographicAnalysis) analysis);
			advancedAnalysis.setAgeRange(ageRange);
			break;
		case SIMPLE:
		default:
			analysis = new SimpleDemographicAnalysis();
		}
		
		analysis.setAreaType(areaType);
		
		switch (areaType) {
		case RADIUS:
			analysis.setRadius(radius);
			break;
		case TRAVEL_TIME:
			analysis.setTravelTime(travelTime);
			analysis.setTravelType(travelType);
			break;
		}
		
		analysis.setDateStarted(new Date());
		analysis.setStatus(AnalysisStatus.PENDING);
		
		analysis.setCreator(creator);
		analysis.setName(name);
		analysis.setLocation(location);
		analysis.setLocationDisplayName(locationDisplayName);
		
		return analysis;
	}

	
}
