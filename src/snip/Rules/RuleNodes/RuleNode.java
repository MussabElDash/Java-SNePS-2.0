package snip.Rules.RuleNodes;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import sneps.Cables.DownCable;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.AntecedentToRuleChannel;
import snip.Channel;
import snip.ChannelTypes;
import snip.Report;
import snip.RuleToConsequentChannel;
import snip.Rules.DataStructures.ContextRUIS;
import snip.Rules.DataStructures.ContextRUISSet;
import snip.Rules.DataStructures.RuleUseInfoSet;
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

	private Hashtable<Integer, Set<Integer>> contextConstantPositiveNodes,
			contextConstantNegativeNodes;

	public RuleNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		antNodesWithoutVars = new NodeSet();
		antNodesWithVars = new NodeSet();
		contextRUISSet = new ContextRUISSet();
		contextConstantPositiveNodes = new Hashtable<Integer, Set<Integer>>();
		contextConstantNegativeNodes = new Hashtable<Integer, Set<Integer>>();
	}

	/**
	 * Applies the rule handler after receiving a report
	 * 
	 * @param report
	 *            Report
	 * @param signature
	 *            Node
	 */
	abstract public void applyRuleHandler(Report report, Node signature);

	/**
	 * Nullifies all instance variables previously used in this node in-order to
	 * save some memory and prepare for the new inference
	 */
	public void clear() {
		contextRUISSet.clear();
		contextConstantPositiveNodes.clear();
		contextConstantNegativeNodes.clear();
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
			return createContextRUISNonShared(c);
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
		contextRUISSet.putIn(cRuis);
		return cRuis;
	}

	/**
	 * Returns a new ContextRUI that is used in-case the antecedents/arguments
	 * do not share the same variables is to be associated with the Context c
	 * 
	 * @param c
	 *            Context
	 * @return ContextRUIS
	 */
	protected ContextRUIS createContextRUISNonShared(Context c) {
		return new RuleUseInfoSet(c);
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
			if (n instanceof NodeWithVar)
				withVars.addNode(n);
			else
				WithoutVars.addNode(n);
		}
	}

	/**
	 * Adds the constant Node node (Node without variables) to the set of
	 * previously reported constant nodes in the Context context with the sign
	 * sign that indicates whether the node is positive or not
	 * 
	 * @param context
	 *            Context
	 * @param node
	 *            Node
	 * @param sign
	 *            boolean
	 */
	public void addConstantToContext(Context context, Node node, boolean sign) {
		Hashtable<Integer, Set<Integer>> hashtable;
		if (sign)
			hashtable = contextConstantPositiveNodes;
		else
			hashtable = contextConstantNegativeNodes;

		Set<Integer> nodes = hashtable.get(context.getId());
		if (nodes == null) {
			nodes = new HashSet<Integer>();
			hashtable.put(context.getId(), nodes);
		}
		nodes.add(node.getId());
	}

	/**
	 * Gets the number of positive constant reports in the Context context
	 * 
	 * @param context
	 *            Context
	 * @return int
	 */
	public int getPositiveCount(Context context) {
		if (contextConstantPositiveNodes.containsKey(context.getId()))
			return contextConstantPositiveNodes.get(context.getId()).size();
		return 0;
	}

	/**
	 * Gets the number of negative constant reports in the Context context
	 * 
	 * @param context
	 *            Context
	 * @return int
	 */
	public int getNegativeCount(Context context) {
		if (contextConstantNegativeNodes.containsKey(context.getId()))
			return contextConstantNegativeNodes.get(context.getId()).size();
		return 0;
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
				this.applyRuleHandler(null, null);
			}
		}
	}
}
