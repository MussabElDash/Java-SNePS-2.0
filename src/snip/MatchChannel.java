package snip;

import sneps.Nodes.Node;
import SNeBR.Context;

public class MatchChannel extends Channel {
	
	public MatchChannel() {
		super();
	}
	public MatchChannel(Filter f, Switch s, Context c, Node d, boolean v) {
		super(f, s, c, d, v);
	}

}
