package snip.Rules.RuleNodes;

import java.util.Set;

import SNeBR.Support;
import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Channel;
import snip.Report;
import snip.Rules.DataStructures.RuleUseInfo;

public class NumericalNode extends RuleNode {

	private int i, ant, cq;;

	public NumericalNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet maxNode = this.getDownNodeSet("i");
		i = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("&ant");
		ant = antNodes.size();
		cq = this.getDownNodeSet("cq").size();
		this.processNodes(antNodes);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, int context) {
		if (tRui.getPosCount() < i)
			return;
		Set<Support> originSupports = ((Proposition) this.getSemantic())
				.getOriginSupport();
		Report reply = new Report(tRui.getSub(),
				tRui.getSupport(originSupports), true, context);
		for (Channel outChannel : outgoingChannels)
			outChannel.addReport(reply);
	}

	public int getI() {
		return i;
	}

	public int getAnt() {
		return ant;
	}

	public int getCq() {
		return cq;
	}

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("&ant");
	}

}
