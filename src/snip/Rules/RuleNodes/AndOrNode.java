package snip.Rules.RuleNodes;

import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Rules.DataStructures.RuleUseInfo;
import SNeBR.Context;

public class AndOrNode extends RuleNode {

	private int min, max;

	public AndOrNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet minNode = this.getDownNodeSet("min");
		min = Integer.parseInt(minNode.getNode(0).getIdentifier());
		NodeSet maxNode = this.getDownNodeSet("max");
		max = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("arg");
		this.processNodes(antNodes);
	}

	protected void sendRui(RuleUseInfo ruiRes, Context context) {
		// TODO Auto-generated method stub

	}

}
