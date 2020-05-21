package com.company;

import java.util.Arrays;

public class Card {
    String color;
    String symbol;

    public Card(String color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    public int score() {
        if (Arrays.asList("+2", "Reverse", "Skip").contains(symbol)) return 20;
        else if (color.equals("Wild")) return 40;
        else return Integer.parseInt(symbol);
    }
}