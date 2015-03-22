/**
 * @className ClosedNode.java
 * 
 * @ClassDescription A closed node is a node which has Closed as its syntactic 
 * 	type. This class extends MolecularNode class and has no more instance variables
 * 	than those inherited from the super class.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Nodes;

import sneps.Cables.DownCableSet;
import sneps.SemanticClasses.Entity;
import sneps.SyntaticClasses.Closed;

public class ClosedNode extends MolecularNode{
	
	/**
	 * The first constructor of this class.
	 * 
	 * @param syntactic
	 * 			an instance of 'Closed' class that will be
	 * 			the syntactic type of the node that will be 
	 * 			created.
	 * @param semantic
	 * 			an instance of a class from the semantic hierarchy
	 * 			that will be the semantic type of the node that will 
	 * 			be created.
	 */
	public ClosedNode(Closed syntactic, Entity semantic){
		super(syntactic, semantic);
	}
	
	/**
	 * The second constructor of this class.
	 * 
	 * @param semantic
	 * 			the name of the semantic class that will be created
	 * 			to represent the syntactic type of the closed node 
	 * 			that will be created.
	 * @param name
	 * 			the name or the label of the closed node that will be created. 
	 * 			(this will be passed as a parameter to the syntactic class 
	 * 			constructor while creating an instance of the syntactic class 
	 * 			specified.)
	 * @param dCableSet
	 * 			the down cable set of the closed node that will be created.
	 * 			(this will be passed as a parameter to the syntactic class 
	 * 			constructor while creating an instance of the syntactic class 
	 * 			specified.)
	 * 
	 * @throws Exception
	 */
	public ClosedNode(String semantic, String name, DownCableSet dCableSet) throws Exception {
		super("Closed", semantic, name, dCableSet);
	}

}
