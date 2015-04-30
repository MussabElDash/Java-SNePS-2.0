package snip.Rules.RuleNodes;

import java.util.HashSet;
import java.util.Set;

import sneps.Cables.DownCable;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PatternNode;
import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import sneps.SyntaticClasses.Pattern;
import sneps.SyntaticClasses.Term;
import sneps.SyntaticClasses.Variable;
import snip.AntecedentToRuleChannel;
import snip.Channel;
import snip.ChannelTypes;
import snip.Report;
import snip.RuleToConsequentChannel;
import snip.Rules.DataStructures.ContextRUIS;
import snip.Rules.DataStructures.ContextRUISSet;
import snip.Rules.DataStructures.PTree;
import snip.Rules.DataStructures.SIndexing;
import snip.Rules.Interfaces.NodeWithVar;
import SNeBR.Context;

public abstract class RuleNode extends PropositionNode {

	/**
	 * a NodeSet containing all the pattern antecedents attached to this Node
	 */
	protected NodeSet antNodesWithVars;

	/**
	 * a NodeSet containing all the non pattern antecedents attached to this
	 * Node
	 */
	protected NodeSet antNodesWithoutVars;

	/**
	 * the number of antecedents with Variables attached to this Node
	 */
	protected int antsWithVarsNumber;

	/**
	 * the number of antecedents with Variables attached to this Node
	 */
	protected int antsWithoutVarsNumber;

	/**
	 * set to true if all the antecedents with Variables share the same
	 * variables, false otherwise.
	 */
	protected boolean shareVars;

	/**
	 * array containing the id's of all the variables in the antecedents in case
	 * they all share the same variables. used in SIndexing
	 */
	protected int[] vars;

	protected ContextRUISSet contextRUISSet;

	public RuleNode(Molecular syn, Proposition sym) {
		super(syn, sym);
	}

	/**
	 * Applies the rule handler after receiving a report
	 * 
	 * @param report
	 *            Report
	 */
	abstract public void applyRuleHandler(Report report);

	/**
	 * Nullifies all instance variables previously used in this node in-order to
	 * save some memory and prepare for the new inference
	 */
	public void clear() {
		contextRUISSet.clear();
	}

	/**
	 * Check if all patterns share all variables or not
	 * 
	 * @return true or false
	 */
	public boolean allShareVars(NodeSet nodes) {

		NodeWithVar n = (NodeWithVar) nodes.getNode(0);
		boolean res = true;
		for (int i = 1; i < nodes.size(); i++) {
			if (!n.hasSameFreeVariablesAs((NodeWithVar) nodes.getNode(i))) {
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

	public ContextRUISSet getContextRUISSet() {
		return contextRUISSet;
	}

	/**
	 * Add a ContextRUIS to ContextRUISSet
	 * 
	 * @param c
	 *            Context
	 * @return ContextRUIS
	 */
	public ContextRUIS addContextRUIS(Context c) {
		if (shareVars)
			return this.addContextRUIS(new SIndexing(c));
		else {
			PTree pTree = new PTree(c);
			ContextRUIS cr = this.addContextRUIS(pTree);
			pTree.buildTree(antNodesWithVars);
			return cr;
		}
	}

	/**
	 * Add a ContextRUIS to the ContextRUISSet and return it.
	 * 
	 * @param cRuis
	 *            ContextRUIS
	 * @return ContextRUIS
	 */
	public ContextRUIS addContextRUIS(ContextRUIS cRuis) {
		// ChannelsSet ctemp = consequentChannel.getConChannelsSet(c);
		ContextRUIS cr = cRuis;
		contextRUISSet.putIn(cRuis);
		return cr;
	}

	/**
	 * Fills the NodeSet withVars with all the Variable Nodes and Pattern Nodes
	 * from the NodeSet allNodes and Fills the NodeSet withoutVars with the rest
	 * of the Nodes from the NodeSet allNodes
	 * 
	 * @param allNodes
	 *            NodeSet
	 * @param withVars
	 *            NodeSet
	 * @param WithoutVars
	 *            NodeSet
	 */
	public void splitToNodesWithVarsAndWithout(NodeSet allNodes,
			NodeSet withVars, NodeSet WithoutVars) {
		for (int i = 0; i < allNodes.size(); i++) {
			Node n = allNodes.getNode(i);
			Term t = n.getSyntactic();
			if (t instanceof Variable || t instanceof Pattern)
				withVars.addNode(n);
			else
				WithoutVars.addNode(n);
		}
	}

	@Override
	public void processRequests() {
		for (Channel currentChannel : incomingChannels) {
			if (currentChannel instanceof RuleToConsequentChannel) {
				// TODO Akram: no free variable
				if (true) {
					Proposition semanticType = (Proposition) this.getSemantic();
					if (semanticType.isAsserted(currentChannel.getContext())) {
						// TODO Akram: if rule is usable
						if (true) {
							// TODO Akram: relation name "Antecedent"
							DownCable antecedntCable = this.getDownCableSet()
									.getDownCable("Antecedent");
							NodeSet antecedentNodeSet = antecedntCable
									.getNodeSet();
							Set<Node> antecedentNodes = new HashSet<Node>();
							for (int i = 0; i < antecedentNodeSet.size(); ++i) {
								Node currentNode = antecedentNodeSet.getNode(i);
								// TODO Akram: if not yet been requested for
								// this instance
								if (true) {
									antecedentNodes.add(currentNode);
								}
							}
							sendRequests(antecedentNodes,
									currentChannel.getContext(),
									ChannelTypes.RuleAnt);
						} else {
							// TODO Akram: establish the rule
						}
					}
				} else if (true) {

				} else if (true) {

				}
			} else {
				super.processSingleRequest(currentChannel);
			}
		}
	}

	@Override
	public void processReports() {
		for (Channel currentChannel : outgoingChannels) {
			processSingleReport(currentChannel);
			if (currentChannel instanceof AntecedentToRuleChannel) {
				// TODO Akram: send the correct report :D
				this.applyRuleHandler(null);
			}
		}
	}
}
