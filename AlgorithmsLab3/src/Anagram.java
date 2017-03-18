import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
public class Anagram {

	private String word;
	private int count;
	
	public Anagram(String word, int count)
	{
		this.word = word;
		this.count = count;
	}
	
	protected static abstract class Algorithm {
		public static final boolean VERBOSE = false;
		protected abstract boolean areAnagrams(String w1, String w2);
	}
	
	protected static class Algorithm1 extends Algorithm {

		@Override
		protected boolean areAnagrams(String word1, String word2) {
		
			// Short circuit if length not the same
			if(word1.length() != word2.length()) return false;
			
			//word1 = word1.toLowerCase();
			//word2 = word2.toLowerCase();
			
			// for each letter in word 1
			for(int i = 0; i < word1.length(); i++) {
				char c = word1.charAt(i);
				boolean found = false;
				
				//search word 2 for the letter
				for(int j = 0; j < word2.length(); j++) {
					if(word2.charAt(j) == c) {
						found = true;
						word2 = word2.substring(0, j) + word2.substring(j+1);
						continue;
					}
				}
				
				if(!found) return false; // short-circuit if a letter isn't found
			}
			
			return word2.length() == 0; 
		}
	}
	
	protected static class Algorithm1_Yudvir extends Algorithm {
		@Override
		protected boolean areAnagrams(String one, String two) {
			if (one.length () != two.length ()) {
				return false;
			}
			
			StringBuilder str = new StringBuilder (two);
			
			for (int i = 0; i < one.length (); i++)
				for (int j = 0; j < str.length (); j++)
					if (one.charAt (i) == str.charAt (j)) {
						str.deleteCharAt (j);
						break;
					}
					
			return str.length() == 0;
		}
	}
	
	protected static class Algorithm2 extends Algorithm {

		@Override
		protected boolean areAnagrams(String word1, String word2) {
		
			// Short circuit if length not the same
			if(word1.length() != word2.length()) return false;
			
			char[] chars1 = word1.toCharArray();
			char[] chars2 = word2.toCharArray();
			Arrays.sort(chars1);
			Arrays.sort(chars2);
			return Arrays.equals(chars1, chars2);
			
		}
	}
	
	
	protected static class Algorithm3 extends Algorithm {

		
		@Override
		protected boolean areAnagrams(String word1, String word2) {
		
			
			// Short circuit if length not the same
			if(word1.length() != word2.length()) return false;
			
			char[] chars1 = new char[128];
			char[] chars2 = new char[128];
			
			int length = word1.length();
			
			for(int i=0; i<length; ++i) {
				++chars1[word1.charAt(i)];
				++chars2[word2.charAt(i)];
			}
			
			return Arrays.equals(chars1, chars2);

		}
	}
	
protected static class Algorithm3_Byte extends Algorithm {

		
		@Override
		protected boolean areAnagrams(String word1, String word2) {
		
			
			// Short circuit if length not the same
			if(word1.length() != word2.length()) return false;
			
			byte[] chars1 = new byte[128];
			byte[] chars2 = new byte[128];
			
			int length = word1.length();
			
			for(int i=0; i<length; ++i) {
				++chars1[word1.charAt(i)];
				++chars2[word2.charAt(i)];
			}
			
			return Arrays.equals(chars1, chars2);

		}
	}
	
	protected static class Algorithm3_Morris extends Algorithm {

		
		@Override
		protected boolean areAnagrams(String word1, String word2) {
		
			
			// Short circuit if length not the same
			if(word1.length() != word2.length()) return false;
			
			int[] vector  = new int[128];
			int length = word1.length();
			
			for(int i=0; i<length; ++i) {
				++vector[word1.charAt(i)];
				--vector[word2.charAt(i)];
			}
			
			return Arrays.equals(vector, new int[128]);

		}
	}
	
	protected static class Algorithm3_Morris_Byte extends Algorithm {

		
		@Override
		protected boolean areAnagrams(String word1, String word2) {
		
			
			// Short circuit if length not the same
			if(word1.length() != word2.length()) return false;
			
			byte[] vector  = new byte[128];
			int length = word1.length();
			
			for(int i=0; i<length; ++i) {
				++vector[word1.charAt(i)];
				--vector[word2.charAt(i)];
			}
			
			return Arrays.equals(vector, new byte[128]);

		}
	}
	
