/**
 * @(#)TreeNode.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/24
 */

package snip.Rules.DataStructures;

import java.util.Hashtable;
import java.util.Set;

import snip.Substitutions;

public class PTreeNode {
	private Hashtable<Integer, RuleUseInfoSet> ruisMap;
	private Set<Integer> pats;
	private Set<Integer> vars;
	private Set<Integer> intersectionWithSibling;
	private PTreeNode parent;
	private PTreeNode right;
	private PTreeNode left;
	private PTreeNode sibling;

	/**
	 * Create a new subtree with the parent up (if up is null then this node is
	 * the root)
	 * 
	 * @param p
	 *            list of patterns ids
	 * @param d
	 *            list of directions
	 * @param parent
	 *            TreeNode
	 */
	public PTreeNode(Set<Integer> p, Set<Integer> v) {
		ruisMap = new Hashtable<Integer, RuleUseInfoSet>();
		pats = p;
		vars = v;
		parent = null;
		right = null;
		left = null;
	}

	public void insertIntoTree(RuleUseInfo rui, RuleUseInfoSet set) {
		int key = insertRUI(rui);
		// Since it has no sibling, it has no parent, and it is the root
		if (sibling == null) {
			set.putIn(rui);
			return;
		}
		RuleUseInfoSet siblingSet = sibling.getRUIS(key);
		if (siblingSet == null)
			return;
		for (RuleUseInfo tRui : siblingSet) {
			RuleUseInfo combinedRui = rui.combine(tRui);
			if (combinedRui == null)
				continue;
			parent.insertIntoTree(combinedRui, set);
		}
	}

	/**
	 * Add rui to the rule use info set of this node and returns the key that
	 * was mapped to the rui
	 * 
	 * @param rui
	 *            RuleUseInfo
	 *
	 * @return int
	 *
	 */
	public int insertRUI(RuleUseInfo rui) {
		// Since it has no sibling, it has no parent, and it is the root
		if (sibling == null) {
			RuleUseInfoSet ruis = ruisMap.get(0);
			if (ruis == null) {
				ruis = new RuleUseInfoSet();
				ruisMap.put(0, ruis);
			}
			ruis.putIn(rui);
			return 0;
		}
		// The upper part of this method is for incase this node is aroot node
		// and the lower part for other nodes in the tree
		int[] ids = new int[intersectionWithSibling.size()];
		Substitutions subs = rui.getSub();
		for (int id : intersectionWithSibling) {
			// TODO fill ids with the vars in the rui
		}
		int key = getKey(ids);
		RuleUseInfoSet ruis = ruisMap.get(key);
		if (ruis == null) {
			ruis = new RuleUseInfoSet();
			ruisMap.put(key, ruis);
		}
		ruis.putIn(rui);
		return key;
	}

	/**
	 * Add ruis to the rule use info set of this node
	 * 
	 * @param ruis
	 *            RuleUseInfoSet
	 */
	public void insertRUIS(RuleUseInfoSet ruis) {
		// for (int i = 0; i < ruis.cardinality(); i++)
		// this.ruis.putIn(ruis.getRuleUseInfo(i));
		for (RuleUseInfo rui : ruis) {
			this.insertRUI(rui);
		}
	}

	/**
	 * Return the rule use info set that maps to set of variables'
	 * substitutions' key
	 * 
	 * @param index
	 *            int
	 * 
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet getRUIS(int index) {
		return ruisMap.get(index);
	}

	/**
	 * Return the parent of this node
	 * 
	 * @return TreeNode
	 */
	public PTreeNode getParent() {
		return parent;
	}

	/**
	 * Return the right child of this node
	 * 
	 * @return
	 */
	public PTreeNode getRight() {
		return right;
	}

	/**
	 * Return the left child of this node
	 * 
	 * @return
	 */
	public PTreeNode getLeft() {
		return left;
	}

	/**
	 * Return the sibling of this node
	 * 
	 * @return
	 */
	public PTreeNode getSibling() {
		return sibling;
	}

	// /**
	// * Create the right child
	// *
	// * @param tn
	// * PTreeNode that to be inserted as a right child
	// */
	// public void insertRight(PTreeNode tn) {
	// right = tn;
	// tn.parent = this;
	// }
	//
	// /**
	// * Create the left child
	// *
	// * @param tn
	// * PTreeNode that to be inserted as a left child
	// */
	// public void insertLeft(PTreeNode tn) {
	// left = tn;
	// tn.parent = this;
	// }

	/**
	 * insert a left and right children
	 * 
	 * @param leftNode
	 *            PTreeNode that to be inserted as a left child
	 * @param rightNode
	 *            PTreeNode that to be inserted as a right child
	 * @param intersection
	 */
	public void insertLeftAndRight(PTreeNode leftNode, PTreeNode rightNode,
			Set<Integer> intersection) {
		leftNode.parent = this;
		rightNode.parent = this;
		leftNode.sibling = rightNode;
		rightNode.sibling = leftNode;
		leftNode.intersectionWithSibling = intersection;
		rightNode.intersectionWithSibling = intersection;
		left = leftNode;
		right = rightNode;
	}

	/**
	 * Return the list of patterns
	 * 
	 * @return int[]
	 */
	public Set<Integer> getPats() {
		return pats;
	}

	/**
	 * Return the list of variables
	 * 
	 * @return
	 */
	public Set<Integer> getVars() {
		return vars;
	}

	/**
	 * Get the index of a RuleUseInfo in the table by the id's of its
	 * substitutions
	 * 
	 * @param x
	 *            int[]
	 * @return int
	 */
	private int getKey(int[] x) {
		int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < x.length; ++i) {
			hash += (hash ^ x[i]) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		return hash;
	}

	@Override
	public String toString() {
		return pats.toString();
	}

}