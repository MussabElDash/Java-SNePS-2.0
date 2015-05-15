package snip;

import sneps.Nodes.Node;
import sneps.match.Substitutions;


public class Pair {
	Node node;
	Substitutions one, two;

	public Pair(Substitutions first, Substitutions sec, Node node) {
		this.node = node;
		one = first;
		sec = two;
	}

	public Node getNode() {
		return node;
	}
	public Substitutions getFilter() {
		return one;
	}

	public Substitutions getSwitch() {
		return two;
	}
}