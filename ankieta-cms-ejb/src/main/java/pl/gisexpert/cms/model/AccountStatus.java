package pl.gisexpert.cms.model;

public enum AccountStatus {
	UNCONFIRMED("UNCONFIRMED"), CONFIRMED("CONFIRMED"), DISABLED("DISABLED"), VERIFIED("VERIFIED");

	String name;

	AccountStatus(String name) {
		this.name=name;
	}
}
