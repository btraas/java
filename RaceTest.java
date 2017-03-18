/** In-lab assignment: Threading
1. Create 2 threads to add the data provided in main <1 mark>
2. After the threads have FINISHED print the resulting linked list (in-order) <1 mark>
3. Verify that your code causes race conditions
4. Answer the following: <3 marks>
	a) Give TWO locations where a race condition can occur
	b) Fix the code to prevent race conditions so that all DLinkedLists are thread safe
********************************************************************************************/

interface DIterator<T> {
	boolean isEmpty();
	boolean hasNext();
	boolean hasPrevious();
	T next();
	T previous();
}
class DLinkedList <T extends Comparable<T>>{
	private DNode head;
	private class DNode {
		T data;
		DNode previous, next;
		DNode(T d){
			data = d;
		}
	}
	public void clear(){
		head = null;
	}
	public boolean insert(T d) {
		try{
		DNode temp = new DNode(d);
		DNode cur = head;
		DNode prev = head;
		//1. empty list case
		if (head == null){
			head = temp;
			return true;
		}
		//2. non-empty list, find position
		while((cur.next != null) && (cur.data.compareTo(d)<0)){
			prev = cur;
			cur = cur.next;
		}
		//3. value exists in list already - fail
		if (cur.data.compareTo(d) == 0 )
			return false;
		//4. single node in list
		if (cur == prev){
			//5. single node < new node
			if (cur.data.compareTo(d) < 0){
				cur.next = temp;
				temp.previous = cur;
				return true;
			}
			//6. single node > new node
			temp.next = cur;
			cur.previous = temp;
			head = temp;
			return true;
		}
		//7. multiple nodes in list
		prev.next = temp;
		temp.next = cur;
		//8. check if being added at the start of the list
		// if it is there is no previous node and the head of list
		// needs to change
		if (cur.previous != null)
			cur.previous = temp;
		else
			head = temp;
		temp.previous = prev;
		}catch(Exception e){}
		return true;
	}
	public DIterator<T> iterator(){
		return new DIterator<T>(){
			DNode cur = head;
			public boolean isEmpty(){
				if (cur != null)
					return false;
				return true;
			}
			public boolean hasNext(){
				return cur.next != null;
			}
			public boolean hasPrevious(){
				return cur.previous != null;
			}
			public T next(){
				T d = cur.data;
				cur = cur.next;
				return d;
			}
			public T previous(){
				T d = cur.data;
				cur = cur.previous;
				return d;
			}
		};
	}
}



// CREATE YOUR RUNNABLE CLASS(ES) HERE FOR THREADING

public final class RaceThread extends Thread {
	
	private final int[] primes;
	
	public RaceThread(final int[] primes) {
		this.primes = primes;
	}
	
	public void run() {
		for (int i = 0; i < primes.length; i++) {
			list.insert(new Integer(primes[i]));
		}
	}
}



public class RaceTest {
		static DLinkedList<Integer> list = new DLinkedList<Integer>();

	public static void main(String[] args){
		int[] prime1 = {47,13,23,17};//for Thread1
		int[] prime2 = {5,19,37,7};//for Thread2
		
		// make threads
		
		RaceThread r1 = new RaceThread(prime1);
		RaceThread r2 = new RaceThread(prime2);
		
		// launch them
		
		r1.start();
		r2.start();
		
		//make sure you WAIT for Thread1 and Thread2 to complete before attempting to print
		print(list);//result should display missing data, data out of order, different each time
	}
	public static <P extends Comparable<P>> void print(DLinkedList<P> list){
		DIterator<P> i = list.iterator();
		while(!i.isEmpty())
			System.out.print(""+i.next()+" ");
		System.out.println("");
	}
	public static <P extends Comparable<P>> void printR(DLinkedList<P> list){
		DIterator<P> i = list.iterator();
		while(i.hasNext()) i.next();
		while (!i.isEmpty())
			System.out.print(""+i.previous()+" ");
		System.out.println("");
	}
}
		
