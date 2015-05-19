/**
 * @className NodeSet.java
 * 
 * @ClassDescription This class represents a set of nodes. It's implemented using
 * 	a linked list but no node can exist more than once in the same node set. 
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Nodes;

import java.util.Iterator;
import java.util.Vector;

public class NodeSet implements Iterable<Node> {

	/**
	 * a list that stores the nodes included in the node set.
	 */
	private Vector<Node> nodes;

	/**
	 * The constructor of the class which initialize an empty node set.
	 */
	public NodeSet() {
		nodes = new Vector<Node>();
	}

	/**
	 * 
	 * @param index
	 *            the index of the node.
	 * 
	 * @return the node at the specified index.
	 */
	public Node getNode(int index) {
		return this.nodes.get(index);
	}

	/**
	 * 
	 * @param node
	 *            the node that will be added to the node set (only if doesn't
	 *            already exist in this node set).
	 */
	public void addNode(Node node) {
		if (!this.nodes.contains(node))
			this.nodes.add(node);
	}

	/**
	 * 
	 * @return the size (number of nodes) in the current node set.
	 */
	public int size() {
		return this.nodes.size();
	}

	/**
	 * 
	 * @param nodeSet
	 *            a given node set that all its elements (nodes) will be added
	 *            to the current node set.
	 */
	public void addAll(NodeSet nodeSet) {
		for (int i = 0; i < nodeSet.size(); i++) {
			this.addNode(nodeSet.getNode(i));
		}
	}

	/**
	 * 
	 * @param node
	 *            a node that will be removed from the current node set.
	 */
	public void removeNode(Node node) {
		this.nodes.remove(node);
	}

	/**
	 * This method removes all the nodes in the current node set.
	 */
	public void clear() {
		this.nodes.clear();
	}

	/**
	 * 
	 * @return true if the current node set is empty, and false otherwise.
	 */
	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}

	/**
	 * 
	 * @param node
	 *            a node that will be checked whether it exists in the current
	 *            node set.
	 * 
	 * @return true if the current node set contains the given node, and false
	 *         otherwise.
	 */
	public boolean contains(Node node) {
		return this.nodes.contains(node);
	}

	/**
	 * 
	 * @param ns
	 *            a given node set.
	 * 
	 * @return a new node set that contains the union of the nodes in the given
	 *         node set and the nodes in the current node set.
	 */
	public NodeSet Union(NodeSet ns) {
		NodeSet unionSet = new NodeSet();
		unionSet.addAll(this);
		unionSet.addAll(ns);
		return unionSet;
	}

	/**
	 * 
	 * @param ns
	 *            a given node set.
	 * 
	 * @return a new node set that contains the intersection between the nodes
	 *         in the given node set and the nodes in the current node set.
	 */
	public NodeSet Intersection(NodeSet ns) {
		NodeSet intersectionSet = new NodeSet();
		for (int i = 0; i < ns.size(); i++) {
			if (this.contains(ns.getNode(i)))
				intersectionSet.addNode(ns.getNode(i));
		}
		return intersectionSet;
	}

	/**
	 * 
	 * @param ns
	 *            a given node set.
	 * @return a new node set that contains the nodes that exist in the current
	 *         node set but do not exist the given node set.
	 */
	public NodeSet difference(NodeSet ns) {
		NodeSet differenceSet = new NodeSet();
		for (int i = 0; i < this.size(); i++) {
			if (!ns.contains(this.getNode(i)))
				differenceSet.addNode(this.getNode(i));
		}
		return differenceSet;
	}

	/**
	 * This method overrides the default equals method inherited from the Object
	 * class.
	 * 
	 * @param obj
	 *            an Object that is to be compared to the current node set to
	 *            check whether they are equal.
	 * 
	 * @return true if the given object is an instance of the node set class and
	 *         has exactly the same elements (nodes) as the current node set
	 */
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().getSimpleName().equals("NodeSet"))
			return false;

		NodeSet nodeSet = (NodeSet) obj;
		if (this.nodes.size() != nodeSet.size())
			return false;
		for (int i = 0; i < this.nodes.size(); i++) {
			if (!nodeSet.contains(this.nodes.get(i)))
				return false;
		}
		return true;
	}

	@Override
	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	/**
	 * This method overrides the default toString method inherited from the
	 * Object class.
	 */
	@Override
	public String toString() {
		String s = "{";
		for (int i = 0; i < this.nodes.size(); i++) {
			s += this.nodes.get(i).toString();
			if (i < this.nodes.size() - 1)
				s += " ";
		}
		s += "}";
		return s;
	}

}
