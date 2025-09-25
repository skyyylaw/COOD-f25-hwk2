import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author [your name here]
 *
 * This class holds the main() method for the sentiment analysis program.
 */

public class Main {

    public static void main(String[] args) {
        // implement this method in Step 4
      Set<Sentence> sentences;
      Map<String, Double> scores;

      if (args.length != 1) {
        System.out.println("no input file");
        return;
      }
      String inputFile = args[0];
      sentences = Reader.readFile(inputFile);
      if (sentences == null) {
        System.out.println("bad input file");
        return;
      }
      scores = Analyzer.calculateWordScores(sentences);
      System.out.println(scores);
      Scanner scanner = new Scanner(System.in);
      while (true){
        System.out.println("Enter a sentence: ");
        String s = scanner.nextLine();
        if (s.equals("quit")) {
          break;
        }
        Double score = Analyzer.calculateSentenceScore(scores, s);
        System.out.println("Score for [" + s + "]: " + score);
      }
    }
}
