package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Random random = new Random();
    private static final Scanner sc = new Scanner(System.in);
    private static byte numOfPlayers = 0;
    private static Player[] players;
    private static Player player;
    private static Card topCard;
    private static byte direction = 1;
    private static DrawPile drawPile;
    private static DiscardPile discardPile;

    public static void main(String[] args) throws IOException, InterruptedException {
        drawPile = new DrawPile();
        discardPile = new DiscardPile(drawPile);

        // if first top card is a reverse card
        if (discardPile.peek().symbol.equals("Reverse")) {
            direction = -1;
            discardPile.peek().actedUpon = true;
        }

        // ask for player names and create them
        initPlayers();

        for (byte playerIndex = (byte) random.nextInt(numOfPlayers); true; playerIndex = next(playerIndex)) {
            player = players[playerIndex];
            topCard = discardPile.peek();

            promptMessage(player + "'s turn.");

            // check if player forgot to say uno
            if (player.hand.size() == 1 && !player.calledUno) {
                player.drawCards(2);
                promptMessage("You forgot to say uno.");
            }

            // acting upon the top card
            if (!topCard.actedUpon) {
                topCard.actedUpon = true;
                if (actUpon(player, topCard.symbol, playerIndex)) continue;
            }

            // printing out player name and top card
            header();

            // printing out available options
            ArrayList<String> options = getOptions(topCard, player);
            System.out.print("Options: ");
            for (var option : options)
                System.out.print(option + " ");
            System.out.println();

            // printing out the cards in player's hand
            var playable = player.showHand();

            // promting the player to play a card or choose an option
            var ans = sc.nextLine().toLowerCase();
            if (isInt(ans) && playable.contains(Integer.parseInt(ans))) {
                player.playCard(Integer.parseInt(ans) - 1);
                if (player.hand.isEmpty()) {
                    clearScreen();
                    System.out.println(player + " won!");
                    for (Player p : players)
                        if (p != player)
                            for (Card card : p.hand)
                                player.score += card.faceValue();
                    System.out.println("Score: " + player.score);
                    break;
                }
            } else if (options.contains(ans)) {
                switch (ans) {
                    case "draw" -> {
                        var calledUno = player.calledUno;
                        player.drawCards(1);
                        var card = player.hand.get(player.hand.size() - 1);
                        if (discardPile.isMatch(card)) {
                            System.out.println("Play " + card + "? (y/n)");
                            if (sc.nextLine().equals("y")) {
                                player.playCard((player.hand.size() - 1));
                                if (player.hand.size() == 1)
                                    player.calledUno = calledUno;
                            }
                        }
                    }
                    case "uno" -> {
                        player.calledUno = true;
                        playerIndex = prev(playerIndex);
                    }
                }
            } else playerIndex = prev(playerIndex);
        }
    }

    private static boolean actUpon(Player player, String symbol, byte playerIndex) throws IOException, InterruptedException {
        switch (symbol) {
            case "+2" -> {
                promptMessage("You have drawn 2 cards because the board card was a +2");
                player.drawCards((byte) 2);
            }
            case "Skip" -> promptMessage("You have been skipped :(");
            case "Reverse" -> {
                promptMessage("That mofo played a reverse! -_-");
                direction *= -1;
            }
            case "+4" -> {
                System.out.println("Challenge? (y/n)");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    Player prevPlayer = players[prev(playerIndex)];
                    if (discardPile.isMatchPrev(prevPlayer.hand)) {
                        prevPlayer.drawCards((byte) 4);
                        promptMessage("Challenge successful. Previous player picked 4 cards");
                        return false;
                    } else {
                        player.drawCards((byte) 6);
                        promptMessage("Challenge failed. You have drawn 6 cards :(");
                    }
                } else {
                    player.drawCards((byte) 4);
                    promptMessage("You have drawn 4 cards because the board card was a +4");
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void header() throws IOException, InterruptedException {
        clearScreen();
        System.out.println("Player name: " + player + "\nTop Card: " + topCard);
    }

    private static void promptMessage(String str) throws IOException, InterruptedException {
        header();
        System.out.println(str);
        System.out.println("Press enter to continue...");
        sc.nextLine();
    }

    private static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static ArrayList<String> getOptions(Card topCard, Player player) {
        ArrayList<String> options = new ArrayList<>();
        options.add("draw");
        if (topCard.symbol.equals("Wild +4") && !topCard.actedUpon) {
            options.add("challenge");
        }
        if (player.hand.size() == 2 && !player.calledUno) {
            for (Card card : player.hand)
                if (discardPile.isMatch(card)) {
                    options.add("uno");
                    break;
                }
        }
        return options;
    }

    private static byte prev(byte playerIndex) {
        return (byte) ((playerIndex + numOfPlayers - direction) % numOfPlayers);
    }

    private static byte next(byte playerIndex) {
        return (byte) ((playerIndex + numOfPlayers + direction) % numOfPlayers);
    }

    private static void initPlayers() throws IOException, InterruptedException {
        clearScreen();
        while (numOfPlayers < 2) {
            System.out.print("Enter the number of players: ");
            numOfPlayers = sc.nextByte();
            clearScreen();
            if (numOfPlayers < 2)
                System.out.println("At least two players required.");
        }
        players = new Player[numOfPlayers];
        System.out.println("Enter the name of the players:");
        for (byte i = 1; i <= numOfPlayers; i++) {
            System.out.print(i + ". ");
            players[i - 1] = new Player(sc.next(), drawPile, discardPile);
        }
    }

    private static void clearScreen() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}
