package snip.Rules.Interfaces;

import java.util.LinkedList;

import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Entity;
import sneps.SyntaticClasses.Term;

public interface NodeWithVar {
	public LinkedList<VariableNode> getFreeVariables();
	public int getId();
	public boolean hasSameFreeVariablesAs(NodeWithVar node);
	public Term getSyntactic();
	public Entity getSemantic();
}
