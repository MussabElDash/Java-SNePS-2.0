/**
 * @(#)ContextRUISSet.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/5/4
 */

package snip.Rules.DataStructures;

import java.util.Hashtable;

import SNeBR.Context;

public class ContextRUISSet {
	// Vector<ContextRUIS> crs;
	Hashtable<Integer, ContextRUIS> crs;

	// /**
	// * Create a new ContextRUISSet
	// */
	// public ContextRUISSet() {
	// // crs=new Vector<ContextRUIS>();
	// crs = new Hashtable<Integer, ContextRUIS>();
	// }

	/**
	 * Add a new ContextRUIS to the ContextRUISSet
	 * 
	 * @param c
	 *            ContextRUIS
	 */
	public void putIn(ContextRUIS c) {
		// crs.add(c);
		crs.put(c.getContext().getId(), c);
	}

	/**
	 * Return the number of ContextRUIS in the ContextRUISSet
	 * 
	 * @return int
	 */
	public int cardinality() {
		return crs.size();
	}

	// /**
	// * Return the index of the ContextRUIS in ContextRUISSet which have the
	// * context c if there are no ContextRUIS with the context c return -1
	// *
	// * @param c
	// * Context
	// * @return int
	// */
	// public int getIndex(Context c) {
	// for (int i = 0; i < crs.size(); i++) {
	// if (crs.get(i).getContext() == c) {
	// return i;
	// }
	// }
	// return -1;
	// }

	// /**
	// * Return the ContextRUIS number x in the ContextRUISSet
	// *
	// * @param x
	// * int
	// * @return ContextRUIS
	// */
	// public ContextRUIS getContextRUIS(int x) {
	// return crs.get(x);
	// }

	/**
	 * Return the ContextRUIS that is associated with the Context c
	 * 
	 * @param c
	 *            Context
	 * @return ContextRUIS
	 */
	public ContextRUIS getContextRUIS(Context c) {
		return crs.get(c.getId());
	}

	/**
	 * Checks if this ContextRUISSet has a ContextRUIS that is associated by the
	 * Context c
	 * 
	 * @param c
	 *            Context
	 * @return boolean
	 */
	public boolean hasContext(Context c) {
		return crs.containsKey(c.getId());
	}
}