package snip;

import sneps.Nodes.Node;
import sneps.match.Substitutions;
import SNeBR.Context;

public class MatchChannel extends Channel {
	
	public MatchChannel() {
		super();
	}
	public MatchChannel(Substitutions switchSubstitution, Substitutions filterSubstitutions, int contextID, Node d, boolean v) {
		super(switchSubstitution, filterSubstitutions, contextID, d, v);
	}

}
