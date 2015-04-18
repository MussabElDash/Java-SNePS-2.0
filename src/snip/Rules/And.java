package snip.Rules;

import java.util.Iterator;
import java.util.LinkedList;

import sneps.Nodes.NodeSet;
import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;
import snip.Rules.DataStructures.ContextRUIS;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.Ptree;
import snip.Rules.DataStructures.RuleUseInfo;
import snip.Rules.DataStructures.RuleUseInfoSet;
import snip.Rules.DataStructures.Sindexing;
import SNeBR.Context;

public class And extends Rule {

	public And(Molecular syn, Proposition sym) {
		super(syn, sym);
	}

	@Override
	public void resetRule() {
		NodeSet antNodes = this.getDownNodeSet("&ant");
		this.antNodesWithoutVars = new NodeSet();
		this.antNodesWithVars = new NodeSet();
		this.splitToNodesWithVarsAndWithout(antNodes, antNodesWithVars,
				antNodesWithoutVars);
		this.shareVars = this.allShareVars(antNodesWithVars);
		if (shareVars) {
			PatternNode pn = (PatternNode) antNodesWithVars.getNode(0);
			LinkedList<VariableNode> varNodes = pn.getFreeVariables();
			vars = new int[varNodes.size()];
			Iterator<VariableNode> varIter = varNodes.iterator();
			for (int i = 0; i < vars.length && varIter.hasNext(); i++) {
				vars[i] = varIter.next().getId();
			}
		}
	}

	@Override
	public void applyRuleHandler(Report report) {
		// TODO Auto-generated method stub
		if (report.isNegative())
			return;

		Context context = report.getContext();
		FlagNode fn = new FlagNode(report.getSignature(), report.getSupport(),
				1);
		FlagNodeSet fns = new FlagNodeSet();
		fns.putIn(fn);
		RuleUseInfo rui = new RuleUseInfo(report.getSubstituions(), 1, 0, fns);
		ContextRUIS crtemp = null;
		if (this.getContextRUISSet().hasContext(context)) {
			crtemp = this.getContextRUISSet().getContextRUIS(context);
		} else {
			crtemp = addContextRUIS(context);
		}
		RuleUseInfoSet res = null;
		if (shareVars) {
			Sindexing scrtemp = (Sindexing) crtemp;
			res = scrtemp.insert(rui, vars);
		} else {
			Ptree pcrtemp = (Ptree) crtemp;
			res = pcrtemp.insert(rui);
			if (res == null) {
				res = new RuleUseInfoSet();
			}
		}
		
	}

}
