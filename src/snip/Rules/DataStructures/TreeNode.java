/**
 * @(#)TreeNode.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/24
 */

package snip.Rules.DataStructures;

public class TreeNode {
	private RuleUseInfoSet ruis;
	private int[] pats;
	private int[] vars;
	private boolean[] dir;
	private TreeNode up;
	private TreeNode right;
	private TreeNode left;

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
	public TreeNode(int[] p, int[] v, boolean[] d) {
		ruis = new RuleUseInfoSet();
		pats = p;
		vars = v;
		dir = d;
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
		for (int i = 0; i < ruis.cardinality(); i++)
			this.ruis.putIn(ruis.getRuleUseInfo(i));
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
	public TreeNode getParent() {
		return up;
	}

	/**
	 * Return the right child of this node
	 * 
	 * @return
	 */
	public TreeNode getRight() {
		return right;
	}

	/**
	 * Return the left child of this node
	 * 
	 * @return
	 */
	public TreeNode getLeft() {
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
	public void insertRight(TreeNode tn) {
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
	public void insertLeft(TreeNode tn) {
		left = tn;
		tn.up = this;
	}

	/**
	 * Return the list of patterns
	 * 
	 * @return int[]
	 */
	public int[] getPats() {
		return pats;
	}

	/**
	 * Return the list of directions
	 * 
	 * @return boolean[]
	 */
	public boolean[] getDir() {
		return dir;
	}

	/**
	 * Return the direction of a pattern
	 * 
	 * @param pat
	 *            int
	 * @return boolean
	 */
	public boolean getPatDir(int pat) {
		int i = 0;
		for (; i < pats.length; i++) {
			if (pat == pats[i])
				break;
		}
		return dir[i];
	}

	/**
	 * Return the list of variables
	 * 
	 * @return
	 */
	public int[] getVars() {
		return vars;
	}

}