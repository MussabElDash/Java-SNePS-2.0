package snip;

import sneps.Nodes.Node;
import sneps.match.Substitutions;
import SNeBR.Context;

public class AntecedentToRuleChannel extends Channel {

	public AntecedentToRuleChannel(Substitutions switchSubstitution, Substitutions filterSubstitutions, int contextID, Node d, boolean v) {
		super(switchSubstitution, filterSubstitutions, contextID, d, v);
	}
}
