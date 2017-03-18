
/**
 * Helper functions here for terminal
 * 
 * @author Brayden
 * @version 1
 */
import java.util.Scanner; // Terminal input
import java.lang.Thread;  // Sleep method
public class Terminal
{
    static final int TERMINAL_LINES = 50;
    static final double MILLISECONDS_PER_SECOND = 1000.0;
    static final double DEFAULT_SLEEP = 1.0;
    
    static int CHAR_DELAY_MS    = 0;
    static double NEWLINE_DELAY = 0.0;
    static double CLEAR_DELAY   = 0.0;
    
    static void clear()
    {
        newLine(TERMINAL_LINES);
    }
    static void newLine()
    {
        newLine(1);
    }
    static void newLine(int lines)
    {
        for(int i = 0; i < lines; i++)
        {
            sleep(CLEAR_DELAY);
            System.out.println("");
        }
    }
    static void println(String in)
    {
        sleep(NEWLINE_DELAY);
        print(in);
        System.out.println();
    }
    static void println(int in)
    {
        sleep(NEWLINE_DELAY);
        print(in);
        System.out.println();
    }
    static void println(char in)
    {
        sleep(NEWLINE_DELAY);
        print(in);
        System.out.println();
    }
    static void print(String in)
    {
       if(CHAR_DELAY_MS > 0)
       {
           int length = in.length();
           for(int i = 0; i < length; i++)
           {
               sleep(CHAR_DELAY_MS);
               System.out.print(in.charAt(i));
           } 
       }
       else System.out.print(in);
    }
    static void print(int in)
    {
        print(Integer.toString(in));
    }
    static void print(char in)
    {
        print(String.valueOf(in));
    }
    static void getEnter()
    {
        Scanner sc = new Scanner(System.in);
        if(sc.hasNextLine()) return;
    }
    static void getEnter(String prompt)
    {
        println(prompt);
        getEnter();
    }
    static String getInput() // raw input, returns System.in
    {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext())
        {
            String input = sc.next();
            return input;
        }
        return null;
    }
    static String getInput(String prompt)
    {
        println(prompt);
        return getInput();
    }
    static String getInputUC()
    {
        return getInput().toUpperCase();
    }
    static String getInputUC(String prompt)
    {
        return getInput(prompt).toUpperCase();
    }
    static String getInputLC()
    {
        return getInput().toLowerCase();
    }
    static String getInputLC(String prompt)
    {
        return getInput(prompt).toLowerCase();
    }
    
    static String getAlpha()
    {
        return Convert.alpha(getInput());
    }
    static String getAlpha(String prompt)
    {
        return Convert.alpha(getInput(prompt));
    }
    static String getAlphaUC()
    {
        return Convert.alphaUC(getInput());
    }
    static String getAlphaUC(String prompt)
    {
        return Convert.alphaUC(getInput(prompt));
    }
    static String getAlphaLC()
    {
        return Convert.alphaLC(getInput());
    }
    static String getAlphaLC(String prompt)
    {
        return Convert.alphaLC(getInput(prompt));
    }
    
    static String getNumeric()
    {
        return Convert.numeric(getInput());
    }
    static String getNumeric(String prompt)
    {
        return Convert.numeric(getInput(prompt));
    }
    static String getNumeric(boolean requireNumber)
    {
        if(requireNumber == false) return getNumeric();
        else
        {
            String num = getNumeric();
            while(num.length() < 1)
            {
                num = getNumeric("Please enter a number!");
            }
            return num;
        }
    }
    static String getNumeric(String prompt, boolean requireNumber)
    {
        if(requireNumber == false) return getNumeric(prompt);
        else
        {
            println(prompt);
            return getNumeric(true);
        }
    }
    
    static int getInt()
    {
        String num = getNumeric(true);
        return Integer.parseInt(num);
    }
    static int getInt(String prompt)
    {
        return Integer.parseInt(getNumeric(prompt, true));
    }
    
    
    static String getAlNum() // get alphanumeric input
    {
        return Convert.alNum(getInput());
    }
    static String getAlNum(String prompt)
    {
        return Convert.alNum(getInput(prompt));
    }
    static String getAlNumUC()
    {
        return Convert.alNumUC(getInput());
    }
    static String getAlNumUC(String prompt)
    {
        return Convert.alNumUC(getInput(prompt));
    }
    static String getAlNumLC()
    {
        return Convert.alNumLC(getInput());
    }
    static String getAlNumLC(String prompt)
    {
        return Convert.alNumLC(getInput(prompt));
    }
    
    
    static boolean confirm() // Like JavaScript's confirm() method/function
    {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext())
        {
            String input = sc.next();
            input = input.toUpperCase();
            if(input.equals("Y") || input.equals("T") || input.equals("YES")) return true;
            else if(input.equals("N") || input.equals("F") || input.equals("NO")) return false;
            else
            {
                println("Unknown input. Try again!");
                return confirm(); // recursive to try again
                
            }
        }
        return confirm(); // beware of infinite loops!!
    }
    static boolean confirm(String str)
    {
        println(str+" Y/N");
        return confirm();
    }
    static void sleep(long ms)
    {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {}
        return;
    }
    static void sleep(double seconds)
    {
        double ms = seconds * MILLISECONDS_PER_SECOND;
        sleep((long) ms);
    }
    static void sleep(int ms)
    {
        sleep((long) ms);
    }
    static void sleep()
    {
        sleep(DEFAULT_SLEEP);
    }
}
