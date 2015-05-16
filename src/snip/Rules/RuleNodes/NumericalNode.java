package snip.Rules.RuleNodes;

import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Rules.DataStructures.RuleUseInfo;
import SNeBR.Context;

public class NumericalNode extends RuleNode {

	private int i, numAnt, cq;;

	public NumericalNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet maxNode = this.getDownNodeSet("i");
		i = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("&ant");
		numAnt = antNodes.size();
		cq = this.getDownNodeSet("cq").size();
		this.processNodes(antNodes);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, Context context) {
		// TODO Mussab Auto-generated method stub

	}

	public int getI() {
		return i;
	}

	public int getNumAnt() {
		return numAnt;
	}

	public int getCq() {
		return cq;
	}

}
