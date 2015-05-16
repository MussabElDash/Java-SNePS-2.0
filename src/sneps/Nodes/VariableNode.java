/**
 * @className VariableNode.java
 * 
 * @ClassDescription A variable node is a node which has Variable as 
 * 	its syntactic type. This class extends Node class and has no more
 * 	instance variables than those inherited from the super class.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Nodes;

import java.util.LinkedList;

import sneps.SemanticClasses.Entity;
import sneps.SyntaticClasses.Variable;
import snip.Rules.Interfaces.NodeWithVar;

public class VariableNode extends Node implements NodeWithVar {
	
	/**
	 * The first constructor of this class.
	 * 
	 * @param syntactic
	 * 			an instance of the 'Variable' class that will
	 * 			be the syntactic type of the node that will be 
	 * 			created.
	 * @param semantic
	 * 			an instance of a class from the semantic hierarchy
	 * 			that will be the semantic type of the node that will 
	 * 			be created.
	 */
	public VariableNode(Variable syntactic, Entity semantic){
		super(syntactic, semantic);
	}
	
	/**
	 * The second constructor of this class.
	 * 
	 * @param semantic
	 * 			the name of the semantic class that will be created
	 * 			to represent the syntactic type of the node that will be
	 * 			created.
	 * @param name
	 * 			the name or the label of the node that will be created. 
	 * 			(this will be passed as a parameter to the syntactic class 
	 * 			constructor while creating an instance of the syntactic class 
	 * 			specified.)
	 * 
	 * @throws Exception
	 */
	public VariableNode(String semantic, String name) throws Exception {
		super("Variable", semantic, name);
	}

	@Override
	public LinkedList<VariableNode> getFreeVariables() {
		LinkedList<VariableNode> temp = new LinkedList<>();
		temp.add(this);
		return temp;
	}

	@Override
	public boolean hasSameFreeVariablesAs(NodeWithVar node) {
		return false;
	}

//////////////////////////////////////main method that was used for testing //////////////////////////////////////	
	
//	public static void main(String[] args){
//		Variable v = new Variable("V1");
//		Entity e = new Entity();
//		VariableNode node = new VariableNode(v, e);
//		System.out.println(node.semantic.getClass().getSimpleName());
//		System.out.println(node.getSemanticType());
//		System.out.println(node.getSemantic().getClass().getSimpleName());
//	}

}
