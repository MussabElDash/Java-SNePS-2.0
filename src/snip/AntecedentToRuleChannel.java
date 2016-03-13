package snip;

import sneps.Nodes.Node;
import sneps.match.Substitutions;
import SNeBR.Context;

public class AntecedentToRuleChannel extends Channel {

	public AntecedentToRuleChannel(Substitutions switchSubstitution, Substitutions filterSubstitutions, int contextID, Node requester, Node reporter, boolean v) {
		super(switchSubstitution, filterSubstitutions, contextID, requester, reporter, v);
	}
}
