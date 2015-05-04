package snip.Rules.RuleNodes;

import SNeBR.Context;
import sneps.Nodes.Node;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;
import snip.Rules.DataStructures.RuleUseInfo;

public class OrNode extends RuleNode {

	public OrNode(Molecular syn, Proposition sym) {
		super(syn, sym);
	}

	@Override
	public void applyRuleHandler(Report report, Node signature) {
		if (report.isNegative())
			return;

		Report reply = new Report(report.getSubstituions(),
				report.getSupport(), true, this, null, report.getContext());
		// TODO
		throw new UnsupportedOperationException();
		// ChannelsSet ctemp = this.getOutGoing().getConChannelsSet(
		// r.getContext());
		// this.sendReport(reply, ctemp);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, Context context) {
	}
}