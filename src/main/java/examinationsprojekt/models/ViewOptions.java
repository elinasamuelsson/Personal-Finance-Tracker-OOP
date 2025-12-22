package examinationsprojekt.models;

public enum ViewOptions {
    YEARLY(1, "Yearly"),
    MONTHLY(2, "Monthly"),
    WEEKLY(3, "Weekly"),
    DAILY(4, "Daily"),
    CATEGORY(5, "By category");

    private final int index;
    private final String description;

    ViewOptions(int index, String description) {
        this.index = index;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }
}
