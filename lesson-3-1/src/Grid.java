import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Grid {
    private record Position(int row, int col) {}
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    public static int[][] readGrid(File file) {
        try {
            Scanner s = new Scanner(file);

            int[][] grid = new int[HEIGHT][WIDTH];

            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    int i = s.nextInt();
                    grid[row][col] = i;
                }
            }

            return grid;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static int biggestProduct(int[][] grid) {
        int biggest = 0;
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                biggest = Math.max(biggest, biggestProduct(grid, new Position(row, col), new HashSet<>(), 4));
            }
        }
        return biggest;
    }

    private static int biggestProduct(int[][] grid, Position start, HashSet<Position> visited, int moves) {
        if (moves == 0) return 1;
        int biggest = 0;
        for (Position move : getMoves(start)) {
            if (!visited.contains(move)) {
                HashSet<Position> innerVisited = (HashSet<Position>) visited.clone();
                innerVisited.add(move);
                int startValue = grid[start.row][start.col];
                int product = startValue * biggestProduct(grid, move, innerVisited, moves - 1);
                biggest = Math.max(biggest, product);
            }
        }
        return biggest;
    }

    private static List<Position> getMoves(Position start) {
        ArrayList<Position> all = new ArrayList<>(List.of(
                new Position(start.row + 1, start.col + 1),
                new Position(start.row, start.col + 1),
                new Position(start.row - 1, start.col + 1),
                new Position(start.row - 1, start.col),
                new Position(start.row - 1, start.col - 1),
                new Position(start.row, start.col - 1),
                new Position(start.row + 1, start.col - 1),
                new Position(start.row + 1, start.col)
        ));
        all.removeIf(position -> position.row < 0 || position.col < 0 || position.row >= WIDTH || position.col >= WIDTH);
        return all;
    }
}

