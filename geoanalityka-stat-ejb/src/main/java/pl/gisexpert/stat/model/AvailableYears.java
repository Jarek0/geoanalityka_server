package pl.gisexpert.stat.model;

/**
 * Created by pkociuba on 2016-11-08.
 */
public enum AvailableYears {
    Y_1995 ("dane1995"),
    Y_2000 ("dane2000"),
    Y_2005 ("dane2005"),
    Y_2010 ("dane2010"),
    CURRENT_YEAR ("dane2015");

    private final String name;

    private AvailableYears(String s) {
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
