package com.company;

import java.util.ArrayList;

public class DiscardPile {
    private final Card[] cards = new Card[10];
    private int numOfCards = 0;
    private final DrawPile drawPile;

    DiscardPile(DrawPile drawPile) {
        this.drawPile = drawPile;
        while (numOfCards == 0 || peek().color.equals("Wild"))
            pushAndReset(drawPile.drawCards(1)[0]);
    }

    public Card peek() {
        return cards[numOfCards-1];
    }

    /**
     * @param card the card to be compared
     * @return true if the card can be played
     */
    public boolean isMatch(Card card) {
        return card.color.equals("Wild") ||
                card.color.equals(peek().color) ||
                card.symbol.equals(peek().symbol);
    }

    /**
     * @param cards cards to be compared
     * @return true if any of the card could be played
     */
    public boolean isMatchPrev(ArrayList<Card> cards) {
        var currCard = pop();
        var result = false;
        for(var card : cards)
            if (!card.symbol.equals("+4") && isMatch(card)) {
                result = true;
                break;
            }
        push(currCard);
        return result;
    }

    /**
     * Pushes a card to the pile. If the pile is already full(max 10 cards), then resets the pile before pushing.
     * @param card card to be pushed
     */
    public void pushAndReset(Card card) {
        if (numOfCards == 10) reset();
        push(card);
    }

    private void reset() {
        Card topCard = pop();
        for (Card card : cards) {
            card.actedUpon = false;
            if (card.symbol.equals("") || card.symbol.equals("+4"))
                card.color = "Wild";
        }
        drawPile.putCards(cards);
        numOfCards = 0;
        push(topCard);
    }
    private Card pop(){ return cards[--numOfCards]; }
    private void push(Card card) { cards[numOfCards++] = card; }

}
