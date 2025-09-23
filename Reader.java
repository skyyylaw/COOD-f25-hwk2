/**
 * @author [YOUR NAME HERE!]
 *
 * This class contains a method for reading from a file and creating Sentence objects
 * for a sentiment analysis program.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Reader {
	/**
	 * This method reads sentences from the input file, creates a Sentence object
	 * for each, and returns a Set of the Sentences.
	 * 
	 * @param filename Name of the input file to be read
	 * @return Set containing one Sentence object per sentence in the input file; null if filename is null
	 */
	public static Set<Sentence> readFile(String filename) {
		/*
		 * Implement this method in Step 1
		 */
    if (filename == null) {
      System.out.println("null file name");
      return null;
    }
    HashSet<Sentence> set = new HashSet<>();
    File f = new File(filename);
    if (f.exists() == false || f.isFile() == false) {
      System.out.println("file 404");
      return null;
    }
    try {
      Scanner scanner = new Scanner(f);
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] words = line.split(" ");
        if (words.length < 2) {
          System.out.println("Invalid sentence: " + line);
          continue;
        }
        int score = 0;
        // A line that starts with an int outside the range [-2, 2]
        try {
          score = Integer.parseInt(words[0]);
          if (score < -2 || score > 2) {
            // invalid
            System.out.println(words[0] + " is out of range [-2,2]");
            continue;
          } else {
            // valid
            String[] s = Arrays.copyOfRange(words, 1, words.length);
            String sentence = String.join( " ", s);
            set.add(new Sentence(score, sentence));
          }
        } catch (NumberFormatException e) {
          // invalid
          System.out.println(words[0] + " is not an integer");
          continue;
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("scanner");
      return null;
    }
		return set;
	}

    /**
     * Use this main() method for testing your Reader.readFile method with different inputs.
     * Note that this is _NOT_ the main() method for the whole sentiment analysis program!
     * Just use it for testing this class. It is not considered for grading.
     */
    public static void main(String[] args) {
      Set<Sentence> set = readFile("test_reader.txt");
      for (Sentence s : set) {
        System.out.println(s.getScore() + " " + s.getText());
      }
//      assert
    }
}
