/**
 * @className Node.java
 * 
 * @ClassDescription A node is the building block of the network. A node 
 * 	has both a semantic and a syntactic type. The Node class has an instance 
 * 	of the semantic class representing its semantic type and an instance of 
 * 	the syntactic class representing the its syntactic type. The Node class 
 * 	is implemented as a 4-tuple (syntactic, semantic, count and id)
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Nodes;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import sneps.Network;
import sneps.Cables.DownCableSet;
import sneps.Cables.UpCable;
import sneps.Cables.UpCableSet;
import sneps.SemanticClasses.Entity;
import sneps.SyntaticClasses.Term;
import sneps.match.LinearSubstitutions;
import sneps.match.Substitutions;
import snip.AntecedentToRuleChannel;
import snip.Channel;
import snip.ChannelSet;
import snip.ChannelTypes;
import snip.MatchChannel;
import snip.Pair;
import snip.Report;
import snip.RuleToConsequentChannel;
import snip.Runner;
import SNeBR.Context;
import SNeBR.PropositionSet;

public class Node {

	/**
	 * An instance of a class from the syntactic (Term) hierarchy that
	 * represents the syntactic type of the current node.
	 */
	private Term syntactic;

	/**
	 * An instance of a class from the semantic (Entity) hierarchy that
	 * represents the semantic type of the current node.
	 */
	private Entity semantic;

	/**
	 * A static variable that counts the number of nodes that were built in the
	 * network.
	 */
	private static int count = 0;

	/**
	 * The id of the current node.
	 */
	private int id;

	// TODO Akram
	protected ChannelSet outgoingChannels;
	protected ChannelSet incomingChannels;
	protected Set<Report> knownInstances;

	/**
	 * The first constructor of this class.
	 * 
	 * This constructor creates a node using the syntactic and semantic
	 * instances passed as parameters.
	 * 
	 * @param syn
	 *            an instance of a syntactic class representing the syntactic
	 *            type of the node that will be created.
	 * @param sem
	 *            an instance of a semantic class representing the semantic type
	 *            of the node that will be created.
	 * 
	 */
	public Node(Term syn, Entity sem) {
		this.syntactic = syn;
		this.semantic = sem;
		id = count;
		count++;
		outgoingChannels = new ChannelSet();
		incomingChannels = new ChannelSet();
		knownInstances = new HashSet<Report>();
	}

	/**
	 * The second constructor of this class.
	 * 
	 * This constructor first creates an instance of the semantic class
	 * specified and an instance of the syntactic class specified and then
	 * creates the new node using those two newly created instances.
	 * 
	 * @param syn
	 *            the name of the syntactic class that will be created to
	 *            represent the syntactic type of the node that will be created.
	 * @param sem
	 *            the name of the semantic class that will be created to
	 *            represent the syntactic type of the node that will be created.
	 * @param name
	 *            the name or the label of the node that will be created. (this
	 *            will be passed as a parameter to the syntactic class
	 *            constructor while creating an instance of the syntactic class
	 *            specified.)
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Node(String syn, String sem, String name) throws Exception {
		Class s = Class.forName("sneps.SyntaticClasses." + syn);
		Constructor con = s.getConstructor(new Class[] { String.class });
		this.syntactic = (Term) con.newInstance(name);
		Class s2 = Class.forName("sneps.SemanticClasses." + sem);
		this.semantic = (Entity) s2.newInstance();
		id = count;
		count++;
		outgoingChannels = new ChannelSet();
		incomingChannels = new ChannelSet();
		knownInstances = new HashSet<Report>();
	}

	// TODO wala tb2a public 3ady?
	// for molecular nodes

	/**
	 * The third constructor of this class.
	 * 
	 * This constructor is used to create a molecular node by providing the
	 * names of the syntactic and semantic classes in addition to the name and
	 * the down cable set of the node that will be created.
	 * 
	 * @param syn
	 *            the name of the syntactic class that will be created to
	 *            represent the syntactic type of the molecular node that will
	 *            be created.
	 * @param sem
	 *            the name of the semantic class that will be created to
	 *            represent the syntactic type of the molecular node that will
	 *            be created.
	 * @param name
	 *            the name or the label of the molecular node that will be
	 *            created. (this will be passed as a parameter to the syntactic
	 *            class constructor while creating an instance of the syntactic
	 *            class specified.)
	 * 
	 * @param dCableSet
	 *            the down cable set of the molecular node that will be created.
	 *            (this will be passed as a parameter to the syntactic class
	 *            constructor while creating an instance of the syntactic class
	 *            specified.)
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Node(String syn, String sem, String name, DownCableSet dCableSet)
			throws Exception {
		Class s = Class.forName("sneps.SyntaticClasses." + syn);
		Constructor con = s.getConstructor(new Class[] { String.class,
				DownCableSet.class });
		this.syntactic = (Term) con.newInstance(name, dCableSet);
		Class s2 = Class.forName("sneps.SemanticClasses." + sem);
		this.semantic = (Entity) s2.newInstance();
		id = count;
		count++;
		outgoingChannels = new ChannelSet();
		incomingChannels = new ChannelSet();
		knownInstances = new HashSet<Report>();
	}

	/**
	 * 
	 * @return the instance of syntactic class representing the syntactic type
	 *         of the current node.
	 */
	public Term getSyntactic() {
		return this.syntactic;
	}

	/**
	 * 
	 * @return the instance of semantic class representing the semantic type of
	 *         the current node.
	 */
	public Entity getSemantic() {
		return this.semantic;
	}

	/**
	 * 
	 * @return the simple name of syntactic class representing the syntactic
	 *         type of the current node.
	 */
	public String getSyntacticType() {
		return this.syntactic.getClass().getSimpleName();
	}

	/**
	 * 
	 * @return the simple name of the super class of the syntactic class
	 *         representing the syntactic type of the current node.
	 */
	public String getSyntacticSuperClass() {
		return this.syntactic.getClass().getSuperclass().getSimpleName();
	}

	/**
	 * 
	 * @return the simple name of semantic class representing the semantic type
	 *         of the current node.
	 */
	public String getSemanticType() {
		return this.semantic.getClass().getSimpleName();
	}

	/**
	 * 
	 * @return the simple name of the super class of the semantic class
	 *         representing the semantic type of the current node.
	 */
	public String getSemanticSuperClass() {
		return this.semantic.getClass().getSuperclass().getSimpleName();
	}

	/**
	 * 
	 * @return the id of the current node.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * 
	 * @return the name or the label of the current node.
	 */
	public String getIdentifier() {
		return this.syntactic.getIdentifier();
	}

	/**
	 * 
	 * @return the up cable set of the current node.
	 */
	public UpCableSet getUpCableSet() {
		return this.syntactic.getUpCableSet();
	}

	/**
	 * 
	 * @return true if the current node is a temporary node, and false
	 *         otherwise.
	 */
	public boolean isTemp() {
		return this.syntactic.isTemp();
	}

	/**
	 * 
	 * @param temp
	 *            the boolean value that indicates whether or not the node is a
	 *            temporary node.
	 */
	public void setTemp(boolean temp) {
		this.syntactic.setTemp(temp);
	}

	/**
	 * 
	 * @return a node set containing all the parent nodes of the current node.
	 *         (whether direct or indirect parent nodes.)
	 */
	public NodeSet getParentNodes() {
		return this.syntactic.getParentNodes();
	}

	/**
	 * This method overrides the default toString method inherited from the
	 * Object class.
	 */
	@Override
	public String toString() {
		return this.syntactic.toString();
	}

	/**
	 *
	 * @return the count of the nodes that were built in the network.
	 */
	public static int getCount() {
		return count;
	}

	/**
	 * This method is used to adjust the count of nodes in the network after
	 * many deletions (this method is used by 'compact' method in the 'Network'
	 * class.)
	 * 
	 * @param c
	 *            the new count of the nodes in the system.
	 * 
	 */
	public static void setCount(int c) {
		count = c;
	}

	/**
	 * This method is used to adjust the id of the current node after compaction
	 * process. (this method is used by 'compact' method in the 'Network'
	 * class.)
	 * 
	 * @param id
	 *            the new id of the current node.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * This method overrides the default equals method inherited from the Object
	 * class.
	 * 
	 * @param obj
	 *            an Object that is to be compared to the current node to check
	 *            whether they are equal.
	 * 
	 * @return true if the given object is an instance of the Node class and has
	 *         the same name as the current node, and false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().getSimpleName().contains("Node"))
			return false;
		if (!this.getIdentifier().equals(((Node) obj).getIdentifier()))
			return false;
		return true;
	}

	// ////////////////////////////////////main method that was used for testing
	// //////////////////////////////////////

	// public static void main(String[] args) throws Exception{
	// // Testing the new approaches
	// System.out.println("Testing the 1st approach:");
	// Base b = new Base("one");
	// Entity e = new Entity();
	// Individual i = new Individual();
	// Node trialNode = new Node(b, i);
	// System.out.println("Trial: ");
	// System.out.println(trialNode.syntactic.getClass().getSimpleName());
	// System.out.println(trialNode.semantic.getClass().getSimpleName());
	// //System.out.println(trialNode.semantic.);
	// System.out.println(trialNode.syntactic.getIdentifier());
	// System.out.println(trialNode.syntactic.isTemp());
	// System.out.println("calling method isTemp directly");
	// trialNode.syntactic.setTemp(true);
	// System.out.println(trialNode.syntactic.isTemp());
	// // trying the difference between my old approach and my new approach
	// // System.out.println(nd.syntacticClass.isTemp());
	// System.out.println("Results of the second node");
	// Variable v = new Variable("two");
	// Node NodeTwo = new Node(v, e);
	// System.out.println(NodeTwo.syntactic.getClass().getSimpleName());
	// System.out.println(NodeTwo.syntactic.getIdentifier());
	// System.out.println("Moved :D");
	// System.out.println("Testing the 2nd approach:");
	// Node NodeThree = new Node("Variable", "Individual", "three");
	// System.out.println(NodeThree.syntactic.getClass().getSimpleName());
	// System.out.println(NodeThree.getId());
	// System.out.println(NodeThree.syntactic.getIdentifier());
	// System.out.println(NodeThree.semantic.getClass().getSimpleName());
	// System.out.println(NodeThree.getSemanticSuperClass());
	// System.out.println(NodeThree.getSyntacticSuperClass());
	//
	// }

	public void processSingleReport(Channel currentChannel) {
		ArrayList<Report> reports = currentChannel.getReportsBuffer();
		for (Report currentReport : reports) {
			Report alteredReport = new Report(currentReport.getSubstitutions(),
					currentReport.getSupport(), currentReport.getSign(),
					currentReport.getContextID());
			if (knownInstances.contains(alteredReport)) {
				continue;
			}
			for (Channel outChannel : outgoingChannels)
				outChannel.addReport(alteredReport);
		}
	}

	public void processReports() {
		for (Channel outChannel : outgoingChannels)
			processSingleReport(outChannel);
	}

	public void broadcastReport(Report report) {
		for (Channel outChannel : outgoingChannels)
			outChannel.addReport(report);
	}

	public boolean sendReport(Report report, Channel channel) {
		if (channel.addReport(report)) {
			return true;
		}
		return false;
	}

	public void processSingleRequest(Channel currentChannel) {

		PropositionSet propSet = new PropositionSet();
		propSet.addProposition((PropositionNode) this);
		// TODO AKram: call the getContextByID from SNeBR
		Context desiredContext = fake();
		if (desiredContext == null || propSet.assertedInContext(desiredContext)) {
			// TODO change the subs to hashsubs
			Report reply = new Report(new LinearSubstitutions(), null, true,
					currentChannel.getContextID());
			knownInstances.add(reply);
			broadcastReport(reply);
		} else {
			boolean sentAtLeastOne = false;
			for (Report currentReport : knownInstances) {
				sentAtLeastOne = sendReport(currentReport, currentChannel);
			}

			// TODO Akram: passed the filter subs to isWhQuest, is that correct
			// ?
			if (!sentAtLeastOne
					|| isWhQuestion(currentChannel.getFilter()
							.getSubstitution())) {
				if (!alreadyWorking(currentChannel)) {
					UpCable consequentCable = this.getUpCableSet().getUpCable(
							"cq");
					if (consequentCable != null) {
						NodeSet dominatingRules = consequentCable.getNodeSet();
						int dominatingRulesCount = dominatingRules.size();
						Set<Node> toBeSentTo = new HashSet<Node>();
						for (int i = 0; i < dominatingRulesCount; ++i) {
							Node currentNode = dominatingRules.getNode(i);
							toBeSentTo.add(currentNode);
						}
						sendRequests(toBeSentTo, currentChannel.getContextID(),
								ChannelTypes.RuleCons);
						// TODO Akram: resources available ?
						if (!(currentChannel instanceof MatchChannel)) {
							// Sending requests to matched channels nodes
							// TODO Ahmed Akram: call network.match
							// ArrayList<Pair> matchedNodes =
							// Network.match(this);
							toBeSentTo.clear();

							// TODO Akram send to all the matched nodes

							sendRequests(toBeSentTo,
									currentChannel.getContextID(),
									ChannelTypes.MATCHED);
						}
					}
				}
			}

		}

	}

	public void processRequests() {
		for (Channel outChannel : outgoingChannels)
			processSingleRequest(outChannel);
	}

	public void sendRequests(ArrayList<Pair> list, int conetxtID,
			ChannelTypes channelType) {
		for (Pair currentPair : list) {
			Substitutions switchSubs = currentPair.getSwitch();
			Substitutions filterSubs = currentPair.getFilter();
			Channel newChannel;
			if (channelType == ChannelTypes.MATCHED) {
				newChannel = new MatchChannel(switchSubs, filterSubs,
						conetxtID, currentPair.getNode(), true);
			} else if (channelType == ChannelTypes.RuleAnt) {
				newChannel = new AntecedentToRuleChannel(switchSubs,
						filterSubs, conetxtID, currentPair.getNode(), true);
			} else {
				newChannel = new RuleToConsequentChannel(switchSubs,
						filterSubs, conetxtID, currentPair.getNode(), true);
			}
			incomingChannels.addChannel(newChannel);
			Runner.addToLowQueue(currentPair.getNode());
			currentPair.getNode().receiveRequest(newChannel);
		}
	}

	public void sendRequests(Set<Node> ns, int contextID,
			ChannelTypes channelType) {
		// for (Node sentTo : ns) {
		//
		// // TODO Akram: what is a temp node ? h
		// if (sentTo.isTemp())
		// continue;
		//
		// Filter f = new Filter();
		// Switch s = new Switch();
		// Channel newChannel;
		// if (channelType == ChannelTypes.MATCHED) {
		// newChannel = new MatchChannel(f, s, c, this, true);
		// } else if (channelType == ChannelTypes.RuleAnt) {
		// newChannel = new AntecedentToRuleChannel(f, s, c, this, true);
		// } else {
		// newChannel = new RuleToConsequentChannel(f, s, c, this, true);
		// }
		//
		// incomingChannels.add(newChannel);
		// Runner.addToLowQueue(this);
		//
		// sentTo.receiveRequest(newChannel);
		// }
	}

	public void receiveRequest(Channel channel) {
		outgoingChannels.addChannel(channel);
		Runner.addToLowQueue(this);
	}

	public boolean alreadyWorking(Channel channel) {
		// TODO Akram: implement this
		return false;
	}

	public boolean isWhQuestion(Substitutions sub) {
		if (!this.getIdentifier().equalsIgnoreCase("patternnode"))
			return false;

		PatternNode node = (PatternNode) this;
		LinkedList<VariableNode> variables = node.getFreeVariables();

		for (int i = 0; i < variables.size(); i++) {
			Node termNode = sub.term(variables.get(i));
			if (termNode == null
					|| (!termNode.getIdentifier().equalsIgnoreCase("basenode")))
				return true;

		}
		return false;
	}

	Context fake() {
		return null;
	}

	public void deduce() {

	}
}