	private static void printAnagrams(Algorithm algorithm, ArrayList<String> dictionary, boolean all, boolean separate) {
		
		if(Algorithm.VERBOSE) System.out.print("Checking dictionary with ");
		System.out.print(algorithm.getClass().getSimpleName());
		if(Algorithm.VERBOSE) 	System.out.println();
		else					System.out.print(": ");
		
		long startTime = System.currentTimeMillis();
		ArrayList<Anagram> anagrams = getAnagrams(algorithm, dictionary, all);
		
		
		
		//System.out.println("printing anagarams...");
		
		if(anagrams == null || anagrams.size() < 1) {
			System.out.println("no anagrams...");
			return;
		}
		
		int anagramSize = anagrams.size();
		
		if(anagramSize == 1) separate = true;
		
		if(separate)
		{
			for(int i = 0; i < anagramSize; i++) {
				System.out.println( "[" + anagrams.get(i).word + "] has " + anagrams.get(i).count + " anagrams");
			}
		} else {
			System.out.print("[");
			for(int i = 0; i < anagrams.size(); i++) {
				System.out.print(anagrams.get(i).word);
				if(i+1 < anagramSize) System.out.print(",");
			}
			System.out.println("] each have "+anagrams.get(0).count+" anagrams");
		}
		
		
		long endTime   = System.currentTimeMillis();	
		System.out.println(" Executed "+algorithm.getClass().getSimpleName()
				+" in " + (endTime - startTime)/1000 + " seconds");
		System.out.println();
		
		
	}
	
	
	
	private static ArrayList<Anagram> getAnagrams(Algorithm algorithm, ArrayList<String> dictionary, boolean all) {
		
		
		if(Algorithm.VERBOSE) System.out.print("Checking ");
		
		
		ArrayList<Anagram> anagrams = new ArrayList<Anagram>();
		ArrayList<Anagram> mostAnagrams = new ArrayList<Anagram>();
		int count = 1; // minimum number of anagrams to show
		
		char curChar = '\0';
		
		int size = dictionary.size();
		for(int i=0; i<size; ++i)
		{
			String str_i = dictionary.get(i);
			
			// Print the first char if it's new... for the user only
			if(Algorithm.VERBOSE && curChar != str_i.charAt(0)) {
				curChar = str_i.charAt(0);
				System.out.print(curChar);
			}
			
			// val = Number of anagrams of this word
			// j   = index of existing anagrams (found words that are anagrams before this word in the dictionary)
			// k   = index of future words (after this word in the dictionary)
			int val = 0, j = 0, k=0;
			
			// 1. Check existing anagrams. Will always be every word that has anagrams, less than index i
			for(j=0; j<anagrams.size(); ++j) {
				if(algorithm.areAnagrams(str_i, anagrams.get(j).word)) ++val;
			}
			
			// 2. Check later dictionary words. i+1 so we check everything after index i
			for(k=i+1; k<size; ++k) 
				if(algorithm.areAnagrams(str_i, dictionary.get(k))) ++val;
				
			
			// 3. Now handle if this is one of the current words with the most anagrams ('Anagram record')
			if(val >= count) {
				
				// If this has more anagrams than the current record
				if(val > count) {
					mostAnagrams.clear(); 	// Clear ArrayList of the words with the most anagrams ('Anagram record')
					count = val;			// Set the new record to this number
				} 
				
				// Add this word to the list of 'Anagram record' words
				mostAnagrams.add(new Anagram(str_i, count));
			}
			
		}
		
		if(Algorithm.VERBOSE) System.out.println();
		
		
		if(all) return anagrams;
		else    return mostAnagrams;
	}
	

	
	public static void main(String[] args) {

		BufferedReader reader;
		ArrayList<String> dictionary = new ArrayList<String>();
		
		try {
			//input = Files.readAllBytes(Paths.get("Dict.txt"));
			reader =  new BufferedReader (new FileReader ("Dict.txt"));
			dictionary = new ArrayList<String>(Arrays.asList(reader.readLine().toLowerCase().split(" ")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//String[] dic = new String(input).toLowerCase().split(" ");
		// dictionary = new ArrayList<String>(Arrays.asList(reader.getLine().toLowerCase().split(" ")));
		
		
		/* Uncomment one or more lines below to run them sequentially */
		
		printAnagrams(new Algorithm1(), dictionary, false, false);
		printAnagrams(new Algorithm1_Yudvir(), dictionary, false, false);
		printAnagrams(new Algorithm2(), dictionary, false, false);
		printAnagrams(new Algorithm3(), dictionary, false, false);
		printAnagrams(new Algorithm3_Byte(), dictionary, false, false);
		printAnagrams(new Algorithm3_Morris(), dictionary, false, false);
		printAnagrams(new Algorithm3_Byte(), dictionary, false, false);
		printAnagrams(new Algorithm3_Morris_Byte(), dictionary, false, false);

	
	}

}
