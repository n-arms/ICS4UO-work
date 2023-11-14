import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class NameScores {
    public static ArrayList<String> readNames(File file) {
        try {
            Scanner s = new Scanner(file);
            s.useDelimiter(",");
            ArrayList<String> names = new ArrayList<>();
            while (s.hasNext()) {
                String token = s.next();
                String[] results = token.split("\"");
                names.add(results[1]);
            }
            return names;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static int nameScore(String name) {
        return name.chars().map(c -> c - 'A' + 1).sum();
    }

    public static int totalNameScore(ArrayList<String> names) {
        int totalScore = 0;
        Collections.sort(names);
        for (int i = 0; i < names.size(); i++) {
            int score = nameScore(names.get(i)) * (i + 1);
            totalScore += score;
        }
        return totalScore;
    }
}
