
/**
 * Rolls a die
 * 
 * @author Brayden Traas
 * @version 1
 */

import java.util.Random;

public class DieRoll
{
    private int result;
    public static final int DEFAULT_DIE_SIDES = 6;
    
    public DieRoll()
    {
        Random die = new Random();
        result = die.nextInt(DEFAULT_DIE_SIDES) + 1; // from 1 to # sides
    }
    
    public DieRoll(int sides)
    {
        Random die = new Random();
        result = die.nextInt(sides - 1) + 1;
    }
    
    public int value()
    {
        return result;
    }
}
