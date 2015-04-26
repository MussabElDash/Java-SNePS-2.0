/**
 * @className UpCable.java
 * 
 * @ClassDescription An up-cable is a structure that enables traversing the network upwards. 
 * 	This class extends the cable class and has no variables than those inherited from the super
 * 	class. 
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Cables;

import sneps.Relation;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;

public class UpCable extends Cable {

	/**
	 * The constructor of this class.
	 * 
	 * @param relation
	 * 			the relation that labels the arcs of this up cable.
	 */
	public UpCable(Relation relation) {
		super(relation, new NodeSet());	
	}
	
	/**
	 * 
	 * @param node
	 * 			a node that will be added to the current up cable. Nodes can 
	 * 			be added or removed from the up cable after its creation.
	 */
	public void addNode(Node node){
		this.getNodeSet().addNode(node);
	}

	/**
	 * 
	 * @param node
	 * 			a node that will be removed from the current up cable. Nodes can 
	 * 			be added or removed from the up cable after its creation.
	 */
	public void removeNode(Node node){
		this.getNodeSet().removeNode(node);
	}
}
