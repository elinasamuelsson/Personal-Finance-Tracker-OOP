package examinationsprojekt.models;

public enum TransactionTypes {
    //earning
    SALARY("Salary"),
    GIFT("Gift"),
    INTEREST("Interest"),
    OTHER_EARNING("Miscellaneous income"),

    //spending
    HOUSING("Housing"),
    UTILITIES("Utilities"),
    FOOD("Food"),
    HEALTH("Health"),
    TRANSPORT("Transport"),
    EDUCATION("Education"),
    SHOPPING("Shopping"),
    ENTERTAINMENT("Entertainment"),
    DONATIONS("Donations"),
    SAVINGS("Savings"),
    OTHER_SPENDING("Miscellaneous spending"),
    ;

    private final String typeDescription;

    TransactionTypes(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getTypeDescription() {
        return typeDescription;
    }
}
