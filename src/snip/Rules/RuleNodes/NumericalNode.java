package snip.Rules.RuleNodes;

import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Channel;
import snip.Report;
import snip.Rules.DataStructures.RuleUseInfo;

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
	protected void sendRui(RuleUseInfo tRui, int context) {
		// TODO Mussab Compute support
		if (tRui.getPosCount() < i)
			return;
		Report reply = new Report(tRui.getSub(), null, true, context);
		for (Channel outChannel : outgoingChannels)
			outChannel.addReport(reply);
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

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("&ant");
	}

}
