package pl.gisexpert.cms.model.analysis;

public enum TravelType {

	CAR("C"), BICYCLE("B"), WALK("W");

	private final String name;

	private TravelType(String s) {
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