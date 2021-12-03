package qiwi.util.enums;

public enum Language {
    ENGLISH, RUSSIAN, SPANISH, ALL;

    public String firstLetterToUpperCase() {
        return Character.toUpperCase(this.toString().charAt(0)) + this.toString().substring(1).toLowerCase();
    }

    public String toLowerCase() {
        return this.toString().toLowerCase();
    }
}
