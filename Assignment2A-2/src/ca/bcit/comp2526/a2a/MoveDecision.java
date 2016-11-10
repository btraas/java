package ca.bcit.comp2526.a2a;

import java.util.Random;

/**
 * <p>A MoveDecision.</p>
 * 
 * <p>This is a basic Decision-making object.
 * Makes moves at random given a seed and
 * possible options.</p>
 * 
 * <p>This Class is NOT type-dependent; Having fun with parameterized types :) </p>
 * 
 * <p>Any use of MoveDecision or subclass must provide a type - eg. "Cell".
 *  This is the "type" of the move destination.</p>
 * 
 * <p>Override this Class to implement 'smarter'
 * decisions.</p>
 *  
 * @author Brayden Traas
 * @version 2016-10-22
 */
public class MoveDecision<DestinationT> {
    
    protected final Random seed;
  
    protected DestinationT[] options;
    
    /**
     * Creates a new MoveDecision.
     * @param seed to choose Randomly from.
     * @param options to choose form.
     */
    public MoveDecision(final Random seed, final DestinationT[] options) {
        this.options = options;
        this.seed = seed;
    }
  
    /**
     * Decide a Cell from the options.
     * @return a Cell that's decided.
     */
    public DestinationT decide() {
        return decide(true);
    }
    
    /**
     * Use overloading to be able to override 
     *  a middle-man overridden decide() method.
     * @param forceRandom if true, force an ultimately Random decision
     * @return an Object that's decided
     */
    public DestinationT decide(boolean forceRandom) {
        
        if (!forceRandom) {
            return decide();
        }
      
        if ( options.length == 0 ) {
            return null;
        }
  
        //int random = (new Random()).nextInt(options.length);
        
        int random = seed.nextInt(options.length);
        return options[random];
    }
  
}
