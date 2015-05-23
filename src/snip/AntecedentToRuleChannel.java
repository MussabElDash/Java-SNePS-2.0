package snip;

import sneps.Nodes.Node;
import sneps.match.Substitutions;

public class AntecedentToRuleChannel extends Channel {

	public AntecedentToRuleChannel(Substitutions switchSubstitution, Substitutions filterSubstitutions, int contextID, Node requester, Node reporter, boolean v) {
		super(switchSubstitution, filterSubstitutions, contextID, requester, reporter, v);
	}
}
