package it.homeworkheroes.pollbot.classes;

import java.util.Arrays;
import java.util.Locale;

public enum EMONUMBER {
    ZERO(/*0,*/ "0️⃣"), ONE(/*1,*/ "1️⃣"), TWO(/*2,*/ "2️⃣"), THREE(/*3,*/"3️⃣"), FOUR(/*4,*/ "4️⃣"),
    FIVE(/*5,*/ "5️⃣"), SIX(/*6,*/ "6️⃣"), SEVEN(/*7,*/ "7️⃣"), EIGHT(/*8,*/ "8️⃣"), NINE(/*9,*/ "9️⃣");

    // private int value;
    private String emoji;

    EMONUMBER(/*int value,*/ String emoji){
        //this.value = value;
        this.emoji = emoji;
    }

    public static int getValue(String emoji){
        /*si potrebbe parallelizzare ma avrebbe un costo maggiore rispetto a cercare linearmente in
        10 stupidi elementi... tantomeno a filtrare con uno stream!*/
        var e = EMONUMBER.values();
        for(int i = 0; i < e.length; i++){
            if(e[i].getEmoji().equals(emoji)){
                return i;
            }
        }
        return -1;
    }

//     public int getValue(){
//        return this.value;
//    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public String toString() {
        return ":" + super.toString().toLowerCase(Locale.ROOT) + ":";
    }
}
