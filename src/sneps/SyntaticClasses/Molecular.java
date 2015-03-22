/**
 * @className Molecular.java
 * 
 * @ClassDescription A molecular term is the super class of any node with 
 * 	outgoing arcs; a node that dominates other node(s), w. This class is an 
 * 	an abstract class and it extends Term class. It has one more instance
 * 	variable in addition to those inherited from the super class.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.SyntaticClasses;

import java.util.Enumeration;

import sneps.Relation;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Cables.UpCable;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;

public abstract class Molecular extends Term{
	
	/**
	 * a down cable set storing the down cables of the current node. Molecular 
	 * 	nodes is the only types that have down cables because they are the only 
	 * 	type of nodes that dominate other nodes.
	 * 
	 */
	private DownCableSet downCableSet;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param identifier
	 * 			the name of the node that will be created.
	 * @param downCableSet
	 * 			the down cable set of the node  that will be
	 * 			created.
	 */
	public Molecular(String identifier, DownCableSet downCableSet){
		super(identifier);
		this.downCableSet = downCableSet;
	}
	
	/**
	 * 
	 * @return the down cable set of the current node.
	 */
	public DownCableSet getDownCableSet(){
		return this.downCableSet;
	}
	
	/**
	 * This method overrides the default toString method inherited from the Term class.
	 * 
	 * @return a string representing the name of the current node + the to string of the 
	 * 	down cable set.
	 */
	@Override
	public String toString(){
		return this.getIdentifier()+":("+this.getDownCableSet().toString()+")";
	}
	
	/**
	 * A method that adds node object, that have the current molecular
	 * 	object as its syntactic object, to the suitable up cable of all 
	 * 	the nodes dominated by the current node.
	 * 
	 * @param node
	 * 			the node object having the current molecular object
	 * 			as its syntactic object.
	 */
	public void updateUpCables(MolecularNode node) {
		DownCableSet dCableSet = this.getDownCableSet();
		Enumeration<DownCable> dCables = dCableSet.getDownCables().elements();
		while(dCables.hasMoreElements()){
			DownCable dCable = dCables.nextElement();
			Relation r = dCable.getRelation();
			NodeSet ns = dCable.getNodeSet();
			for (int j = 0; j < ns.size(); j++){
				Node n = ns.getNode(j);
				if (!n.getUpCableSet().contains(r))
					n.getUpCableSet().addUpCable(new UpCable(r));
				n.getUpCableSet().getUpCable(r.getName()).addNode(node);
			}
		}
	}

}
