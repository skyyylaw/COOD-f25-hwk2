import java.io.*;
import java.util.*;

/*
 * Implements a text search engine for a collection of documents in the same directory.
 *
 * In buildMap, I used a HashMap<String, Set<String>> because out goal is to associate each unique word that appears across all files to the files it appears in, which is a singular string key to a set of one or multiple * string filenames. It also provides the fatest lookup + put in O(1)
 *
 * In search, I first used a HashMap<String, Integer>  to map each file to the number of terms from the args
 * that it contains because it provides a fast O(1) lookup and put. Then I iterated through the hashmap to reorganize data into a TreeMap<Integer, TreeSet<String>>, where the key is a word count group that each file that contains the same number of terms from the args would fall in. Using a treeset means the keys are sorted (with O(log(n)) for add and lookup), and using a treeset for filenames mean the filenames would be sorted as well. This is useful because we want to rank the filenames first by count and then by filenames.
 *
 * In search, I used array list because it provides the fastest O(1) add to the back functionality.
 *
 */

public class WordSearch {
	
	public static Map<String, Set<String>> buildMap(String dirName) {
		File dir = new File(dirName);	// create a File object for this directory
		
		// make sure it exists and is actually a directory
		if (dir.exists() == false || dir.isDirectory() == false) {
            // this tells the caller "you gave me bad input"
			throw new IllegalArgumentException(dirName + " does not exist or is not a directory");
		}
		
		File[] files = dir.listFiles();		// get the Files in the specified directory
		
		// Implement the rest of this method starting from here!

		// this is for debugging, just to make sure it's reading the right files
    for (File file : files) {
        System.out.println(file.getName());
    }

    HashMap<String, Set<String>> map = new HashMap<>();
    for (File f : files){
      try{
        Scanner scanner = new Scanner(f);
        while (scanner.hasNextLine()){
          String line = scanner.nextLine();
          String[] words = line.split(" ");
          for (String w : words){
            String k = w.toLowerCase();
            String fileName = f.getName();
            if (!map.containsKey(k)) {
              map.put(w.toLowerCase(), new HashSet<>());
            }
            map.get(k).add(fileName);
          }
        }
      } catch (FileNotFoundException e) {
        System.out.println("404");
        throw new RuntimeException(e);
      }
    }
		return map; // change this as necessary
	}
	
	public static List<String> search(String[] terms, Map<String, Set<String>> map) {
		// Implement this method starting from here!
    List<String> res = new ArrayList<>();
    // count how many of the search terms each file contains
    HashMap<String, Integer> fileCount = new HashMap<>();
    // reverse the above and group filesnames into corresponding counts, while keep filenames sorted
    TreeMap<Integer, TreeSet<String>>  rank = new TreeMap<>();

    // count file contains
    for (String w : terms) {
      if (map.containsKey(w)){
        for (String fileName : map.get(w)) {
          if (!fileCount.containsKey(fileName)) {
            fileCount.put(fileName, 0);
          }
          fileCount.put(fileName, fileCount.get(fileName) + 1);
        }
      }
    }

    // populate rank
    for (String fileName : fileCount.keySet()) {
      int count = fileCount.get(fileName);
      if (!rank.containsKey(count)) {
        rank.put(count, new TreeSet<>());
      }
      rank.get(count).add(fileName);
    }

    // generate results
    for (int count : rank.descendingKeySet()) {
      for (String fileName : rank.get(count)) {
        res.add(fileName);
      }
    }
		return res; // change this as necessary
	}
	
	public static void main(String[] args) {
		Map<String, Set<String>> map = buildMap(args[0]);
		System.out.println(map); 					// for debugging purposes
		
		System.out.print("Enter a term to search for: ");
		
		try (Scanner in = new Scanner(System.in)) { // create a Scanner to read from stdin
			String input = in.nextLine();			// read the entire line that was entered
			String[] terms = input.split(" ");		// separate tokens based on a single whitespace
			List<String> list = search(terms, map);	// search for the tokens in the Map
			for (String file : list) {				// print the results
				System.out.println(file);
			}
		}
		catch (Exception e) {
			// oops! something went wrong
			e.printStackTrace();
		}
	}

}
