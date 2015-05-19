package snip.Rules.RuleNodes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Channel;
import snip.Report;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.RuleUseInfo;
import SNeBR.Context;

public class AndOrNode extends RuleNode {

	private int min, max, arg;

	public AndOrNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet minNode = this.getDownNodeSet("min");
		min = Integer.parseInt(minNode.getNode(0).getIdentifier());
		NodeSet maxNode = this.getDownNodeSet("max");
		max = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("arg");
		arg = antNodes.size();

		this.processNodes(antNodes);
	}

	protected void sendRui(RuleUseInfo ruiRes, Context context) {
		// TODO Mussab Compute Support
		boolean sign = false;
		if (ruiRes.getNegCount() == arg - min)
			sign = true;
		else if (ruiRes.getPosCount() != max)
			return;

		Set<Integer> consequents = new HashSet<Integer>();
		for (FlagNode fn : ruiRes.getFlagNodeSet()) {
			if (antNodesWithVarsIDs.contains(fn.getNode().getId()))
				continue;
			if (antNodesWithoutVarsIDs.contains(fn.getNode().getId()))
				continue;
			consequents.add(fn.getNode().getId());
		}

		Report reply = new Report(ruiRes.getSub(), null, sign, context.getId());
		Channel ch = null;
		for (Iterator<Channel> iter = outgoingChannels.getIterator(); iter
				.hasNext();) {
			ch = iter.next();
			if (!consequents.contains(ch.getDestination().getId()))
				continue;
			ch.addReport(reply);
		}

	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getArg() {
		return arg;
	}

}
