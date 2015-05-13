package sneps.SemanticClasses;

import java.util.HashSet;
import java.util.Iterator;

import sneps.Nodes.PropositionNode;
import SNeBR.Context;
import SNeBR.ContextSet;
import SNeBR.PropositionSet;
import SNeBR.Support;

public class Proposition extends Entity {
	HashSet<Support> originSupport;
	ContextSet contextSet;
	HashSet<Support> justificationSet;
	HashSet<Proposition> justifiedSet;
	boolean updated;

	public Proposition() {
		originSupport = new HashSet<Support>();
		contextSet = new ContextSet();
		justificationSet = new HashSet<Support>();
		justifiedSet = new HashSet<Proposition>();
		updated = false;
	}

	public HashSet<Support> getOrigin() {
		return this.originSupport;
	}

	public boolean isAsserted(Context context) {
		if (this.contextSet.contains(context) || checkForValidSupport(context))
			return true;
		return false;
	}

	public boolean checkForValidSupport(Context context) {
		for (Iterator<Support> iterator = this.originSupport.iterator(); iterator
				.hasNext();) {
			Support support = iterator.next();
			if (!support.getOriginSet().getHypothesisSet().propositions
					.isEmpty()
					&& context.getHypothesisSet().propositions
							.containsAll(support.getOriginSet()
									.getHypothesisSet().propositions)) {
				return true;
			}
		}
		return false;
	}

	public HashSet<Support> getValidSupport(Context context) {
		HashSet<Support> thisSupport = getSupport();
		HashSet<Support> finalSupport = new HashSet<Support>();
		Support support;
		for (Iterator<Support> iterator = thisSupport.iterator(); iterator
				.hasNext();) {
			support = iterator.next();
			if (support.assertedInContext(context))
				finalSupport.add(support);
		}
		return finalSupport;

	}

	public void falsifyUpdateStatus() {
		this.updated = false;
		for (Iterator<Proposition> iterator = this.justifiedSet.iterator(); iterator
				.hasNext();) {
			Proposition temp = iterator.next();
			if (temp.updated)
				temp.falsifyUpdateStatus();
		}
	}

	private HashSet<Support> getSupport() {
		// TODO Auto-generated method stub

		return originSupport;
	}

	public void addContext(Context context) {
		if (!this.contextSet.contains(context))
			this.contextSet.add(context);
	}

	public void addAllContexts(HashSet<Context> contexts) {
		this.contextSet.addAll(contexts);
	}

	public void addSupport(Support support) {
		this.justificationSet.add(support);
		this.updated = false;
		for (Iterator<PropositionNode> iterator = support.getOriginSet()
				.getHypothesisSet().propositions.iterator(); iterator.hasNext();) {
			PropositionNode temp = iterator.next();
			((Proposition) temp.getSemantic()).justifiedSet.add(this);
		}
		this.falsifyUpdateStatus();
	}

	//
	// private void getOrginSet() {
	// Support s2 = new Support();
	// s2.addToOriginSet(this);
	// originSupport.clear();
	// originSupport.add(s2);
	// this.updated = true;
	// if (!this.justificationSet.isEmpty()) {
	// for (Iterator<Support> iterator1 = this.justificationSet.iterator();
	// iterator1
	// .hasNext();) {
	// HashSet<Support> temp = new HashSet<Support>();
	// Support s1 = iterator1.next();
	//
	// for (Iterator<PropositionNode> iterator2 =
	// s1.originSet.hypothesisSet.propositions
	// .iterator(); iterator2.hasNext();) {
	// Proposition p = iterator2.next();
	// temp = multiply(temp, p.getSupport());
	// }
	// if (originSupport.contains(temp))
	// break;
	// originSupport.addAll(temp);
	// }
	// }
	//
	// }

	private HashSet<Support> multiply(HashSet<Support> prop1,
			HashSet<Support> prop2) {
		if (prop1.isEmpty())
			return prop2;
		if (prop2.isEmpty())
			return prop1;

		HashSet<Support> prop3 = new HashSet<Support>();
		Support temp1;
		Support temp2;
		for (Iterator<Support> iterator1 = prop1.iterator(); iterator1
				.hasNext();) {
			temp1 = iterator1.next();
			Support propSet = new Support();
			propSet.getOriginSet().getHypothesisSet()
					.addPropSet(temp1.getOriginSet().getHypothesisSet());
			for (Iterator<Support> iterator2 = prop2.iterator(); iterator2
					.hasNext();) {
				temp2 = iterator2.next();
				propSet.getOriginSet().getHypothesisSet()
						.addPropSet(temp2.getOriginSet().getHypothesisSet());
				Support temp = new Support();
				temp.getOriginSet().getHypothesisSet()
						.addPropSet(propSet.getOriginSet().getHypothesisSet());
				prop3.add(temp);
				propSet.getOriginSet().getHypothesisSet()
						.removePropSet(temp2.getOriginSet().getHypothesisSet());

			}

		}
		return prop3;

	}

	public HashSet<Support> getOriginSupport() {
		return originSupport;
	}

	public void setOriginSupport(HashSet<Support> originSupport) {
		this.originSupport = originSupport;
	}

	public ContextSet getContextSet() {
		return contextSet;
	}

	public void setContextSet(ContextSet contextSet) {
		this.contextSet = contextSet;
	}

	public HashSet<Support> getJustificationSet() {
		return justificationSet;
	}

	public void setJustificationSet(HashSet<Support> justificationSet) {
		this.justificationSet = justificationSet;
	}

	public HashSet<Proposition> getJustifiedSet() {
		return justifiedSet;
	}

	public void setJustifiedSet(HashSet<Proposition> justifiedSet) {
		this.justifiedSet = justifiedSet;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public HashSet<Support> calcOrigin(PropositionSet origins) {
		if (origins.propositions.size() > 2) {
			HashSet<PropositionSet> originsSplit = new HashSet<PropositionSet>();
			originsSplit = origins.split();
			Iterator<PropositionSet> iterator1 = originsSplit.iterator();
			HashSet<Support> support1 = new HashSet<Support>();
			HashSet<Support> support2 = new HashSet<Support>();
			support1 = calcOrigin(iterator1.next());
			support2 = calcOrigin(iterator1.next());
			return multiply(support1, support2);

		} else if (origins.propositions.size() == 2) {
			Proposition firstProp = new Proposition();
			Proposition secondProp = new Proposition();
			Iterator<PropositionNode> iterator1 = origins.propositions
					.iterator();
			firstProp = (Proposition) iterator1.next().getSemantic();
			secondProp = (Proposition) iterator1.next().getSemantic();
			return multiply(firstProp.originSupport, secondProp.originSupport);
		} else {
			Iterator<PropositionNode> iterator1 = origins.propositions
					.iterator();
			return ((Proposition) iterator1.next().getSemantic()).originSupport;
		}

	}

}