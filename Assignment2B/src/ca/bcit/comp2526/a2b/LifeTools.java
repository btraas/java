package ca.bcit.comp2526.a2b;

/**
 * A Class of Tools and useful subclasses.
 * 
 * @author Brayden
 * @version 2016-11-22
 *
 */
public abstract class LifeTools {

    /*
     * Simple Type for LifeType enum to reference...
     */
    protected enum Type implements Edible {
        PLANT, HERBIVORE, OMNIVORE, CARNIVORE, NULL;
   
    }

    private LifeTools() {}
    
    protected static LifeType getLifeType(final LifeTools.Type type) {
        if (type == null) {
            return null;
        }
        return LifeType.valueOf(type.name());
    }
    
    
    /*
     * The following inner classes are to make the code in
     *  the LifeType enum easier to read.
     *  Instead of a string of numbers or or an array of Matter
     *  objects, a parameter like:
     *  
     *   Color.RED,
     *   3,
     *   new LifeTools.MoveStats(1, 2, 4),
     *   new LifeTools.ReproductionStats(1, 2, 1, 2, 2)
     *   
     *  is much easier to understand than:
     *   
     *   Color.RED,
     *   3,
     *   1, 2, 4, 
     *   1, 2, 1, 2, 2
     *   
     *  etc.
     */
    
    protected static class ReproductionStats {
        
        protected int minSpawn;
        protected int maxSpawn;
        
        protected int minNeighbors;
        protected int minEmpty;
        protected int minFood;
        
        protected ReproductionStats(int minSpawn, int maxSpawn, 
                int minNeighbors, int minEmpty, int minFood) {
            this.minSpawn = minSpawn;
            this.maxSpawn = maxSpawn;
            this.minNeighbors = minNeighbors;
            this.minEmpty = minEmpty;
            this.minFood = minFood;
        }
    }
    
    protected static class MoveStats {
        
        protected int min;
        protected int max;
        protected int sense;
        
        protected MoveStats(int min, int max, int sense) {
            if (max < min) {
                try {
                    throw new Exception("MAX > MIN!");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            this.min = min;
            this.max = max;
            this.sense = sense;
        }

    }
    
    protected abstract static class MatterArray {
        protected final Matter[] data;
        
        protected MatterArray() {
            this.data = new Matter[]{};
        }
        
        protected MatterArray(final Matter[] data) {
            this.data = data;
        }
    }
    
    protected static class AvoidArray extends MatterArray {
        
        protected AvoidArray(){}
        
        protected AvoidArray(final Matter[] data) {
            super(data);
        }
    }
    
    protected static class IncompatibleArray extends MatterArray {
        
        protected IncompatibleArray(){}
        
        protected IncompatibleArray(final Matter[] data) {
            super(data);
        }
    }
    

}
