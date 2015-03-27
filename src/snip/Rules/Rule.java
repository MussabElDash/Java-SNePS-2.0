package snip.Rules;

import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;

public abstract class Rule extends PropositionNode {
	public Rule(Molecular syn, Proposition sym) {
		super(syn,sym);
	}
	
	abstract public void applyRuleHandler();
}
