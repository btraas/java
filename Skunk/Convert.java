
/**
 * String helpers
 * 
 * @author Brayden Traas 
 * @version 1
 */
public class Convert
{
    /**
     * @param input string to convert
     * @return output alphanumeric string
     */
    public static String alNum(String in) // alphanumeric out
    {
        return in.replaceAll("^A-Za-z0-9", "");
    }
    
    /**
     * @param input string to convert
     * @return Uppercase Alphanumeric string
     */
    public static String alNumUC(String in)
    {
        return alNum(in).toUpperCase();
    }
    
    /**
     * @param input string to convert
     * @return Lowercase Alphanumeric
     */
    public static String alNumLC(String in)
    {
        return alNum(in).toLowerCase();
    }
    
    /**
     * @param input string to convert
     * @return A-Z characters
     */
    public static String alpha(String in)
    {
        return in.replaceAll("^A-Za-z", "");
    }
    
    /**
     * @param input string to convert
     * @return A-Z characters in uppercase
     */
    public static String alphaUC(String in)
    {
        return alpha(in).toUpperCase();
    }
    
    /**
     * @param input string to convert
     * @return Lowercase A-Z
     */
    public static String alphaLC(String in)
    {
        return alpha(in).toLowerCase();
    }
    
    /**
     * @param input string to convert
     * @return Numeric chars only
     */
    public static String numeric(String in)
    {
        return in.replaceAll("^0-9", "");
    }
    
}
