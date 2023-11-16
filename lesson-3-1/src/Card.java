public class Card {
    public static final int KING_RANK = 13;
    public static final int ACE_RANK = 1;
    private int rank;
    private Suit suit;
    public enum Suit {
        Heart,
        Diamond,
        Club,
        Spade
    };
    public Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }
    public static Card parseCard(String string) throws IllegalArgumentException {
        if (string.length() != 2) throw new IllegalArgumentException("A card string must be of length 2");

        char rankChar = string.charAt(0);
        int rank;
        if (rankChar >= '0' && rankChar <= '9') {
            rank = rankChar - '0';
        } else {
            rank = switch (rankChar) {
                case 'A' -> 1;
                case 'T' -> 10;
                case 'J' -> 11;
                case 'Q' -> 12;
                case 'K' -> 13;
                default -> throw new IllegalArgumentException("Unknown rank " + rankChar);
            };
        }

        char suitChar = string.charAt(1);
        Suit suit = switch (suitChar) {
            case 'H' -> Suit.Heart;
            case 'D' -> Suit.Diamond;
            case 'C' -> Suit.Club;
            case 'S' -> Suit.Spade;
            default -> throw new IllegalArgumentException("Unknown suit " + suitChar);
        };

        return new Card(rank, suit);
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
}
