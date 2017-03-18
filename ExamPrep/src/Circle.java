abstract class Shape {
	void draw(){
		System.out.println("no shape yet");
	}
	Shape(){
		System.out.println("creating shape");
		draw();
		System.out.println("finished drawing shape");
	}
}
public class Circle extends Shape {
	int radius = 1;
	String s = "DEFAULT STRING";
	Circle(int r){
		radius = r;
		System.out.println(s + " Circle has radius = "+radius);
	}
	void draw(){
		System.out.println(s + " Draw Circle, radius = "+radius);
	}
	public static void main(String[] args){
		new Circle(5);
	}
}
