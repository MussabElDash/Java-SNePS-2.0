/**
 * @className Pattern.java
 * 
 * @ClassDescription A pattern term is the syntactic type of any node 
 * 	that dominates free variables. This class extends Molecular class
 * 	and has one more instance in addition to those inherited from
 * 	the super classes.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.SyntaticClasses;

import java.util.Enumeration;
import java.util.LinkedList;

import sneps.Relation;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.VariableNode;

public class Pattern extends Molecular {
	
	/**
	 * a list of the free variables dominated by the current node.
	 */
	private LinkedList<VariableNode> freeVariables;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param identifier
	 * 			the name of the node that will be created.
	 * @param downCableSet
	 * 			the down cable set of the node that will be
	 * 			created.
	 */
	public Pattern(String identifier, DownCableSet downCableSet){
		super(identifier, downCableSet);
		this.freeVariables = new LinkedList<VariableNode>();
		this.updateFreeVariables();
	}
	
	/**
	 * 
	 * @return the list of free variables dominated by the current 
	 */
	public LinkedList<VariableNode> getFreeVariables(){
		return this.freeVariables;
	}
	
	/**
	 * The method that populate the list of free variables by the 
	 * 	free variables dominated by the current node.
	 */
	public void updateFreeVariables(){
		DownCableSet dCableSet = this.getDownCableSet();
		Enumeration<DownCable> dCables = dCableSet.getDownCables().elements();
		while (dCables.hasMoreElements()){
			DownCable dCable = dCables.nextElement();
			NodeSet ns = dCable.getNodeSet();
			Relation r = dCable.getRelation();
			for (int j = 0; j < ns.size(); j++){
				// if node is variable node
				String nodeType = ns.getNode(j).getSyntacticType();
				if (nodeType.equals("Variable") && !r.isQuantifier())
					this.freeVariables.add((VariableNode) ns.getNode(j));
				// if node is pattern node (means it dominates free variables)
				if (nodeType.equals("Pattern")){
					Pattern p = (Pattern) ns.getNode(j).getSyntactic();
					LinkedList<VariableNode> patternFVars = new LinkedList<VariableNode>();
					patternFVars.addAll(p.getFreeVariables());
					// looping over free variables of the pattern node
					for (int k = 0; k < patternFVars.size(); k++){
						VariableNode vNode = patternFVars.get(k);
						// looping over the down cables
						Enumeration<DownCable> dCs = dCableSet.getDownCables().elements();
						while(dCs.hasMoreElements()){
							DownCable d = dCs.nextElement();
							if (d.getNodeSet().contains(vNode))
								patternFVars.remove(vNode);
						}
					}
					this.freeVariables.addAll(patternFVars);
				}
			}
		}
	}
	
	// changed it to MolecularNode because propNodes can use this method and they are not pattern nodes 
	// but their syntactic type should be pattern
	/**
	 *  
	 * @param mNode
	 * 			a given molecular node that its free variables will be compared
	 * 			to the free variables of the current node.
	 * 
	 * @return true if both nodes has the same free variables, and false otherwise.
	 */
	public boolean hasSameFreeVariablesAs(Node mNode) {
		if(mNode.getSyntacticType().equals("Pattern")){
			LinkedList<VariableNode> freeVars = ((Pattern)mNode.getSyntactic()).getFreeVariables();
			if (freeVars.size() != this.freeVariables.size())
				return false;
			for(int i = 0; i < freeVars.size(); i++){
				if (! this.freeVariables.contains(freeVars.get(i)))
					return false;
			}
			return true;
		}
		return false;
	}

}
