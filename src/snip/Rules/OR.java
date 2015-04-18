package snip.Rules;

import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;

public class OR extends Rule {

	public OR(Molecular syn, Proposition sym) {
		super(syn, sym);
	}

	@Override
	public void applyRuleHandler(Report r) {
		if (r.isPositive()) {

			Report reply = new Report(r.getSubstituions(), r.getSupport(),
					true, this, null, r.getContext());
			// TODO
			throw new UnsupportedOperationException();
			// ChannelsSet ctemp = this.getOutGoing().getConChannelsSet(
			// r.getContext());
			// this.sendReport(reply, ctemp);
		}

	}

	@Override
	public void resetRule() {
	}
}