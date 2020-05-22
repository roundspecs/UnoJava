package com.company;

import java.util.Arrays;

public class Card {
    final String symbol;
    String color;
    /**
     * true if an action card or wild card is already used
     */
    boolean actedUpon = false;

    public Card(String color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    /**
     * Number cards count their face value, all action cards count 20, and Wild and Wild Draw Four cards count 50.
     *
     * @return points of the card
     */
    public byte faceValue() {
        if (Arrays.asList("+2", "Reverse", "Skip").contains(symbol)) return 20;
        else if (color.equals("Wild")) return 40;
        else return Byte.parseByte(symbol);
    }

    @Override
    public String toString() {
        return color + " " + symbol;
    }
}
