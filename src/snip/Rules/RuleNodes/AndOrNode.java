package snip.Rules.RuleNodes;

import java.util.Iterator;
import java.util.LinkedList;

import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;
import snip.Rules.Interfaces.NodeWithVar;

public class AndOrNode extends RuleNode {

	public AndOrNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet antNodes = this.getDownNodeSet("arg");
		this.splitToNodesWithVarsAndWithout(antNodes, antNodesWithVars,
				antNodesWithoutVars);
		this.antsWithoutVarsNumber = this.antNodesWithoutVars.size();
		this.antsWithVarsNumber = this.antNodesWithVars.size();
		this.shareVars = this.allShareVars(antNodesWithVars);
		if (shareVars) {
			NodeWithVar pn = (NodeWithVar) antNodesWithVars.getNode(0);
			LinkedList<VariableNode> varNodes = pn.getFreeVariables();
			vars = new int[varNodes.size()];
			Iterator<VariableNode> varIter = varNodes.iterator();
			for (int i = 0; i < vars.length && varIter.hasNext(); i++) {
				vars[i] = varIter.next().getId();
			}
		}
	}

	@Override
	public void applyRuleHandler(Report report, Node signature) {
		// TODO Auto-generated method stub

	}

}
