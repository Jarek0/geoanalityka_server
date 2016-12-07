package pl.gisexpert.cms.model;

public enum OrderType {
	
	STANDARD_PLAN_ACTIVATION ("STANDARD_PLAN_ACTIVATION"),
	ADVANCED_PLAN_ACTIVATION ("ADVANCED_PLAN_ACTIVATION"),
	ADD_CREDIT ("ADD_CREDIT"),
	DEDICATED_PLAN_ACTIVATION ("DEDICATED_PLAN_ACTIVATION"),
	NONE ("NONE");

	private final String name;

	private OrderType(String s) {
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
