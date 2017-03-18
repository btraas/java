import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
public class SpellChecker {

	public static boolean seqSearch(String word, ArrayList<String> dictionary) {
		
		word = word.toLowerCase();
		char first = word.charAt(0);
		
		
		String dictWord;
		for(int i = 0; i < dictionary.size(); i++) {
			dictWord = dictionary.get(i);
			if(dictWord.charAt(0) != first) continue;
			
			if(word.equals(dictWord)) return true;
			
		}
		
		return false;
	}
	
	
	public static boolean binSearch(String word, ArrayList<String> dictionary) {
		
		word = word.toLowerCase();
		char first = word.charAt(0);
		
		int low = 0;
		int high = dictionary.size() - 1;
		
		while(low <= high) {
			int mid = low + (high - low) / 2;
			if(word.compareTo(dictionary.get(mid)) < 0) high = mid -1;
			else if (word.compareTo(dictionary.get(mid)) > 0) low = mid + 1;
			else return true;
		}
		
		
		
		return false;
	}
	
	
	public static void main(String[] args) {

		
		BufferedReader reader, reader2;
		ArrayList<String> dictionary = new ArrayList<String>();
		ArrayList<String> testdata   = new ArrayList<String>();
		
		try {
			//input = Files.readAllBytes(Paths.get("Dict.txt"));
			reader =  new BufferedReader (new FileReader ("lab4_wordlist.txt"));
			reader2 =  new BufferedReader (new FileReader ("lab4_testdata.txt"));
			
			String s;
			while((s = reader.readLine()) != null) {
				dictionary.add(s.toLowerCase());
			}
			
			testdata = new ArrayList<String>(
					Arrays.asList(reader2.readLine().toLowerCase().split(" ")));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int seqErrors = 0, binErrors = 0;
		
		// seq
		
		long startTime = TimeUnit.NANOSECONDS.toMicros(System.nanoTime());
		for(int i = 0; i < testdata.size(); i++) {
			if(!seqSearch(testdata.get(i), dictionary)) seqErrors++;
		}
		System.out.println("seqErrors: "+seqErrors);
		
		long endTime   = TimeUnit.NANOSECONDS.toMicros(System.nanoTime());	
		System.out.println(" Executed seqSearch()"
				+" in " + (endTime - startTime) + " microseconds");
		System.out.println();
		System.out.println();
		
		
		// bin
		
		startTime = TimeUnit.NANOSECONDS.toMicros(System.nanoTime());
		for(int i = 0; i < testdata.size(); i++) {
			if(!binSearch(testdata.get(i), dictionary)) binErrors++;
		}
		System.out.println("binErrors: "+binErrors);
		
		endTime   = TimeUnit.NANOSECONDS.toMicros(System.nanoTime());	
		System.out.println(" Executed binSearch()"
				+" in " + (endTime - startTime) + " microseconds");
			
			
		

		
		
		
		
		
		
		//String[] dic = new String(input).toLowerCase().split(" ");
		// dictionary = new ArrayList<String>(Arrays.asList(reader.getLine().toLowerCase().split(" ")));
		
		/*

		
		printAnagrams(new Algorithm1(), dictionary, false, false);
		printAnagrams(new Algorithm1_Yudvir(), dictionary, false, false);
		printAnagrams(new Algorithm2(), dictionary, false, false);
		printAnagrams(new Algorithm3(), dictionary, false, false);
		printAnagrams(new Algorithm3_Byte(), dictionary, false, false);
		printAnagrams(new Algorithm3_Morris(), dictionary, false, false);
		printAnagrams(new Algorithm3_Byte(), dictionary, false, false);
		printAnagrams(new Algorithm3_Morris_Byte(), dictionary, false, false);
		*/
	
	}

}
