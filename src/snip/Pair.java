package snip;

import sneps.Nodes.Node;
import sneps.match.Substitutions;


public class Pair {
	Node node;
	Substitutions filterSubs, switchSubs;

	public Pair(Substitutions filterSubs, Substitutions switchSubs, Node node) {
		this.node = node;
		this.filterSubs = filterSubs;
		this.switchSubs = switchSubs;
	}

	public Node getNode() {
		return node;
	}
	public Substitutions getFilter() {
		return filterSubs;
	}

	public Substitutions getSwitch() {
		return switchSubs;
	}
}