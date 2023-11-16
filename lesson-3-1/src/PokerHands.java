import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PokerHands {
    public static int player1Wins(File file) {
        Hand[] player1 = new Hand[1000];
        Hand[] player2 = new Hand[1000];

        try {
            Scanner reader = new Scanner(file);

            for (int i = 0; i < 1000; i++) {
                Card[] first = new Card[5];
                for (int j = 0; j < first.length; j++) {
                    String token = reader.next();
                    first[j] = Card.parseCard(token);
                }
                player1[i] = new Hand(first);
                Card[] second = new Card[5];
                for (int j = 0; j < second.length; j++) {
                    String token = reader.next();
                    second[j] = Card.parseCard(token);
                }
                player2[i] = new Hand(second);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return -1;
        }

        int player1Wins = 0;

        for (int i = 0; i < player1.length; i++) {
            if (player1[i].isBetter(player2[i])) {
                player1Wins ++;
            }
        }
        return player1Wins;
    }
}
