public class Wrapping {
  private int i;
  public Wrapping(int x) { i = x; }
  public int value() { return i; }
  
  public static void main(String[] args) {
	  System.out.println( (new Parcel()).wrapping(10).value());
  }
}

// Equivalent
class Wrapping2 extends Wrapping {
	
	Wrapping2(int x) {
		super(x);
	}
	
	public int value () { 
		return 47*super.value();
	}
  }

class Parcel {
  public Wrapping wrapping(int x) {
    //return an anonymous inner class object of Wrapping type
   //with overloaded method “public int value (){ return 47*i;}”
	
	  return new Wrapping(x) {
		public int value () { 
			return 47*super.value();
		}
	  };

  }
  
}
  