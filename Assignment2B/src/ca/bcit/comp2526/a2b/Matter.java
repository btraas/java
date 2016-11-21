package ca.bcit.comp2526.a2b;

import java.util.ArrayList;

/**
 * A type that takes up space.
 * @author Brayden Traas
 * @version 1
 *
 */
public interface Matter {

	
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
