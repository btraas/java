
/**
 * Main class for Guessing game. Includes UI and game processing
 * 
 * @author Brayden Traas
 * @version 1
 */

import java.util.Scanner;
import java.util.Random;

public class Game
{
    public Game() // starts a new game
    {
        boolean quit = false;
        Scanner sc = new Scanner(System.in);
        Random r = new Random();
        int answer = r.nextInt(10) + 1;
        
        while(quit == false)
        {
            System.out.println("Welcome to the Guessing Game. Please pick a number between 1 and 10, or Q to quit.");
            
            if (sc.hasNextInt())
            {
                int number = sc.nextInt();
                
                if (number < 0 || number > 10)
                {
                    System.out.println("Number must be betwen 1 and 10!");
                }
                else {
                    System.out.println("You entered: " + number + ". ");
                    if(number == answer)
                    {
                        System.out.println("Correct!");
                        answer = r.nextInt(10) + 1;
                    }
                    else
                    {
                        System.out.println("Incorrect!");
                    }
                }
            }
            else
            {
                String scWord = sc.next();
                
                if (scWord.equalsIgnoreCase("Q")) quit = true;
                else System.out.println("ERROR! NaN: " + scWord);
            }
        }
        
        System.out.println("Thanks for playing! BYE!");
     }
	 
	 public static void main(String[] args) {
		 new Game();
	 }
}
