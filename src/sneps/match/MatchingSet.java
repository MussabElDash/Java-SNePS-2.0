package sneps.match;

import java.util.HashSet;
import java.util.LinkedList;

import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;

public class MatchingSet  {
	
HashSet<Node> VariableBaseNodes;
LinkedList<MolecularNode> MolecularNodes;

public MatchingSet(){
	VariableBaseNodes=new HashSet<Node>();
	MolecularNodes=new LinkedList<MolecularNode>();
}

	
	public boolean add(Node n) {
		if(n.getSyntacticSuperClass().equals("Molecular")){
		for (int i = 0; i < MolecularNodes.size(); i++) 
			if(Matcher.sameFunction((MolecularNode) n, MolecularNodes.get(i)))
				return false;
		MolecularNodes.add((MolecularNode) n);
			return true;
			}
		else
			return VariableBaseNodes.add(n);
		
	}

	



	
	public boolean remove(Node n) {
		if(n.getSyntacticSuperClass().equals("Molecular")){
			for (int i = 0; i < MolecularNodes.size(); i++) 
				if(Matcher.sameFunction((MolecularNode) n, MolecularNodes.get(i)))
				{MolecularNodes.remove((MolecularNode) n);
				return true;}
			return false;
				
		}
		else
			return VariableBaseNodes.remove(n);
	}
	
	public boolean replace(Node n1,Node n2){
		return remove(n1)&&add(n2);
	}

	public int size(){
		return VariableBaseNodes.size()+MolecularNodes.size();
	}
	
	public Node[] toArray(){
		Node[] nodes=new Node[VariableBaseNodes.size()+MolecularNodes.size()];
		Node[] vbNodes=new Node[VariableBaseNodes.size()];
		Node[] mNodes=new Node[MolecularNodes.size()];
		VariableBaseNodes.toArray(vbNodes);
		MolecularNodes.toArray(mNodes);
		for (int i = 0; i < mNodes.length+vbNodes.length; i++) {
		if(i<mNodes.length)
			nodes[i]=mNodes[i];
		else
			nodes[i]=vbNodes[i-mNodes.length];
		}
		
		return nodes;
	}
	
	public String toString(){
		return VariableBaseNodes.toString()+MolecularNodes.toString();
	}
	
	public void add(NodeSet ns){
		for (int i = 0; i < ns.size(); i++) 
			add(ns.getNode(i));
		
	}

}
