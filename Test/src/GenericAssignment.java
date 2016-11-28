
public class GenericAssignment {

	/* *****************************************************************************************
	 *                                                                                         *
	 *  1. Convert the following method to a generic method that will work for any class...    *
	 *  
	 *  public static int min(int[]data){
			int result = data[0];
			for (int value : data){
				if (result > value)
					result = value;
				}
			return result;
		}
	 *  
	 *                                                                                         *
	 * *****************************************************************************************
	 */
	public static <T extends Comparable<T>> T min(T[] data) {
		T result = data[0];
		for (T value : data) {
			if (result.compareTo(value) > 0) {
				result = value;
			}
		}
		return result;
	}
	
	/* ******************************************************************************************
	 *                                                                                          *
	 *  2. Which search algorithm will search for a value in a sorted array fastest?            *
	 *                                                                                          *
	 * ******************************************************************************************     
	 *                                                     
	 *  Binary search algorithm (choose halfway point, find if it's greater or less than it,  
	 *     and continue.)              
	 */
	
	
	

	/* ********************************************************************************
	 *                                                                                *
	 * 3. Demonstrate the quicksort algorithm on the following data values provided:  *
	 *     {5, 7, 9, 13, 2, 4, 6}                                                     *
	 *                                                                                *
	 * ********************************************************************************
	 *     
	 *     {5, 7, 9, 13, 2, 4, 6}
	 * 	   {4, 7, 9, 13, 2, 5, 6}
	 * 	   {4, 5, 9, 13, 2, 7, 6}
	 *     {4, 2, 9, 13, 5, 7, 6}
	 *     {4, 2, 5, 13, 9, 7, 6}
	 *     
	 *     {{4, 2}, 5, {13, 9, 7, 6}}
	 *     {{2, 4}, 5, {6, 9, 7, 13}}
	 *     
	 *     {2, 4, 5, {6, 9, 7}, 13}
	 *     
	 *     {2, 4, 5, 6, {9, 7}, 13}
	 *     {2, 4, 5, 6, {7, 9}, 13}
	 *     
	 *     {2, 4, 5, 6, 7, 9, 13}
	 * 
	 */

	/* **************************************************************
	 *                                                              *
	 *  4. What is the Big O order for this BubbleSort algorithm?   *
	 *                                                              *
	 * ************************************************************** 
	 *  
	 *    procedure bubbleSort( A : list of sortable items ) defined as:
            do
				swapped := false
				for each i in 0 to length( A ) - 2 do:
				  if A[ i ] > A[ i + 1 ] then
				    swap( A[ i ], A[ i + 1 ] )
					swapped := true
				  end if
				end for
		    while swapped
		  end procedure
	 * 
	 * 
	 *   let N = length( A )
	 * 
	 *   *** For loop expanded ***
	 *   (N-2)+(N-3)...+2+1
	 *   
	 *   *** (N + N-1 + N-2 + ... + 2 + 1) Has N terms ***
	 *   *** Average term is N/2 ***
	 *   *** Therefore, (N + N-1 + N-2 + ... + 2 + 1) == N * (N/2) *** 
	 *   *** However, we don't have the terms N or N-1, so subtract them ***
	 *   
	 *   N*N / 2 - (N + N-1) 
	 *   
	 *   *** Simplified ***
	 *   (N^2)/2 - 2N - 1
	 *   
	 *   *** Big O order ignores 0-degree constants ***
	 *   (N^2)/2 -2N
	 *   
	 *   *** Big O order ignores multiplied constants ***
	 *   (N^2) - N
	 *   
	 *   *** N is insignificant to N^2. We keep the largest order only. ***
	 *   N^2
	 *   
	 *   
	 *   #########################
	 *   #                       #
	 *   #  Order: O(N^2)        #
	 *   #                       #
	 *   #########################
	 *   
	 *   
	 */
	
	/* *******************************************************************
	 *                                                                   *
	 *  Bonus: getSumOfTwoLargestInts(int[] arrayOfInts) with generics   *
	 *                                                                   *
	 * ******************************************************************* 
	*/
	public static <T extends Number & Comparable<T>> double getSumOfTwoLargestNumbers(T[] array) {
		
		// Set from first two indexes.
		T largest1 = array[0];
		T largest2 = array[1];
		
		// Swap if largest2 > largest1
		if (largest1.compareTo(largest2) < 0) {
			T largestTmp = largest1;
			largest1 = largest2;
			largest2 = largestTmp;
		}
		
		// For each in array starting after the first two.
		for (int i = 2; i < array.length; i++) {
			
			// If this Number > largest1 (and therefore > both)
			if (array[i].compareTo(largest1) > 0) {
				
				// Copy largest down to second largest
				largest2 = largest1;
				
				// Set new largest
				largest1 = array[i];
				
			// Otherwise if this Number > largest2 (but not largest1)
			} else if (array[i].compareTo(largest2) > 0) {
				
				// Update largest2 to this one.
				largest2 = array[i];
			}
		}
		// Sum & return.
		return largest1.doubleValue() + largest2.doubleValue();
	}
	
	
	/*
	 * Testing
	 */
	public static void main(String[] args) {
	
		// Q1 testing
		
		Integer[] array1 = new Integer[] {5, 8, 3, 4, 5};
		Integer[] array2 = new Integer[] {3, 9, 1, 123, -2};
		
		String[] array3 = new String[] {"world", "hello", "hi"};
		String[] array4 = new String[] {"3", "5", "1"};
		
		System.out.println(min(array1));
		System.out.println(min(array2));
		System.out.println(min(array3));
		System.out.println(min(array4));
		
		
		// Bonus testing

		Double[] array5 = new Double[] {1.2, 5.7, -123.0, 0.0};
		Short[] array6 = new Short[] {1, 2, 3, 9, -1};
		
		System.out.println("******** BONUS *********");
		System.out.println(getSumOfTwoLargestNumbers(array1));
		System.out.println(getSumOfTwoLargestNumbers(array2));
		System.out.println(getSumOfTwoLargestNumbers(array5));
		System.out.println(getSumOfTwoLargestNumbers(array6));
		
	}

}
