package pl.gisexpert.cms.model.analysis.demographic;

public enum DemographicAnalysisType {

	SIMPLE("simple"), ADVANCED("advanced");

	private final String name;

	private DemographicAnalysisType(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : name.equals(otherName);
	}

	@Override
	public String toString() {
		return this.name;
	}
}