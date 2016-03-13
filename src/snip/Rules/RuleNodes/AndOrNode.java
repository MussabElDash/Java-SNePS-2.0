package snip.Rules.RuleNodes;

import java.util.HashSet;
import java.util.Set;

import SNeBR.Support;
import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Channel;
import snip.Report;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.RuleUseInfo;

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

	protected void sendRui(RuleUseInfo ruiRes, int context) {
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

		Set<Support> originSupports = ((Proposition) this.getSemantic())
				.getOriginSupport();
		Report reply = new Report(ruiRes.getSub(),
				ruiRes.getSupport(originSupports), sign, context);
		for (Channel outChannel : outgoingChannels) {
			if (!consequents.contains(outChannel.getRequester().getId()))
				continue;
			outChannel.addReport(reply);
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

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("arg");
	}

}
