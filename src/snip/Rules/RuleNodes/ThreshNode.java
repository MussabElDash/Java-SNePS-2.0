package snip.Rules.RuleNodes;

import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Rules.DataStructures.RuleUseInfo;
import SNeBR.Context;

public class ThreshNode extends RuleNode {

	private int min, max , arg;

	public ThreshNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet minNode = this.getDownNodeSet("thresh");
		min = Integer.parseInt(minNode.getNode(0).getIdentifier());
		NodeSet maxNode = this.getDownNodeSet("threshmax");
		max = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("arg");
		arg=antNodes.size();
		this.processNodes(antNodes);
	}

	protected void sendRui(RuleUseInfo ruiRes, Context context) {
		// TODO Mussab Auto-generated method stub

	}
	public int getThresh(){
		return min;
	}
	public int getThreshMax(){
		return max;
	}
	public int getArg(){
		return arg;
	}

}
