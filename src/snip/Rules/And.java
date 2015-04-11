package snip.Rules;

import java.util.Iterator;
import java.util.LinkedList;

import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.Report;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.RuleUseInfo;
import SNeBR.Context;

public class And extends Rule {

	public And(Molecular syn, Proposition sym) {
		super(syn, sym);
		antNodes = getDownNodeSet("&ant");
		antsNumber = antNodes.size();
		shareVars = allShareVars(antNodes);
		if (shareVars) {
			PatternNode pn = (PatternNode) antNodes.getNode(0);
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
		if(report.isPositive()){
			Context context = report.getContext();
			FlagNode fn = new FlagNode(report.getSignature(), report.getSupport(), 1);
			FlagNodeSet fns = new FlagNodeSet();
			RuleUseInfo rui = new RuleUseInfo(report.getSubstituions(), 1, 0, fns);
		}
	}

}
