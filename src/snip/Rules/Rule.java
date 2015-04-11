package snip.Rules;

import sneps.Nodes.NodeSet;
import sneps.Nodes.PatternNode;
import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;

public abstract class Rule extends PropositionNode {

	/**
	 * a NodeSet containing all the antecedents of the node attached to this
	 * Node
	 */
	protected NodeSet antNodes;

	/**
	 * the number of antecedents in the node attached to this process
	 */
	protected int antsNumber;

	/**
	 * set to true if all the antecedents share the same variables, false
	 * otherwise.
	 */
	protected boolean shareVars;

	/**
	 * array containing the id's of all the variables in the antecedents in case
	 * they all share the same variables. used in SIndexing
	 */
	protected int[] vars;

	public Rule(Molecular syn, Proposition sym) {
		super(syn, sym);
	}

	abstract public void applyRuleHandler(Report report);

	/**
	 * Check if all patterns share all variables or not
	 * 
	 * @return true or false
	 */
	public boolean allShareVars(NodeSet nodes) {

		PatternNode n = (PatternNode) nodes.getNode(0);
		boolean res = true;
		for (int i = 1; i < nodes.size(); i++) {
			if (!n.hasSameFreeVariablesAs((PatternNode) nodes.getNode(i))) {
				res = false;
				break;
			}
		}
		return res;
	}

	/**
	 * Return the down node set of this node
	 * 
	 * @param name
	 *            the relation name
	 * @return NodeSet
	 */
	public NodeSet getDownNodeSet(String name) {
		// TODO Make sure of the cables
		return this.getDownCableSet().getDownCable(name).getNodeSet();
	}

	/**
	 * Return the up node set of this node
	 * 
	 * @param name
	 *            the relation name
	 * @return NodeSet
	 */
	public NodeSet getUpNodeSet(String name) {
		// TODO Make sure of the cables
		return this.getUpCableSet().getUpCable(name).getNodeSet();
	}
}
