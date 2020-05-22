package com.company;

import java.util.Arrays;
import java.util.Random;

class DrawPile {
    private final static Random rand = new Random();
    private final Card[] cards = new Card[108];
    byte numOfCards = 0;

    DrawPile() {
        for (var color : Arrays.asList("Red", "Green", "Blue", "Yellow")) {
            for (byte symbol = 0; symbol < 10; symbol++)
                putCard(new Card(color, Byte.toString(symbol)));
            for (byte symbol = 1; symbol < 10; symbol++)
                putCard(new Card(color, Byte.toString(symbol)));
            for (var symbol : Arrays.asList("Reverse", "Skip", "+2"))
                for (byte i = 0; i < 2; i++)
                    putCard(new Card(color, symbol));
        }
        for (var symbol : Arrays.asList("+4", ""))
            for (byte i = 0; i < 4; i++)
                putCard(new Card("Wild", symbol));
        shuffle();
    }

    void shuffle() {
        for (byte index1 = 0; index1 < numOfCards; index1++) {
            byte index2 = (byte) rand.nextInt(numOfCards);
            Card card1 = cards[index1];
            cards[index1] = cards[index2];
            cards[index2] = card1;
        }
    }

    Card[] drawCards(int num) {
        var drawnCards = new Card[num];
        for (byte i = 0; i < num; i++)
            drawnCards[i] = cards[--numOfCards];
        return drawnCards;
    }

    void putCards(Card[] cards) {
        for (var card : cards)
            putCard(card);
        shuffle();
    }

    private void putCard(Card card) {
        cards[numOfCards++] = card;
    }

}