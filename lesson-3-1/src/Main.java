import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        //var grid = Grid.readGrid(new File("grid.txt"));
        //System.out.println(Grid.biggestProduct(grid));


        //var names = NameScores.readNames(new File("names.txt"));
        //System.out.println(NameScores.totalNameScore(names));

        System.out.println(PokerHands.player1Wins(new File("poker.txt")));
    }
}