package SNeBR;

import java.util.HashSet;

import sneps.Nodes.PropositionNode;


public class Restriction {
	Context restrictor;
	PropositionSet removedSet;
	HashSet<PropositionSet> restrictions;
	
	public Restriction(Context r,PropositionSet p){
		this.restrictor=r;
		this.removedSet=p;
	}
	public Restriction(Context r,PropositionSet p,HashSet<PropositionSet> re){
		this.restrictor=r;
		this.removedSet=p;
		this.restrictions=re;
	}
	public boolean canBeAddedHelper(PropositionNode prop) {
		if (!this.restrictor.hypothesisSet.propositions.contains(prop)
				| !removedSet.propositions.contains(prop)
				| (removedSet.propositions.contains(prop) && removedSet.propositions
						.size() > 1))
			return true;
		else
			return false;
	}

	public void addRemoved(PropositionNode newRemoved) {
		if (!this.removedSet.propositions.contains(newRemoved)
				&& this.restrictor.hypothesisSet.propositions
						.contains(newRemoved))
			this.removedSet.propositions.add(newRemoved);

	}
	
    public void removeAdded (PropositionNode prop)
    {
            if (this.removedSet.propositions.contains(prop))
                    this.removedSet.propositions.remove(prop);
    }
	public Context getRestrictor() {
		return restrictor;
	}
	public void setRestrictor(Context restrictor) {
		this.restrictor = restrictor;
	}
	public PropositionSet getRemovedSet() {
		return removedSet;
	}
	public void setRemovedSet(PropositionSet removedSet) {
		this.removedSet = removedSet;
	}
	public HashSet<PropositionSet> getRestrictions() {
		return restrictions;
	}
	public void setRestrictions(HashSet<PropositionSet> restrictions) {
		this.restrictions = restrictions;
	}

}
