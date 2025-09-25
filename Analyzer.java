/**
 * @author [YOUR NAME HERE!]
 *
 * This class contains the methods used for conducting a simple sentiment analysis.
 */

import java.util.*;

public class Analyzer {

  private static HashMap<String, Double> memo = new HashMap<>();

	/**
	 * This method calculates the weighted average for each word in all the Sentences.
	 * This method is case-insensitive and all words should be stored in the Map using
	 * only lowercase letters.
	 * 
	 * @param sentences Set containing Sentence objects with words to score
	 * @return Map of each word to its weighted average; null if input is null
	 */
	public static Map<String, Double> calculateWordScores(Set<Sentence> sentences) {
		/*
		 * Implement this method in Step 2
		 */
    if (sentences == null) {
      return null;
    }
    HashMap<String, Integer> wordTotalScore = new HashMap<>();
    HashMap<String, Integer> wordTotalCount = new HashMap<>();
    HashMap<String, Double> wordAvgScore = new HashMap<>();
    for (Sentence s : sentences) {
      String line = s.getText();
      int score = s.getScore();
      if (score < -2 || score > 2 || line == null || line.length()==0 ) {
        continue;
      }
      String[] words = line.split(" ");
      for (String w : words) {
        String lowerW = w.toLowerCase();
        if (lowerW.charAt(0) < 'a' || lowerW.charAt(0) > 'z') {
          // ignore this word since it does not start with a letter
          continue;
        }
        if (!wordTotalScore.containsKey(lowerW)) {
          wordTotalCount.put(lowerW, 1);
          wordTotalScore.put(lowerW, score);
          wordAvgScore.put(lowerW, (double) score);
        } else {
          wordTotalCount.put(lowerW, wordTotalCount.get(lowerW) + 1);
          wordTotalScore.put(lowerW, wordTotalScore.get(lowerW) + score);
          wordAvgScore.put(lowerW, (double) (wordTotalScore.get(lowerW) / wordTotalCount.get(lowerW)));
        }
      }
    }
		return wordAvgScore;
	}
	
	/**
	 * This method determines the sentiment of the input sentence using the average of the
	 * scores of the individual words, as stored in the Map.
	 * This method is case-insensitive and all words in the input sentence should be
	 * converted to lowercase before searching for them in the Map.
	 * 
	 * @param wordScores Map of words to their weighted averages
	 * @param sentence Text for which the method calculates the sentiment
	 * @return Weighted average scores of all words in input sentence; null if either input is null
	 */
	public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {
		/*
		 * Implement this method in Step 3
		 */
    if (wordScores == null || sentence == null || wordScores.size() == 0 || sentence.length() == 0) {
      return 0;
    }

    if (memo.containsKey(sentence)) {
      System.out.println("Using memo!");
      return memo.get(sentence);
    }

    String[] words = sentence.split(" ");
    Double totalWordCount = 0.0;
    Double totalScore = 0.0;
    for (String w : words) {
      String lowerW = w.toLowerCase();
      // ignore this word if it does not start with a letter
      if (lowerW.charAt(0) < 'a' || lowerW.charAt(0) > 'z') {
        continue;
      }

      if (!wordScores.containsKey(lowerW)) {
        wordScores.put(lowerW, 0.0);
      }
      totalWordCount += 1;
      totalScore += wordScores.get(lowerW);
    }

    double res = totalScore / totalWordCount;
    memo.put(sentence, res);
		return res;
	}

    /**
     * Use this main() method for testing your calculateWordScores and
     * calculateSentenceScore methods with different inputs.
     * Note that this is _NOT_ the main() method for the whole sentiment analysis program!
     * Just use it for testing this class. It is not considered for grading.
     */
    public static void main(String[] args) {
      Set<Sentence> sentences = new HashSet<>();
      sentences.add(new Sentence(2, "I like dogs"));
      sentences.add(new Sentence(5, "dogs dogs dogs dogs dogs dogs 'dogs ,dogs .dogs"));
      sentences.add(new Sentence(-2, "I"));
      Map<String, Double> scores = calculateWordScores(sentences);
      assert (scores.get("dogs") == 1);
      assert (scores.get("i") == 0);
      assert (scores.get("like") == 2);
      System.out.println("calculateWordScores Tests Passed");


      scores = new HashMap<>();
      scores.put("dogs", 1.5);
      scores.put("are", 0.0);
      scores.put("cute", 2.0);
      double score = calculateSentenceScore(scores, "dogs are cute");
      assert (score == (3.5 / 3));
      System.out.println("calculateSentenceScore Tests Passed");

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
      scores = calculateWordScores(sentences);
      System.out.println(scores);
      Scanner scanner = new Scanner(System.in);
      while (true){
        System.out.println("Enter a sentence: ");
        String s = scanner.nextLine();
        if (s.equals("quit")) {
          break;
        }
        score = calculateSentenceScore(scores, s);
        System.out.println("Score for [" + s + "]: " + score);
      }


    }

}
