/**
 * @className DownCable.java
 * 
 * @ClassDescription A down-cable is a structure that enables traversing the network downwards. 
 * 	This class extends the cable class and has no variables than those inherited from the super
 * 	class. Once a down cable is created, its relation and node set can never be changed.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Cables;

import sneps.Relation;
import sneps.Nodes.NodeSet;

public class DownCable extends Cable{

	/**
	 * 
	 * @param relation
	 * 			the relation that labels the arcs of this down cable
	 * @param nodeSet
	 * 			the nodes included in this down cable.
	 */
	public DownCable(Relation relation, NodeSet nodeSet) {
		super(relation, nodeSet);
		// TODO does this resembles what we agreed to do with semantic classes??
		//this.updateSemanticClass();
	}
	
//	// old
//	public DownCable(Relation relation, NodeSet nodeSet) {
//		super(relation, nodeSet);
//		this.setNodeSet(nodeSet);
//		// TODO does this resembles what we agreed to do with semantic classes??
//		//this.updateSemanticClass();
//	}

	
	public void updateSemanticClass(){
		
	}
	
	/* the method that (set) or update the semantic class of base node
	*/
	// TODO check it again with the Dr
	// the method to update semantic type of the base node
	public void setSemanticClass(String semanticType) {
	}
}
