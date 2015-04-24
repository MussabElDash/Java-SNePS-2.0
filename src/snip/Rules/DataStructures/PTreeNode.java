/**
 * @(#)TreeNode.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/24
 */

package snip.Rules.DataStructures;

import java.util.Set;

public class PTreeNode {
	private RuleUseInfoSet ruis;
	private Set<Integer> pats;
	private Set<Integer> vars;
	private PTreeNode up;
	private PTreeNode right;
	private PTreeNode left;

	/**
	 * Create a new subtree with the parent up (if up is null then this node is
	 * the root)
	 * 
	 * @param p
	 *            list of patterns ids
	 * @param d
	 *            list of directions
	 * @param up
	 *            TreeNode
	 */
	public PTreeNode(Set<Integer> p, Set<Integer> v) {
		ruis = new RuleUseInfoSet();
		pats = p;
		vars = v;
		up = null;
		right = null;
		left = null;
	}

	/**
	 * Add rui to the rule use info set of this node
	 * 
	 * @param rui
	 *            RuleUseInfo
	 */
	public void insertRUI(RuleUseInfo rui) {
		ruis.putIn(rui);
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
	 * Return the rule use info set
	 * 
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet getRUIS() {
		return ruis;
	}

	/**
	 * Return the parent of this node
	 * 
	 * @return TreeNode
	 */
	public PTreeNode getParent() {
		return up;
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
	 * Create the right child
	 * 
	 * @param v
	 *            list of variables ids
	 * @param d
	 *            list of directions
	 */
	public void insertRight(PTreeNode tn) {
		right = tn;
		tn.up = this;
	}

	/**
	 * Create the left child
	 * 
	 * @param v
	 *            list of variables ids
	 * @param d
	 *            list of directions
	 */
	public void insertLeft(PTreeNode tn) {
		left = tn;
		tn.up = this;
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

	@Override
	public String toString() {
		return pats.toString();
	}

}