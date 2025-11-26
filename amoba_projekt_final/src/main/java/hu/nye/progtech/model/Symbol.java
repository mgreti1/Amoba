package hu.nye.progtech.model;

public enum Symbol {
    X('X'), O('O'), EMPTY('.');

    private final char sign;

    Symbol(char sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return String.valueOf(sign);
    }
}