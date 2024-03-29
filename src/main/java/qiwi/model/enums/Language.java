package qiwi.model.enums;

public enum Language {
    ENGLISH, RUSSIAN, SPANISH;

    public String firstLetterToUpperCase() {
        return Character.toUpperCase(this.toString().charAt(0)) + this.toString().substring(1).toLowerCase();
    }

    public String toLowerCase() {
        return this.toString().toLowerCase();
    }
}
