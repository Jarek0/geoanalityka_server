package pl.gisexpert.cms.model.analysis;

public enum AreaType {

	RADIUS("R"), TRAVEL_TIME("T"), TRAVEL_DISTANCE("D");

	private final String name;

	private AreaType(String s) {
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