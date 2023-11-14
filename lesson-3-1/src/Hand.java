import java.util.Arrays;

public class Hand {
    private Card[] cards;
    public Hand(Card[] cards) {
        this.cards = cards;
    }
    public int highCard() {
        return Arrays.stream(cards).mapToInt(Card::getRank).max().getAsInt();
    }
    // returns -1 if there is no highest pair
    public int pair() {

    }
    private int pair(int excluding) {
        int biggestRank = -1;
        for (int i = 0; i < cards.length; i++) {
            for (int j = i + 1; j < cards.length; j++) {
                if (cards[i].getRank() == cards[j].getRank() && cards[i].getRank() != excluding) {
                    biggestRank = Math.max(biggestRank, cards[i].getRank());
                }
            }
        }
        return biggestRank;
    }
}
