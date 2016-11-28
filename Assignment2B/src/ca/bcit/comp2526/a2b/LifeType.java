package ca.bcit.comp2526.a2b;


import java.awt.Color;


/**
 * An Enum that defines rules for a LifeType.
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public enum LifeType implements Edible {

    // To add a LifeType, we need to add it here and in
    //  LifeTools.Type, so they can refer to each other.


    // Reproduction = min, max, neighbor species, neighbor empties, neighbor food
    
    PLANT(      Color.GREEN,            // Base color
                10,                     // Turns before death
                new LifeTools.MoveStats(0, 0, 0),   // Moves 0-0, senses 0
                new LifeTools.ReproductionStats(1, 2, (World.HEX ? 1 : 3), (World.HEX ? 1 : 2), 0),
                null,                               // No food
                new LifeTools.IncompatibleArray(    // Any "difficult" terrain
                        Terrain.getDifficults() ),
                new LifeTools.AvoidArray()),    // Don't avoid anything 

    HERBIVORE(  Color.YELLOW,
                6,
                new LifeTools.MoveStats(1, 1, 2),
                new LifeTools.ReproductionStats(1, 2, 1, 
                        (World.HEX ? 1 : 2), (World.HEX ? 2 : 2)),
                new Edible[] {  LifeTools.Type.PLANT, Terrain.WATER }, 
                new LifeTools.IncompatibleArray( new Matter[] { 
                    LifeTools.Type.HERBIVORE, 
                    LifeTools.Type.CARNIVORE, 
                    LifeTools.Type.OMNIVORE }),
                new LifeTools.AvoidArray( new Matter[] { 
                    LifeTools.Type.CARNIVORE, 
                    LifeTools.Type.OMNIVORE })),
                        
    CARNIVORE(  Color.RED, 
                3,
                new LifeTools.MoveStats(1, 2, (World.HEX ? 6 : 4)),
                new LifeTools.ReproductionStats(1, 2, 1, (World.HEX ? 2 : 2), 2),
                new Edible[] {  LifeTools.Type.HERBIVORE, LifeTools.Type.OMNIVORE },
                new LifeTools.IncompatibleArray(Matter.combine(
                        Terrain.getDifficults(),
                        new Matter[] { LifeTools.Type.CARNIVORE })),
                new LifeTools.AvoidArray()
                ),
    
    OMNIVORE(   Color.MAGENTA, 
                4,  
                new LifeTools.MoveStats(1, 1, (World.HEX ? 2 : 3)),
                new LifeTools.ReproductionStats(1, (World.HEX ? 1 : 2), 1, 
                        (World.HEX ? 1 : 3), (World.HEX ? 2 : 3)),
                new Edible[] {  LifeTools.Type.PLANT, 
                                LifeTools.Type.CARNIVORE, 
                                Terrain.WATER },
                new LifeTools.IncompatibleArray(new Matter[] { LifeTools.Type.OMNIVORE }),
                new LifeTools.AvoidArray());
                

    
    protected final Color color;
    protected final int initFood;
    
    protected final LifeTools.MoveStats move;
    protected final LifeTools.ReproductionStats reproduction;
    
    private final Edible[] foodTypes;
    private final LifeTools.IncompatibleArray incompatibleTypes;
    private final LifeTools.AvoidArray avoid;
    
    private LifeType(   final Color color, 
                        int initFood, 
                        final LifeTools.MoveStats moveStats,
                        final LifeTools.ReproductionStats reproductionStats,
                        final Edible[] foodTypes, 
                        final LifeTools.IncompatibleArray incompatible,
                        final LifeTools.AvoidArray avoid) {
            
        
        this.color = color;
        this.initFood = initFood;
        this.move = moveStats;
        this.reproduction = reproductionStats;
        this.foodTypes = foodTypes;
        this.incompatibleTypes = incompatible;
        this.avoid = avoid;
    }
    
    private static final Matter[] simpleToComplex(Matter[] in) {
        if (in == null) {
            return null;// new Matter[]{};
        }
        for (int i = 0; i < in.length; i++) {
            if (in[i] instanceof LifeTools.Type) {
                LifeTools.Type simple = (LifeTools.Type)in[i];
                in[i] = LifeTools.getLifeType(simple);
            }
        }
        return in;
    }
    
    /**
     * Gets the food types
     * @return an array of Edible types.
     */
    public Edible[] getFoodTypes() {    
        return (Edible[])simpleToComplex(foodTypes);
    }
    
    /**
     * Gets the Incompatible types.
     * @return an array of Matter types
     */
    public Matter[] getIncompatibleTypes() {
        return simpleToComplex(incompatibleTypes.data);
    }
    
    /**
     * Gets the Avoidable types.
     * @return an array of Avoidable Matter
     */
    public Matter[] getAvoid() {
        return simpleToComplex(avoid.data);
    }
    
    
       
        
        
}
