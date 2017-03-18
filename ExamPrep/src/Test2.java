
public class Test2 {
	

	public static <T extends Comparable<T>> void bubbleSort( T[] a ){
		  boolean swapped = false;
		  T temp = null;
		  do{
		    swapped = false;
		    for(int i = 0; i<=a.length - 2; i++){
		      if (a[i].compareTo(a[i+1]) > 0){
		        temp = a[i];
		        a[i] = a[i+1];
		        a[i+1] = temp;
		        swapped = true;
		      }
		    }
		  }while( swapped);
		}

	public static <T extends Comparable<T>> void  printArray(T[] ints) {
		for(int i = 0; i < ints.length; i++) {
			System.out.print(ints[i] + ", ");
		}
		System.out.println();
	}
	
	public static int sum(int[] value, int n) {
		if(n == 1) return value[0];
		return sum(value, n-1) + value[n-1];
	}
	
	public static void main(String[] args){
		
		System.out.println(sum(new int[]{99, 5, 4, 2}, 4));
		/*
		Integer[] ints = {	new Integer(1), 
							new Integer(6), 
							new Integer(4), 
							new Integer(3), 
							new Integer(8),
							new Integer(2) };
		
		printArray(ints);
		bubbleSort(ints);
		printArray(ints);
		*/
		
	}

}
