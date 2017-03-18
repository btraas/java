import java.util.ArrayList;

class Grain {
	public String toString() { return "Grain"; }
}


class CovariantReturn {

	public static double sum( ArrayList<Number> list) {
		double total = 0;
		
		for(Number element : list) {
			total += element.doubleValue();
		}
		return total;
	}
	

}