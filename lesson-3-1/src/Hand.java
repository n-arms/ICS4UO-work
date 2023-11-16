import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Hand {
    private Card[] cards;
    public Hand(Card[] cards) {
        this.cards = cards;
        Arrays.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return -Integer.compare(c1.getRank(), c2.getRank());
            }
        });
    }
    public int highCard() {
        return Arrays.stream(cards).mapToInt(Card::getRank).max().getAsInt();
    }
    // returns -1 if there is no highest pair
    public int pair() {
        return pair(-1);
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
    public record TwoPair(int high, int low) {}
    public TwoPair twoPair() {
        int high = pair();
        if (high == -1) {
            return null;
        }
        int low = pair(high);
        if (low == -1) {
            return null;
        } else {
            return new TwoPair(high, low);
        }
    }
    public int threeOfAKind() {
        return nOfAKind(3);
    }
    private int nOfAKind(int number) {
        int[] frequencies = new int[13];
        for (Card c : cards) {
            frequencies[c.getRank() - 1] += 1;
        }
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] >= number) {
                return i + 1;
            }
        }
        return -1;
    }
    // return the rank of highest card in the straight
    public int straight() {
        for (int i = 0; i < cards.length; i++) {
            int straight = straightFrom(i, 5);
            if (straight != -1) {
                return straight;
            }
        }
        return -1;
    }

    private int straightFrom(int start, int remaining) {
        if (remaining == 0) {
            return start;
        }
        int nextRank = -1;
        if (cards[start].getRank() == Card.KING_RANK) {
            nextRank = Card.ACE_RANK;
        } else {
            nextRank = cards[start].getRank() + 1;
        }
        int nextIndex = -1;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getRank() == Card.ACE_RANK) {
                nextIndex = i;
            }
        }
        if (nextIndex == -1) {
            return -1;
        }
        return straightFrom(nextIndex, remaining - 1);
    }

    // return the rank of the highest card in the flush
    public int flush() {
        Card.Suit suit = cards[0].getSuit();
        for (int i = 1; i < cards.length; i++) {
            if (suit != cards[i].getSuit()) {
                return -1;
            }
        }
        return cards[0].getRank();
    }
    public record FullHouse(int two, int three) {}
    public FullHouse fullHouse() {
        int three = threeOfAKind();
        if (three == -1) {
            return null;
        }
        int two = pair(three);
        if (two == -1) {
            return null;
        } else {
            return new FullHouse(two, three);
        }
    }
    public int fourOfAKind() {
        return nOfAKind(4);
    }
    public int straightFlush() {
        if (flush() == -1) {
            return -1;
        } else {
            return straight();
        }
    }
    public boolean royalFlush() {
        int high = straightFlush();
        if (high == -1) {
            return false;
        }
        return high == 1;
    }
    public int bestHand() {
        if (royalFlush()) {
            return 9;
        } else if (straightFlush() != -1) {
            return 8;
        } else if (fourOfAKind() != -1) {
            return 7;
        } else if (fullHouse() != null) {
            return 6;
        } else if (flush() != -1) {
            return 5;
        } else if (straight() != -1) {
            return 4;
        } else if (threeOfAKind() != -1) {
            return 3;
        } else if (twoPair() != null) {
            return 2;
        } else if (pair() != -1) {
            return 1;
        } else {
            return 0;
        }
    }
    public boolean isBetter(Hand other) {
        int handScore = this.bestHand();
        int otherHandScore = other.bestHand();

        if (handScore == otherHandScore) {
            switch (handScore) {
                case 0 -> {
                    return this.highCard() > other.highCard();
                }
                case 1 -> {
                    return this.pair() > other.pair();
                }
                case 2 -> {
                    TwoPair thisPair = this.twoPair();
                    TwoPair otherPair = other.twoPair();
                    if (thisPair.high > otherPair.high) {
                        return true;
                    } else if (thisPair.high < otherPair.high) {
                        return false;
                    } else {
                        return thisPair.low > otherPair.low;
                    }
                }
                case 3 -> {
                    return this.threeOfAKind() > other.threeOfAKind();
                }
                case 4 -> {
                    return this.straight() > other.straight();
                }
                case 5 -> {
                    return this.flush() > other.flush();
                }
                case 6 -> {
                    FullHouse thisHouse = this.fullHouse();
                    FullHouse otherHouse = other.fullHouse();
                    if (thisHouse.three > otherHouse.three) {
                        return true;
                    } else if (thisHouse.three < otherHouse.three) {
                        return false;
                    } else {
                        return thisHouse.two > otherHouse.two;
                    }
                }
                case 7 -> {
                    return this.fourOfAKind() > other.fourOfAKind();
                }
                case 8 -> {
                    return this.straightFlush() > other.straightFlush();
                }
                case 9 -> {
                    return true;
                }
                default -> throw new AssertionError("Invalid hand id");
            }
        } else {
            return handScore > otherHandScore;
        }
    }
}
