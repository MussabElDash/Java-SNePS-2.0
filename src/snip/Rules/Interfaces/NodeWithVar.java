package snip.Rules.Interfaces;

import java.util.LinkedList;

import sneps.Nodes.VariableNode;

public interface NodeWithVar {
	public LinkedList<VariableNode> getFreeVariables();
	public int getId();
}
