package snip.Rules.RuleNodes;

import java.util.Iterator;

import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Channel;
import snip.Report;
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
		// TODO Mussab Compute support
		if (tRui.getPosCount() < i)
			return;
		Report reply = new Report(tRui.getSub(), null, true, context.getId());
		Channel ch = null;
		for (Iterator<Channel> iter = outgoingChannels.getIterator(); iter
				.hasNext();) {
			ch = iter.next();
			ch.addReport(reply);
		}
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
