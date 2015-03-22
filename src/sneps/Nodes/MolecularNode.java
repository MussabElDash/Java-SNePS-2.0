/**
 * @className MolecularNode.java
 * 
 * @ClassDescription A Molecular node is a node which has Pattern or Closed 
 * 	as its syntactic type. This class extends Node class and has no more
 * 	instance variables than those inherited from the super class. This is an
 * 	abstract class.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Nodes;

import sneps.Cables.DownCableSet;
import sneps.SemanticClasses.Entity;
import sneps.SyntaticClasses.Molecular;

public class MolecularNode extends Node{
	
	/**
	 * The first constructor of this class.
	 * 
	 * @param syntactic
	 * 			an instance of any syntactic class 
	 * 			that extend the 'Molecular' class and this will
	 * 			be the syntactic type of the node that will be 
	 * 			created.
	 * @param semantic
	 * 			an instance of a class from the semantic hierarchy
	 * 			that will be the semantic type of the node that will 
	 * 			be created.
	 */
	public MolecularNode(Molecular syntactic, Entity semantic){
		super(syntactic, semantic);
		this.updateUpCables();
	}

	/**
	 * The second constructor of this class.
	 * 
	 * @param syntactic
	 * 			the name of the syntactic class that will be created
	 * 			to represent the syntactic type of the molecular node 
	 * 			that will be created.
	 * @param semantic
	 * 			the name of the semantic class that will be created
	 * 			to represent the syntactic type of the molecular node 
	 * 			that will be created.
	 * @param name
	 * 			the name or the label of the molecular node that will be created. 
	 * 			(this will be passed as a parameter to the syntactic class 
	 * 			constructor while creating an instance of the syntactic class 
	 * 			specified.)
	 * @param dCableSet
	 * 			the down cable set of the molecular node that will be created.
	 * 			(this will be passed as a parameter to the syntactic class 
	 * 			constructor while creating an instance of the syntactic class 
	 * 			specified.)
	 * 
	 * @throws Exception
	 */
	public MolecularNode(String syntactic, String semantic, String name, DownCableSet dCableSet) throws Exception {
		super(syntactic, semantic, name, dCableSet);
		this.updateUpCables();
	}
	
	/**
	 * 
	 * @return the down cable set of the current molecular node.
	 */
	public DownCableSet getDownCableSet(){
		return ((Molecular) this.getSyntactic()).getDownCableSet();
	}
	
	/**
	 * This method overrides the default toString method inherited from the Object class.
	 */
	@Override
	public String toString(){
		return ((Molecular) this.getSyntactic()).toString();
	}

	/**
	 * This method is invoked from the constructor of this class (while 
	 * 	creating any molecular node) and it invokes the 'updateUpCables'
	 * 	method in the 'Molecular' class and pass the current node as a 
	 * 	parameter.
	 */
	public void updateUpCables(){  	
		((Molecular) this.getSyntactic()).updateUpCables(this);
	}

}
