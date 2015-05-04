package snip.Rules.RuleNodes;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;
import snip.Rules.DataStructures.ContextRUIS;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.PTree;
import snip.Rules.DataStructures.RuleUseInfo;
import snip.Rules.DataStructures.RuleUseInfoSet;
import snip.Rules.DataStructures.SIndexing;
import snip.Rules.Interfaces.NodeWithVar;
import SNeBR.Context;

public class AndNode extends RuleNode {
	/**
	 * A Map that keeps track of the Ruis that was not reported due to the
	 * missing reports of the constant nodes
	 */
	private Hashtable<Integer, Set<RuleUseInfo>> contextRuiNotSent;

	public AndNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		contextRuiNotSent = new Hashtable<Integer, Set<RuleUseInfo>>();
		NodeSet antNodes = this.getDownNodeSet("&ant");
		this.processNodes(antNodes);
	}

	@Override
	public void applyRuleHandler(Report report, Node signature) {
		if (report.isNegative()) {
			return;
		}

		Context context = report.getContext();
		if (!(signature instanceof NodeWithVar)) {
			this.addConstantToContext(context, signature, true);
			if (this.getPositiveCount(context) != this.antsWithoutVarsNumber)
				sendReports(report.getContext());
			return;
		}

		FlagNode fn = new FlagNode(signature, report.getSupport(), 1);
		FlagNodeSet fns = new FlagNodeSet();
		fns.putIn(fn);
		RuleUseInfo rui = new RuleUseInfo(report.getSubstituions(), 1, 0, fns);
		ContextRUIS crtemp = null;
		if (this.getContextRUISSet().hasContext(context)) {
			crtemp = this.getContextRUISSet().getContextRUIS(context);
		} else {
			crtemp = addContextRUIS(context);
		}
		if (shareVars) {
			SIndexing scrtemp = (SIndexing) crtemp;
			RuleUseInfo ruiRes = scrtemp.insert(rui, vars);
			sendRui(ruiRes, context);
			return;
		}
		PTree pcrtemp = (PTree) crtemp;
		RuleUseInfoSet res = pcrtemp.insert(rui);
		if (res == null) {
			res = new RuleUseInfoSet();
		}
		for (RuleUseInfo tRui : res) {
			sendRui(tRui, context);
		}
	}

	@Override
	protected ContextRUIS createContextRUISNonShared(Context c) {
		PTree pTree = new PTree(c);
		ContextRUIS cr = this.addContextRUIS(pTree);
		pTree.buildTree(antNodesWithVars);
		return cr;
	}

	private void sendReports(Context context) {
		Iterator<RuleUseInfo> iter = contextRuiNotSent.get(context.getId())
				.iterator();
		while (iter.hasNext()) {
			RuleUseInfo info = iter.next();
			iter.remove();
			sendRui(info, context);
		}
	}

	private void addNotSentRui(RuleUseInfo tRui, Context context) {
		Set<RuleUseInfo> set = contextRuiNotSent.get(context.getId());
		if (set == null) {
			set = new HashSet<RuleUseInfo>();
			contextRuiNotSent.put(context.getId(), set);
		}
		set.add(tRui);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, Context context) {
		// TODO Auto-generated method stub
		if (tRui.getPosCount() == this.antsWithVarsNumber) {
			if (this.getPositiveCount(context) != this.antsWithoutVarsNumber) {
				addNotSentRui(tRui, context);
				return;
			}
			throw new UnsupportedOperationException();
		}
	}

}
