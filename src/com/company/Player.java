package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
    final ArrayList<Card> hand = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);
    private final String name;
    private final DrawPile drawPile;
    private final DiscardPile discardPile;
    int score;
    boolean calledUno;

    public Player(String name, DrawPile drawPile, DiscardPile discardPile) {
        this.name = name;
        this.drawPile = drawPile;
        this.discardPile = discardPile;
        drawCards(7);
    }

    void drawCards(int num) {
        calledUno = false;
        hand.addAll(Arrays.asList(drawPile.drawCards(num)));
    }

    void playCard(int index) {
        Card card = hand.remove(index);
        if (card.color.equals("Wild")) {
            System.out.println("Pick a color:");
            String[] colors = {"Red", "Green", "Blue", "Yellow"};
            for (byte i = 1; i < 5; i++)
                System.out.println(i + ". " + colors[i - 1]);
            card.color = colors[sc.nextInt() - 1];
        }
        discardPile.pushAndReset(card);
    }

    /**
     * @return ArrayList of corresponding numbers to playable cards
     */
    ArrayList<Integer> showHand() {
        ArrayList<Integer> playable = new ArrayList<>();
        System.out.println(name + "'s hand:");
        for (int i = 1; i <= hand.size(); i++) {
            Card card = hand.get(i - 1);
            if (discardPile.isMatch(card)) {
                System.out.println(i + ". " + card);
                playable.add(i);
            } else System.out.println("   " + card);
        }
        return playable;
    }

    @Override
    public String toString() {
        return name;
    }
}
