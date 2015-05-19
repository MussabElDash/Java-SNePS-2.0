package snip.Rules.RuleNodes;

import java.util.Iterator;

import SNeBR.Context;
import sneps.Nodes.Node;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Channel;
import snip.Report;
import snip.Rules.DataStructures.RuleUseInfo;

public class OrNode extends RuleNode {

	private int ant, cq;

	public OrNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		ant = getDownNodeSet("ant").size();
		cq = getDownNodeSet("cq").size();
	}

	@Override
	public void applyRuleHandler(Report report, Node signature) {
		if (report.isNegative())
			return;

		// TODO Mussab Compute Support
		Report reply = new Report(report.getSubstitutions(), null, true,
				report.getContextID());
		Channel ch = null;
		for (Iterator<Channel> iter = outgoingChannels.getIterator(); iter
				.hasNext();) {
			ch = iter.next();
			ch.addReport(reply);
		}
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, Context context) {
	}

	public int getAnt() {
		return ant;
	}

	public int getCq() {
		return cq;
	}
}