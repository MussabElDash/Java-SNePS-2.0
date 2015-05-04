package snip.Rules.RuleNodes;

import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Rules.DataStructures.RuleUseInfo;
import SNeBR.Context;

public class NumericalNode extends RuleNode {

	private int i;

	public NumericalNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		// TODO Auto-generated constructor stub
		NodeSet maxNode = this.getDownNodeSet("i");
		i = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("&ant");
		this.processNodes(antNodes);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, Context context) {
		// TODO Auto-generated method stub

	}

}
