/**
 * @(#)ContextRUISSet.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/5/4
 */

package snip.Rules.DataStructures;

import java.util.Hashtable;

public class ContextRuisSet {
	// Vector<ContextRUIS> crs;
	Hashtable<Integer, RuisHandler> crs;

	/**
	 * Create a new ContextRUISSet
	 */
	public ContextRuisSet() {
		crs = new Hashtable<Integer, RuisHandler>();
	}

	/**
	 * Add a new ContextRUIS to the ContextRUISSet
	 * 
	 * @param c
	 *            ContextRUIS
	 */
	public void putIn(RuisHandler c) {
		// crs.add(c);
		crs.put(c.getContext(), c);
	}

	/**
	 * Return the number of ContextRUIS in the ContextRUISSet
	 * 
	 * @return int
	 */
	public int cardinality() {
		return crs.size();
	}

	/**
	 * Return the ContextRUIS that is associated with the Context c
	 * 
	 * @param c
	 *            Context
	 * @return ContextRUIS
	 */
	public RuisHandler getContextRUIS(int contextID) {
		return crs.get(contextID);
	}

	/**
	 * Checks if this ContextRUISSet has a ContextRUIS that is associated by the
	 * Context c
	 * 
	 * @param c
	 *            Context
	 * @return boolean
	 */
	public boolean hasContext(int contextID) {
		return crs.containsKey(contextID);
	}

	/**
	 * Clears the ContextRUISet
	 */
	public void clear() {
		crs.clear();
	}
}
