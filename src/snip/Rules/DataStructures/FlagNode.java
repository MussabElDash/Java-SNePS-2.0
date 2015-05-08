/**
 * @(#)FlagNode.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/12
 */
package snip.Rules.DataStructures;

import SNeBR.Support;
import sneps.Nodes.Node;

public class FlagNode {

	private Node node;
	private Support support;
	private int flag;

	/**
	 * Create a new flag node
	 * 
	 * @param n
	 *            node
	 * @param s
	 *            support
	 * @param f
	 *            true or false
	 */
	public FlagNode(Node n, Support s, int f) {
		node = n;
		support = s;
		flag = f;
	}

	/**
	 * Return the node of the flag node
	 * 
	 * @return Node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Return the support of the flag node
	 * 
	 * @return support
	 */
	public Object getSupport() {
		return support;
	}

	/**
	 * Return the flag of the flag node (1 is true, 2 is false, 3 is unknown and
	 * 4 is requested)
	 * 
	 * @return Node
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * Check if this and fn are equal
	 * 
	 * @param fn
	 *            flag node
	 * @return true or false
	 */
	public boolean isEqual(FlagNode fn) {
		return fn.node == node && fn.support == support && fn.flag == flag;
	}

	/**
	 * Set the value of the flag to x
	 * 
	 * @param x
	 *            int
	 */
	public void setFlag(int x) {
		flag = x;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (!(obj instanceof FlagNode))
	// return false;
	// FlagNode fn = (FlagNode) obj;
	// return isEqual(fn);
	// }
}