package SNeBR;

import java.util.HashSet;
import java.util.Iterator;

import sneps.Nodes.PropositionNode;

public class PropositionSet implements Iterable<PropositionNode>{
	public HashSet<PropositionNode> propositions;

	public PropositionSet() {
		this.propositions = new HashSet<PropositionNode>();
	}

	public PropositionSet(HashSet<PropositionNode> p) {
		this.propositions = p;
	}

	public void addPropSet(PropositionSet propSet) {
		if (propSet != null)
			this.propositions.addAll(propSet.propositions);
	}

	public void removePropSet(PropositionSet propSet) {
		this.propositions.removeAll(propSet.propositions);
	}
	
	public void addProposition(PropositionNode node) {
		propositions.add(node);
	}

	public boolean assertedInContext(Context context) {
		if (context.hypothesisSet.propositions.containsAll(this.propositions))
			return true;
		return false;
	}

	public HashSet<PropositionSet> split() {
		HashSet<PropositionSet> split = new HashSet<PropositionSet>();
		int i = 0;
		PropositionSet newSet1 = new PropositionSet();
		PropositionSet newSet2 = new PropositionSet();
		PropositionNode prop;
		for (Iterator<PropositionNode> iterator1 = this.propositions.iterator(); iterator1
				.hasNext();) {
			prop = iterator1.next();
			if (this.propositions.size() / 2 <= i) {
				newSet2.propositions.add(prop);
			} else {
				newSet1.propositions.add(prop);
			}
			i++;
		}
		split.add(newSet1);
		split.add(newSet2);
		return split;
	}

	public HashSet<PropositionNode> getPropositions() {
		return propositions;
	}

	public void setPropositions(HashSet<PropositionNode> propositions) {
		this.propositions = propositions;
	}

	@Override
	public Iterator<PropositionNode> iterator() {
		return propositions.iterator();
	}

}
