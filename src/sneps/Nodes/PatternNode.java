/**
 * @className PatternNode.java
 * 
 * @ClassDescription A pattern node is a node which has Pattern as its syntactic 
 * 	type. This class extends MolecularNode class and has no more instance variables
 * 	than those inherited from the super class.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Nodes;

import java.util.LinkedList;

import sneps.Cables.DownCableSet;
import sneps.SemanticClasses.Entity;
import sneps.SyntaticClasses.Pattern;
import snip.Rules.Interfaces.NodeWithVar;

public class PatternNode extends MolecularNode implements NodeWithVar{

	/**
	 * The first constructor of this class.
	 * 
	 * @param syntactic
	 * 			an instance of 'Pattern' class that will be
	 * 			the syntactic type of the node that will be 
	 * 			created.
	 * @param semantic
	 * 			an instance of a class from the semantic hierarchy
	 * 			that will be the semantic type of the node that will 
	 * 			be created.
	 */
	public PatternNode(Pattern syntactic, Entity semantic){
		super(syntactic, semantic);
	}
	
	/**
	 * The second constructor of this class.
	 * 
	 * @param semantic
	 * 			the name of the semantic class that will be created
	 * 			to represent the syntactic type of the pattern node 
	 * 			that will be created.
	 * @param name
	 * 			the name or the label of the pattern node that will be created. 
	 * 			(this will be passed as a parameter to the syntactic class 
	 * 			constructor while creating an instance of the syntactic class 
	 * 			specified.)
	 * @param dCableSet
	 * 			the down cable set of the pattern node that will be created.
	 * 			(this will be passed as a parameter to the syntactic class 
	 * 			constructor while creating an instance of the syntactic class 
	 * 			specified.)
	 * 
	 * @throws Exception
	 */
	public PatternNode(String semantic, String name, DownCableSet dCableSet)throws Exception {
		super("Pattern", semantic, name, dCableSet);
	}
	
	/**
	 * 
	 * @return the list of free variables dominated by the current pattern node.
	 */
	public LinkedList<VariableNode> getFreeVariables(){
		return ((Pattern)this.getSyntactic()).getFreeVariables();
	}
	
	/**
	 *  
	 * @param patternNode
	 * 			a given pattern node that its free variables will be compared
	 * 			to the free variables of the current node.
	 * 
	 * @return true if both nodes has the same free variables, and false otherwise.
	 */
	public boolean hasSameFreeVariablesAs(NodeWithVar patternNode){
		return ((Pattern)this.getSyntactic()).hasSameFreeVariablesAs((Node)patternNode);
	}

}