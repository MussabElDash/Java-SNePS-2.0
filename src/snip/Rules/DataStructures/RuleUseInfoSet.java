/**
 * @(#)RuleUseInfoSet.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/6
 */
package snip.Rules.DataStructures;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RuleUseInfoSet extends RuisHandler implements
		Iterable<RuleUseInfo> {
	// private Vector<RuleUseInfo> ruis;
	private Set<RuleUseInfo> ruis;
	private boolean singleton;

	// private VectorHashSet<RuleUseInfo> ruis;

	/**
	 * Create a new empty rule use info set for general use
	 */
	public RuleUseInfoSet() {
		super(-1);
		// ruis = new Vector<RuleUseInfo>();
		// ruis = new VectorHashSet<RuleUseInfo>();
		ruis = new HashSet<RuleUseInfo>();
	}

	/**
	 * Create a new empty rule use info set for ContextRUIS use
	 */
	public RuleUseInfoSet(int contextID, boolean singleton) {
		super(contextID);
		// ruis = new Vector<RuleUseInfo>();
		// ruis = new VectorHashSet<RuleUseInfo>();
		ruis = new HashSet<RuleUseInfo>();
		this.singleton = singleton;
	}

	/**
	 * Check is the rule use info set is new or not
	 * 
	 * @return true if new otherwise false
	 */
	public boolean isEmpty() {
		return ruis.isEmpty();
	}

	/**
	 * Add r to the rule use info set
	 * 
	 * @param r
	 *            rule use info
	 */
	public void add(RuleUseInfo r) {
		ruis.add(r);
	}

	// /**
	// * Return the index of r in the rule use info set, if r is not in the rule
	// * use info set return -1 (used in Sindexing only)
	// *
	// * @param r
	// * RuleUseInfo
	// * @return int
	// */
	// public int getIndex(RuleUseInfo r) {
	// for (int i = 0; i < ruis.size(); i++) {
	// throw new UnsupportedOperationException();
	// // if (r.getSub().isEqual(rs.get(i).getSub())) {
	// // return i;
	// // }
	// }
	// return -1;
	// }
	//
	// /**
	// * Return the rule use info number x in the rule use info set
	// *
	// * @param x
	// * int
	// * @return RuleUseInfo
	// */
	// public RuleUseInfo getRuleUseInfo(int x) {
	// return ruis.get(x);
	// }

	// /**
	// * Remove the rule use info number x from the rule use info set
	// *
	// * @param x
	// * int
	// */
	// public void remove(int x) {
	// ruis.remove(x);
	// }

	/**
	 * Removes RuleUseInfo rui from the set and returns it
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @return RuleUseInfo
	 */
	public RuleUseInfo remove(RuleUseInfo rui) {
		ruis.remove(rui);
		return rui;
	}

	/**
	 * Combine rui with the rule use info set
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet combine(RuleUseInfo rui) {
		RuleUseInfoSet res = new RuleUseInfoSet();
		// for (int i = 0; i < ruis.size(); i++) {
		// RuleUseInfo tmp = rui.combine(ruis.get(i));
		// if (tmp != null)
		// res.putIn(tmp);
		// }
		for (RuleUseInfo tRui : ruis) {
			RuleUseInfo tmp = rui.combine(tRui);
			if (tmp != null)
				res.add(tmp);
		}
		return res;
	}

	/**
	 * Combine ruis with the rule use info set
	 * 
	 * @param ruis
	 *            RuleUseInfoSet
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet combine(RuleUseInfoSet ruis) {
		RuleUseInfoSet res = new RuleUseInfoSet();
		// for (int i = 0; i < this.ruis.size(); i++) {
		// RuleUseInfoSet temp = ruis.combine(this.ruis.get(i));
		// for (int j = 0; j < temp.ruis.size(); j++) {
		// res.putIn(temp.ruis.get(j));
		// }
		// }
		for (RuleUseInfo rui : this.ruis) {
			RuleUseInfoSet temp = ruis.combine(rui);
			for (RuleUseInfo tRui : temp.ruis) {
				res.add(tRui);
			}
		}
		return res;
	}

	/**
	 * insert rui in the rule use info set when Sindexing and Ptree are not used
	 * and return the set of new rule use info set
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet combineAdd(RuleUseInfo rui) {
		RuleUseInfoSet temp = this.combine(rui);
		temp.add(rui);
		// for (int i = 0; i < temp.ruis.size(); i++) {
		// this.putIn(temp.ruis.get(i));
		// }
		for (RuleUseInfo tRui : temp.ruis) {
			this.add(tRui);
		}
		return this;
	}

	public RuleUseInfoSet insertRUI(RuleUseInfo rui) {
		if (!singleton)
			return combineAdd(rui);
		if (isEmpty())
			add(rui);
		else
			ruis = combine(rui).ruis;
		return this;
	}

	/**
	 * Return the number of rule use infos in this set
	 * 
	 * @return int
	 */
	public int cardinality() {
		return ruis.size();
	}

	@Override
	public Iterator<RuleUseInfo> iterator() {
		return ruis.iterator();
	}
}