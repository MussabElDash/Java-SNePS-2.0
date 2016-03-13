package snip.Rules.RuleNodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import SNeBR.Context;
import SNeBR.SNeBR;
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
import snip.Rules.DataStructures.ContextRuisSet;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.RuisHandler;
import snip.Rules.DataStructures.RuleUseInfo;
import snip.Rules.DataStructures.RuleUseInfoSet;
import snip.Rules.DataStructures.SIndex;
import snip.Rules.Interfaces.NodeWithVar;

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
	 * an integer set containing all the ids of the pattern antecedents attached
	 * to this Node
	 */
	protected Set<Integer> antNodesWithVarsIDs;

	/**
	 * an integer set containing all the ids of the non pattern antecedents
	 * attached to this Node
	 */
	protected Set<Integer> antNodesWithoutVarsIDs;

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

	protected ContextRuisSet contextRuisSet;

	private Hashtable<Integer, RuleUseInfo> contextConstantRUI;

	public RuleNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		antNodesWithoutVars = new NodeSet();
		antNodesWithoutVarsIDs = new HashSet<Integer>();
		antNodesWithVars = new NodeSet();
		antNodesWithVarsIDs = new HashSet<Integer>();
		contextRuisSet = new ContextRuisSet();
		contextConstantRUI = new Hashtable<Integer, RuleUseInfo>();
	}

	protected void processNodes(NodeSet antNodes) {
		this.splitToNodesWithVarsAndWithout(antNodes, antNodesWithVars, antNodesWithoutVars);
		for (Node n : antNodesWithVars) {
			antNodesWithVarsIDs.add(n.getId());
		}
		for (Node n : antNodesWithoutVars) {
			antNodesWithoutVarsIDs.add(n.getId());
		}
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
		int contextID = report.getContextID();
		// Context context = SNeBR.getContextByID(contextID);
		RuleUseInfo rui;
		if (report.isPositive()) {
			FlagNode fn = new FlagNode(signature, report.getSupports(), 1);
			FlagNodeSet fns = new FlagNodeSet();
			fns.putIn(fn);
			rui = new RuleUseInfo(report.getSubstitutions(), 1, 0, fns);
		} else {
			FlagNode fn = new FlagNode(signature, report.getSupports(), 2);
			FlagNodeSet fns = new FlagNodeSet();
			fns.putIn(fn);
			rui = new RuleUseInfo(report.getSubstitutions(), 0, 1, fns);
		}
		RuisHandler crtemp = null;
		if (this.getContextRUISSet().hasContext(contextID)) {
			crtemp = this.getContextRUISSet().getContextRUIS(contextID);
		} else {
			crtemp = addContextRUIS(contextID);
		}
		RuleUseInfoSet res = crtemp.insertRUI(rui);
		if (res == null)
			res = new RuleUseInfoSet();
		for (RuleUseInfo tRui : res) {
			sendRui(tRui, contextID);
		}
	}

	abstract protected void sendRui(RuleUseInfo tRui, int contextID);

	/**
	 * Nullifies all instance variables previously used in this node in-order to
	 * save some memory and prepare for the new inference
	 */
	public void clear() {
		contextRuisSet.clear();
		contextConstantRUI.clear();
	}

	/**
	 * Check if all patterns share all variables or not
	 * 
	 * @return true or false
	 */
	public boolean allShareVars(NodeSet nodes) {
		if (nodes.isEmpty())
			return false;

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
		return this.getDownCableSet().getDownCable(name).getNodeSet();
	}

	/**
	 * Returns the antecedents down node set and in case of And-or or Thresh
	 * returns the arguments down node set
	 * 
	 * @return NodeSet
	 */
	public abstract NodeSet getDownAntNodeSet();

	/**
	 * Return the up node set of this node
	 * 
	 * @param name
	 *            the relation name
	 * @return NodeSet
	 */
	public NodeSet getUpNodeSet(String name) {
		return this.getUpCableSet().getUpCable(name).getNodeSet();
	}

	public ContextRuisSet getContextRUISSet() {
		return contextRuisSet;
	}

	/**
	 * Add a ContextRUIS to ContextRUISSet
	 * 
	 * @param c
	 *            Context
	 * @return ContextRUIS
	 */
	public RuisHandler addContextRUIS(int contextID) {
		if (sharedVars.size() != 0) {
			SIndex si = null;
			if (shareVars)
				si = new SIndex(contextID, sharedVars, SIndex.SINGLETONRUIS, getPatternNodes());
			else
				si = new SIndex(contextID, sharedVars, getSIndexContextType(), getParentNodes());
			return this.addContextRUIS(si);
		} else {
			return this.addContextRUIS(createContextRUISNonShared(contextID));
		}
	}

	/**
	 * Add a ContextRUIS to the ContextRUISSet and return it.
	 * 
	 * @param cRuis
	 *            ContextRUIS
	 * @return ContextRUIS
	 */
	public RuisHandler addContextRUIS(RuisHandler cRuis) {
		// ChannelsSet ctemp = consequentChannel.getConChannelsSet(c);
		contextRuisSet.putIn(cRuis);
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
	protected RuisHandler createContextRUISNonShared(int contextID) {
		return new RuleUseInfoSet(contextID, false);
	}

	/**
	 * Returns the contextSet type that is used in-case the
	 * antecedents/arguments share some variables. </br>
	 * <b>SIndex.PTree: </b> in AndNode </br>
	 * <b>SIndex.RUIS: </b> in other rule nodes
	 * 
	 * @return byte
	 */
	protected byte getSIndexContextType() {
		return SIndex.RUIS;
	}

	/**
	 * Returns the class of the ContextRUI to be used in the GeneralSindexing
	 * 
	 * @return
	 */
	protected NodeSet getPatternNodes() {
		return antNodesWithVars;
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
	public void splitToNodesWithVarsAndWithout(NodeSet allNodes, NodeSet withVars, NodeSet WithoutVars) {
		for (int i = 0; i < allNodes.size(); i++) {
			Node n = allNodes.getNode(i);
			if (isConstantNode(n))
				WithoutVars.addNode(n);
			else
				withVars.addNode(n);
		}
	}

	/**
	 * Combines the Constant RuleUseInfo (RUI) rui with the existed Constant RUI
	 * that is Associated with the Context context.
	 * 
	 * @param context
	 *            The Context that is associated with the RUI sent.
	 * @param rui
	 *            The RUI to be combined with the existed one.
	 * @return the resulted from combining the existed RUI with the new one
	 * @throws NullPointerException
	 *             when there is something went wrong in combine the existed
	 *             constant RUI with the new RUI
	 */

	public RuleUseInfo addConstantRuiToContext(int context, RuleUseInfo rui) {
		RuleUseInfo tRui = contextConstantRUI.get(context);
		if (tRui != null)
			tRui = rui.combine(tRui);
		else
			tRui = rui;
		if (tRui == null)
			throw new NullPointerException(
					"The existed RUI could not be merged " + "with the given rui so check your code again");
		contextConstantRUI.put(context, tRui);
		return tRui;
	}

	public RuleUseInfo getConstantRui(Context con) {
		RuleUseInfo tRui = contextConstantRUI.get(con.getId());
		return tRui;
	}

	// /**
	// * Gets the number of positive constant reports in the Context context
	// *
	// * @param context
	// * Context
	// * @return int
	// */
	// public int getPositiveCount(Context context) {
	// if (contextConstantRUI.containsKey(context.getId()))
	// return contextConstantRUI.get(context.getId()).getPosCount();
	// return 0;
	// }

	// /**
	// * Gets the number of negative constant reports in the Context context
	// *
	// * @param context
	// * Context
	// * @return int
	// */
	// public int getNegativeCount(Context context) {
	// if (contextConstantRUI.containsKey(context.getId()))
	// return contextConstantRUI.get(context.getId()).getNegCount();
	// return 0;
	// }

	public RuleUseInfo getConstantRUI(int context) {
		return contextConstantRUI.get(context);
	}

	/**
	 * Checks if the given Node is a constant node i.e. does not have
	 * freeVariables
	 * 
	 * @param n
	 *            Node
	 * @return boolean
	 */
	public static boolean isConstantNode(Node n) {
		return !(n instanceof NodeWithVar) || n instanceof RuleNode || ((NodeWithVar) n).getFreeVariables().isEmpty();
	}

	@Override
	public void processRequests() {
		for (Channel currentChannel : outgoingChannels) {
			if (currentChannel instanceof RuleToConsequentChannel) {
				LinkedList<VariableNode> variablesList = this.getFreeVariables();
				if (variablesList.isEmpty()) {
					Proposition semanticType = (Proposition) this.getSemantic();
					if (semanticType.isAsserted(SNeBR.getContextByID(currentChannel.getContextID()))) {
						NodeSet antecedentNodeSet = this.getDownAntNodeSet();
						NodeSet toBeSentTo = new NodeSet();
						for (Node currentNode : antecedentNodeSet) {
							if (currentNode == currentChannel.getRequester()) {
								continue;
							}
							// TODO Akram: if not yet been requested for this
							// instance
							if (true) {
								toBeSentTo.addNode(currentNode);
							}
						}
						sendRequests(toBeSentTo, currentChannel.getFilter().getSubstitution(),
								currentChannel.getContextID(), ChannelTypes.RuleAnt);
					}
				} else if (true)

				{
					// TODO Akram: there are free variables but each is bound
				} else if (true)

				{
					// TODO Akram: there are free variable
				}

			} else {
				super.processSingleRequest(currentChannel);
			}
		}
	}

	@Override
	public void processReports() {
		for (Channel currentChannel : incomingChannels) {
			ArrayList<Report> channelReports = currentChannel.getReportsBuffer();
			for (Report currentReport : channelReports) {
				if (currentChannel instanceof AntecedentToRuleChannel) {
					applyRuleHandler(currentReport, currentChannel.getReporter());
				}
			}
			currentChannel.clearReportsBuffer();
		}
	}
}
