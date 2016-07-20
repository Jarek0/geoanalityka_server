package pl.gisexpert.cms.model;

public enum PremiumPlanType {

	PLAN_TESTOWY ("PLAN_TESTOWY"),
	PLAN_STANDARDOWY ("PLAN_STANDARDOWY"),
	PLAN_ZAAWANSOWANY ("PLAN_ZAAWANSOWANY"),
	PLAN_DEDYKOWANY ("PLAN_DEDYKOWANY");

	private final String name;

	private PremiumPlanType(String s) {
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
