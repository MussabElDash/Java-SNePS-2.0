package snip.Rules.RuleNodes;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import sneps.Cables.DownCable;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PropositionNode;
import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.AntecedentToRuleChannel;
import snip.Channel;
import snip.ChannelTypes;
import snip.Report;
import snip.RuleToConsequentChannel;
import snip.Rules.DataStructures.ContextRUIS;
import snip.Rules.DataStructures.ContextRUISSet;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.RuleUseInfo;
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
	 * Set of ids of the variables shared by all patterns
	 */
	protected Set<Integer> sharedVars;

	protected ContextRUISSet contextRUISSet;

	private Hashtable<Integer, RuleUseInfo> contextConstantRUI;

	public RuleNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		antNodesWithoutVars = new NodeSet();
		antNodesWithVars = new NodeSet();
		contextRUISSet = new ContextRUISSet();
		contextConstantRUI = new Hashtable<Integer, RuleUseInfo>();
	}

	protected void processNodes(NodeSet antNodes) {
		this.splitToNodesWithVarsAndWithout(antNodes, antNodesWithVars,
				antNodesWithoutVars);
		this.antsWithoutVarsNumber = this.antNodesWithoutVars.size();
		this.antsWithVarsNumber = this.antNodesWithVars.size();
		this.shareVars = this.allShareVars(antNodesWithVars);
		sharedVars = getSharedVarsInts(antNodesWithVars);
	}

	/**
	 * Applies the rule handler after receiving a report
	 * 
	 * @param report
	 *            Report
	 * @param signature
	 *            Node
	 */
	public void applyRuleHandler(Report report, Node signature) {
		Context context = report.getContext();
		RuleUseInfo rui;
		if (report.isPositive()) {
			FlagNode fn = new FlagNode(signature, report.getSupport(), 1);
			FlagNodeSet fns = new FlagNodeSet();
			fns.putIn(fn);
			rui = new RuleUseInfo(report.getSubstituions(), 1, 0, fns);
		} else {
			FlagNode fn = new FlagNode(signature, report.getSupport(), 2);
			FlagNodeSet fns = new FlagNodeSet();
			fns.putIn(fn);
			rui = new RuleUseInfo(report.getSubstituions(), 0, 1, fns);
		}
		ContextRUIS crtemp = null;
		if (this.getContextRUISSet().hasContext(context)) {
			crtemp = this.getContextRUISSet().getContextRUIS(context);
		} else {
			crtemp = addContextRUIS(context);
		}
		if (shareVars) {
			SIndexing scrtemp = (SIndexing) crtemp;
			RuleUseInfoSet ruisRes = scrtemp.insertRUI(rui);
			for (RuleUseInfo ruiRes : ruisRes)
				sendRui(ruiRes, context);
			return;
		}
		RuleUseInfoSet rcrtemp = (RuleUseInfoSet) crtemp;
		RuleUseInfoSet res = rcrtemp.insertRUI(rui);
		if (res == null)
			res = new RuleUseInfoSet();
		for (RuleUseInfo tRui : res) {
			sendRui(tRui, context);
		}
	}

	abstract protected void sendRui(RuleUseInfo tRui, Context context);

	/**
	 * Nullifies all instance variables previously used in this node in-order to
	 * save some memory and prepare for the new inference
	 */
	public void clear() {
		contextRUISSet.clear();
		contextConstantRUI.clear();
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
	 * Return the intersection of the Variables of the patterns
	 * 
	 * @param nodes
	 *            NodeSet
	 * @return Set<VariableNode>
	 */
	public Set<VariableNode> getSharedVarsNodes(NodeSet nodes) {
		if (nodes.isEmpty())
			return new HashSet<VariableNode>();
		NodeWithVar n = (NodeWithVar) nodes.getNode(0);
		Set<VariableNode> res = ImmutableSet.copyOf(n.getFreeVariables());
		for (int i = 1; i < nodes.size(); i++) {
			n = (NodeWithVar) nodes.getNode(i);
			Set<VariableNode> temp = ImmutableSet.copyOf(n.getFreeVariables());
			res = Sets.intersection(res, temp);
		}
		return res;
	}

	/**
	 * Return the intersection of the Variables of the patterns
	 * 
	 * @param nodes
	 *            NodeSet
	 * @return Set<VariableNode>
	 */
	public Set<Integer> getSharedVarsInts(NodeSet nodes) {
		Set<VariableNode> vars = getSharedVarsNodes(nodes);
		Set<Integer> res = new HashSet<Integer>();
		for (VariableNode var : vars)
			res.add(var.getId());
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
			if (n instanceof NodeWithVar && !(n instanceof RuleNode))
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
	public RuleUseInfo addConstantRuiToContext(Context context, RuleUseInfo rui) {
		RuleUseInfo tRui = contextConstantRUI.get(context.getId());
		if (tRui != null)
			rui = rui.combine(tRui);
		contextConstantRUI.put(context.getId(), rui);
		return rui;
	}

	/**
	 * Gets the number of positive constant reports in the Context context
	 * 
	 * @param context
	 *            Context
	 * @return int
	 */
	public int getPositiveCount(Context context) {
		if (contextConstantRUI.containsKey(context.getId()))
			return contextConstantRUI.get(context.getId()).getPosCount();
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
		if (contextConstantRUI.containsKey(context.getId()))
			return contextConstantRUI.get(context.getId()).getNegCount();
		return 0;
	}

	public static boolean isConstantNode(Node n) {
		return !(n instanceof NodeWithVar) || n instanceof RuleNode;
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
