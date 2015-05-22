package SNeBR;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sneps.Nodes.PropositionNode;

public class Support {
	Context originSet;

	public Support(PropositionNode proposition) {
		this.originSet = new Context(proposition);
	}

	public Support() {
		this.originSet = new Context();
	}

	public Support(Context c) {
		this.originSet = c;
	}

	public boolean assertedInContext(Context context) {
		if (context.hypothesisSet.propositions
				.containsAll(this.originSet.hypothesisSet.propositions))
			return true;
		return false;
	}

	public void addRestriction(Context restrictor, PropositionSet removed,
			HashSet<Support> supportSet) {
		HashSet<PropositionSet> restrictions = new HashSet<PropositionSet>();
		for (Iterator<Support> iterator = supportSet.iterator(); iterator
				.hasNext();) {
			restrictions.add(iterator.next().originSet.hypothesisSet);
		}
		PropositionSet newRemoved = new PropositionSet(removed.propositions);
		Restriction newRestriction = new Restriction(restrictor, newRemoved,
				restrictions);
		this.originSet.restrictionSet.add(newRestriction);
	}

	public void addToOriginSet(PropositionNode proposition) {
		this.originSet.addToContext(proposition);
	}

	public Context getOriginSet() {
		return originSet;
	}

	public void setOriginSet(Context originSet) {
		this.originSet = originSet;
	}

	public Support combine(Support support) {
		// TODO Mussab Combine Two Supports
		Context resContext = new Context();
		for (PropositionNode pn : originSet.getHypothesisSet())
			resContext.addToContext(pn);
		for (PropositionNode pn : support.getOriginSet().getHypothesisSet())
			resContext.addToContext(pn);
		return new Support(resContext);
	}

	public static Set<Support> combine(Set<Support> originSupports,
			Set<Support> supports) {
		if (originSupports.isEmpty())
			return supports;
		if (supports.isEmpty())
			return originSupports;
		Set<Support> res = new HashSet<Support>();
		for (Support s1 : originSupports)
			for (Support s2 : supports)
				res.add(s1.combine(s2));
		return res;
	}
}
