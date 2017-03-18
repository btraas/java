
/**
 * Generic tools
 * 
 * @author Brayden
 * @version 1
 */


import java.util.Random;
public class Tools
{
   /**
    * @param String input to check if empty
    * @return true if empty
    */
    public static boolean empty(String in)
    {
        if(in == null) return true;
        else if(in.length() == 0) return true;
        else if(in.toUpperCase().equals("FALSE")) return true;
        else if(Convert.numeric(in).length() > 0 && Integer.parseInt(in) == 0) return true;
        else return false;
    }
    
    /**
    * @param int input to check if empty
    * @return true if empty
    */
    public static boolean empty(int in)
    {
        if(in == 0) return true;
        else return false;
    }
    
    /**
    * @param Double input to check if empty
    * @return true if empty
    */
    public static boolean empty(double in)
    {
        if(in == 0.0) return true;
        else return false;
    }
    
    /**
    * @param Float input to check if empty
    * @return true if empty
    */
    public static boolean empty(float in)
    {
        if(in == 0.0) return true;
        else return false;
    }
    
    /**
    * @param boolean input to check if empty (false)
    * @return true if empty
    * 
    * Just in case a user uses empty() as a catch-all regardless of type.
    */
    public static boolean empty(boolean in)
    {
        if(in == false) return true;
        else return false;
    }
    // let's forget about chars for now.
    
    /**
     * @param int, int. Odds are 'int 1' in 'int 2'
     * @return random chance output with given odds (boolean)
     */
    public static boolean getChances(int i, int j) // Odds are 'i' in 'j'
    {
        Random r = new Random();
        int num = r.nextInt(j) + 1;
         
        if(num <= i)    return true;
        else            return false;
        
    }
    
    /**
     * @param String to pad, int output length
     * @return padded string
     */
    public static String padR(String s, int n)
    {
        for(int i = 0; s.length() < n; i++)
        {
            s += " ";
        }
        return  s;
    }
    
    /**
     * @param String to pad, int output length
     * @return padded string
     */
    public static String padL(String s, int n)
    {
        for(int i = 0; s.length() < n; i++)
        {
            s = " " + s;
        }
        return s;
    }
    
    /**
     * @param String to pad, int output length
     * @return padded string
     */
    public static String padLR(String s, int n)
    {
        String padL = "";
        String padR = "";
        for(int i = 0; s.length() < n; i++)
        {
            s = " " + s;
            if(s.length() < n) s += " ";
        }
        return s;
    }
    
}
