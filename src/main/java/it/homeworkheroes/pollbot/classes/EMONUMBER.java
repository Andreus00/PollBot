package it.homeworkheroes.pollbot.classes;

import java.util.Locale;

public enum EMONUMBER {
    ZERO(0, ""), ONE(1, ""), TWO(2, ""), THREE(3, ""), FOUR(4, ""),
    FIVE(5, ""), SIX(6, ""), SEVEN(7, ""), EIGHT(8, ""), NINE(9, "9️⃣");

    private int value;
    private String emoji;

    EMONUMBER(int value, String emoji){
        this.value = value;
    }

    @Override
    public String toString() {
        return ":" + super.toString().toLowerCase(Locale.ROOT) + ":";
    }

    public int getValue(){
        return this.value;
    }

    public String getEmoji() {
        return emoji;
    }
}
