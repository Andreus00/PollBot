package it.homeworkheroes.pollbot.classes;

import java.util.Locale;

/**
 * Enum that maps a number to the associated emoticons.
 */
public enum EMONUMBER {
    ZERO(/*0,*/ "0️⃣"), ONE(/*1,*/ "1️⃣"), TWO(/*2,*/ "2️⃣"), THREE(/*3,*/"3️⃣"), FOUR(/*4,*/ "4️⃣"),
    FIVE(/*5,*/ "5️⃣"), SIX(/*6,*/ "6️⃣"), SEVEN(/*7,*/ "7️⃣"), EIGHT(/*8,*/ "8️⃣"), NINE(/*9,*/ "9️⃣");

    private String emoji;

    EMONUMBER(String emoji){
        this.emoji = emoji;
    }

    /**
     * This returns the int value of the emoji passed as input
     * @param emoji the emoji to convert
     * @return the EMONUMBER associated
     */
    public static int getValue(String emoji){
        var e = EMONUMBER.values();
        for(int i = 0; i < e.length; i++){
            if(e[i].getEmoji().equals(emoji)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Getter of the emoji
     * @return the emoji field
     */
    public String getEmoji() {
        return emoji;
    }

    @Override
    public String toString() {
        return ":" + super.toString().toLowerCase(Locale.ROOT) + ":";
    }
}
