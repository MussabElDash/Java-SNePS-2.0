/**
 * @className Cable.java
 * 
 * @ClassDescription A cable is a structure that allows traversing the SNePS 
 * 	network in both directions upwards and downwards. This is an abstract class 
 * 	and it is implemented as a 2-tuple (relation and node set).
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Cables;

import sneps.Relation;
import sneps.Nodes.NodeSet;

public abstract class Cable {
	
	/**
	 * The relation that labels the arcs included this cable.
	 */
	private Relation relation;
	
	/**
	 * The node set containing the nodes that are pointed to by
	 * 	the arcs of this cable.
	 */
	private NodeSet nodeSet;
	
	/**
	 * The constructor of this class
	 * 
	 * @param relation
	 * 			the relation that labels the arcs of this cable.
	 * @param ns
	 * 			the nodes included in this cable.
	 */
	public Cable(Relation relation, NodeSet ns){
		this.relation = relation;
		this.nodeSet = ns;
	}
	
	/**
	 * 
	 * @return the relation specified in this cable.
	 */
	public Relation getRelation(){
		return this.relation;
	}

	/**
	 * 
	 * @return the node set that contains the nodes included in this
	 * 	cable.
	 */
	public NodeSet getNodeSet(){
		return this.nodeSet;
	}
	
	/**
	 * This method overrides the default toString method inherited from the Object class.
	 */
	@Override
	public String toString(){
		return this.relation.toString()+" "+this.nodeSet.toString();
	}

}
