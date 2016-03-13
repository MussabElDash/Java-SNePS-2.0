package snip.Rules.RuleNodes;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import SNeBR.Support;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;
import snip.Rules.DataStructures.RuisHandler;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.PTree;
import snip.Rules.DataStructures.RuleUseInfo;
import snip.Rules.DataStructures.RuleUseInfoSet;
import snip.Rules.DataStructures.SIndex;

public class AndNode extends RuleNode {
	/**
	 * A Map that keeps track of the Ruis that was not reported due to the
	 * missing reports of the constant nodes
	 */
	private Hashtable<Integer, Set<RuleUseInfo>> contextRuiNotSent;
	private int ant, cq;

	public AndNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		contextRuiNotSent = new Hashtable<Integer, Set<RuleUseInfo>>();
		NodeSet antNodes = this.getDownNodeSet("&ant");
		ant = antNodes.size();
		cq = this.getDownNodeSet("cq").size();
		this.processNodes(antNodes);
	}

	@Override
	public void applyRuleHandler(Report report, Node signature) {
		if (report.isNegative()) {
			return;
		}

		FlagNode fn = new FlagNode(signature, report.getSupports(), 1);
		FlagNodeSet fns = new FlagNodeSet();
		fns.putIn(fn);
		RuleUseInfo rui = new RuleUseInfo(report.getSubstitutions(), 1, 0, fns);

		int context = report.getContextID();
		if (isConstantNode(signature)) {
			addConstantRuiToContext(context, rui);
			sendSavedRUIs(context);
			return;
		}

		RuisHandler crtemp = null;
		if (this.getContextRUISSet().hasContext(context)) {
			crtemp = this.getContextRUISSet().getContextRUIS(context);
		} else {
			crtemp = addContextRUIS(context);
		}
		RuleUseInfoSet res = crtemp.insertRUI(rui);
		if (res == null) {
			res = new RuleUseInfoSet();
		}
		for (RuleUseInfo tRui : res) {
			if (tRui.getPosCount() == this.antsWithVarsNumber)
				addNotSentRui(tRui, context);
		}
		sendSavedRUIs(context);
	}

	@Override
	protected RuisHandler createContextRUISNonShared(int c) {
		PTree pTree = new PTree(c);
		RuisHandler cr = this.addContextRUIS(pTree);
		pTree.buildTree(antNodesWithVars);
		return cr;
	}

	@Override
	protected byte getSIndexContextType() {
		return SIndex.PTREE;
	}

	private void sendSavedRUIs(int contextID) {
		RuleUseInfo addedConstant = getConstantRUI(contextID);
		if (addedConstant == null && this.antsWithoutVarsNumber != 0)
			return;
		if (addedConstant != null && addedConstant.getPosCount() != this.antsWithoutVarsNumber)
			return;
		Set<RuleUseInfo> ruis = contextRuiNotSent.get(contextID);
		if (ruis == null) {
			sendRui(addedConstant, contextID);
			return;
		}
		RuleUseInfo combined;
		for (Iterator<RuleUseInfo> iter = ruis.iterator(); iter.hasNext();) {
			RuleUseInfo info = iter.next();
			iter.remove();
			combined = info.combine(addedConstant);
			if (combined == null)
				throw new NullPointerException(
						"The Constant RUI could not be merged " + "with the non-constant one so check your code again");
			sendRui(combined, contextID);
		}
	}

	private void addNotSentRui(RuleUseInfo tRui, int contextID) {
		Set<RuleUseInfo> set = contextRuiNotSent.get(contextID);
		if (set == null) {
			set = new HashSet<RuleUseInfo>();
			contextRuiNotSent.put(contextID, set);
		}
		set.add(tRui);
	}

	@Override

	protected void sendRui(RuleUseInfo tRui, int contextID) {
		addNotSentRui(tRui, contextID);
		if (tRui.getPosCount() != this.antsWithVarsNumber + this.antsWithoutVarsNumber)
			return;
		Set<Support> originSupports = ((Proposition) this.getSemantic()).getOriginSupport();
		Report reply = new Report(tRui.getSub(), tRui.getSupport(originSupports), true, contextID);
		broadcastReport(reply);
	}

	public int getant() {
		return ant;
	}

	public int getCq() {
		return cq;
	}

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("&ant");
	}

	@Override
	public void clear() {
		super.clear();
		contextRuiNotSent.clear();
	}

}
