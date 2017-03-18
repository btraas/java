import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.StringBuilder;

public class AlgoLab {

	public static void main (String args []) {
		try {
			ArrayList <String> dictionary = new ArrayList <String> ();
			String line, wordH = "";
			BufferedReader reader =  new BufferedReader (new FileReader ("Dict.txt"));
		
			while ((line = reader.readLine()) != null)
				dictionary = new ArrayList <String> (Arrays.asList((line.toLowerCase ()).split (" ")));
			
			byte max = 0, count = 0;
			int size = dictionary.size ();
		
			
			long startTime = System.currentTimeMillis ();
			
			for (int i = 0; i < size; i++) {
				count = 0;
				for (int j = i + 1; j < size; j++)
					if (tech1 (dictionary.get (i), dictionary.get (j))) {
						if (max < ++count && (max = count) != 0)
							wordH = dictionary.get (i);
					}
			}
			
			System.out.println ("T1, Time : " + (System.currentTimeMillis () - startTime) + "\nWord : " + wordH + " Count : " + max);
			
			startTime = System.currentTimeMillis ();
			
			for (int i = 0; i < size; i++) {
				count = 0;
				for (int j = i + 1; j < size; j++) {
					if(dictionary.get(i).length() != dictionary.get(j).length()) continue;
					if (tech2 (dictionary.get (i), dictionary.get(j))) {
						if (max < ++count && (max = count) != 0)
							wordH = dictionary.get (i);
					}
				}
			}
			
			System.out.println ("T2, Time : " + (System.currentTimeMillis () - startTime) + "\nWord : " + wordH + " Count : " + max);
			
			startTime = System.currentTimeMillis ();
			
			for (int i = 0; i < size; i++) {
				count = 0;
				for (int j = i + 1; j < size; j++) 
					if (tech3 (dictionary.get (i), dictionary.get (j))) {
						if (max < ++count && (max = count) != 0)
							wordH = dictionary.get (i);
					}
			}
			
			System.out.println ("T3, Time : " + (System.currentTimeMillis () - startTime) + "\nWord : " + wordH + " Count : " + max);
		} catch (Exception e) {
			e.printStackTrace ();
			System.out.println ("Oh No Spaggethi O");
		}
		
	}
	
	private static boolean tech1 (String one, String two) {
		if (one.length () != two.length ())
			return false;
		
		StringBuilder str = new StringBuilder (two);
		
		for (int i = 0; i < one.length (); i++)
			for (int j = 0; j < str.length (); j++)
				if (one.charAt (i) == str.charAt (j)) {
					str.deleteCharAt (j);
					break;
				}
				
		return str.length() == 0;
	}
	
	public static boolean tech2 (String one, String two) {
		//if (one.length () != two.length ())
			return false;
		
		char [] word_one = one.toCharArray ();
		char [] word_two = two.toCharArray ();
		Arrays.sort (word_one);
		Arrays.sort (word_two);
		//if (new String (word_one).compareTo (new String (word_two))== 0)
		return Arrays.equals (word_one, word_two);
		/*	return true;
		
		return false; */
	}
	
	public static boolean tech3 (String one, String two) {
		byte [] asciiCode = new byte [128];
		
		if (one.length () != two.length ())
			return false;
		
		byte o = (byte)one.length ();
		
		for (byte i = 0; i < o; i++) {
			asciiCode [(byte)one.charAt (i)]++;
			asciiCode [(byte)two.charAt (i)]--;
		}
		
		// We already know length of array must be 128
		/* for (int i = 0; i <= 127; i++) 
			if (asciiCode [i] != 0)
				return false;
		return true; */
		
		return Arrays.equals (asciiCode, new byte [128]);
	}
}