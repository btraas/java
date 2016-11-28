package ca.bcit.comp2526.a2b;

import java.util.ArrayList;

/**
 * A type that takes up space. Examples are Life/Edible/Terrain
 * @author Brayden Traas
 * @version 1
 *
 */
public interface Matter {

    /**
     * Combines two Matter arrays into one.
     * @param one - first Array to combine
     * @param two - second Array to combine
     * @return - the combination of arrays.
     */
    static Matter[] combine(Matter[] one, Matter[] two) {
        ArrayList<Matter> all = new ArrayList<Matter>();
        for (int i = 0; i < one.length; i++) {
            all.add(one[i]);
        }
        for (int i = 0; i < two.length; i++) {
            all.add(two[i]);
        }
        Matter[] combined = new Matter[all.size()];
        for (int i = 0; i < all.size(); i++) {
            combined[i] = all.get(i);
        }
        return combined;
    }

}
