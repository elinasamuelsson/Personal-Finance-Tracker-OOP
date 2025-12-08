package examinationsprojekt.models;

public enum AccountTypes {
    CHECKING("Checking account"),
    SAVING("Savings account");

    private final String typeDescription;

    AccountTypes(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getTypeDescription() {
        return typeDescription;
    }
}
