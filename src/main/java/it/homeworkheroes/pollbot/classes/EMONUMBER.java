package it.homeworkheroes.pollbot.classes;

import java.util.Locale;

public enum EMONUMBER {
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

    @Override
    public String toString() {
        return ":" + super.toString().toLowerCase(Locale.ROOT) + ":";
    }
}
