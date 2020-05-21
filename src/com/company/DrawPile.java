package com.company;

import java.util.Arrays;
import java.util.Random;

class DrawPile {
    private final static Random rand = new Random();
    private final static Card[] cards = new Card[108];
    private static byte numOfCards = 0;
    static {
        for (var color : Arrays.asList("Red", "Green", "Blue", "Yellow")) {
            for (Byte symbol = 0; symbol < 10; symbol++)
                cards[numOfCards++] = new Card(color, symbol.toString());
            for (Byte symbol = 1; symbol < 10; symbol++)
                cards[numOfCards++] = new Card(color, symbol.toString());
            for (var symbol : Arrays.asList("Reverse", "Skip", "+2")) {
                cards[numOfCards++] = new Card(color, symbol);
                cards[numOfCards++] = new Card(color, symbol);
            }
        }
        for (var symbol : Arrays.asList("+4", ""))
            for (byte i = 0; i < 4; i++)
                cards[numOfCards++] = new Card("Wild", symbol);
        shuffle();
    }

    static void shuffle() {
        for (byte index1 = 0; index1 < numOfCards; index1++) {
            byte index2 = (byte) rand.nextInt(numOfCards);
            Card card1 = cards[index1];
            cards[index1] = cards[index2];
            cards[index2] = card1;
        }
    }

}