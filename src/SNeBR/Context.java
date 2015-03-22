package SNeBR;

import java.util.HashSet;
import java.util.Iterator;

import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.*;
public class Context {
	PropositionSet hypothesisSet;
	HashSet<String> names;
	boolean conflicting;
	HashSet<Restriction> restrictionSet;
	HashSet<Contradiction> contradictions;
	int id;
	private static int count = 1;

	public Context() {
		this.names = new HashSet<String>();
		this.conflicting = false;
		this.restrictionSet = new HashSet<Restriction>();
		this.hypothesisSet = new PropositionSet();
		this.contradictions= new HashSet<Contradiction>();
		this.id = count;
		count++;

	}

	public Context(PropositionNode proposition) {
		this.names = new HashSet<String>();
		this.conflicting = false;
		this.hypothesisSet = new PropositionSet();
		this.hypothesisSet.propositions.add(proposition);
		this.contradictions= new HashSet<Contradiction>();
		this.restrictionSet = new HashSet<Restriction>();
		this.id = count;
		count++;
	}

	public Context(PropositionSet propSet) {
		this.hypothesisSet = new PropositionSet();
		hypothesisSet.addPropSet(propSet);
		this.names = new HashSet<String>();
		this.conflicting = false;
		this.contradictions= new HashSet<Contradiction>();
		this.restrictionSet = new HashSet<Restriction>();
		this.id = count;
		count++;

	}

	public Context(Context context) {
		this.names = new HashSet<String>();
		this.conflicting = context.conflicting;
		this.hypothesisSet = new PropositionSet();
		this.hypothesisSet.addPropSet(context.hypothesisSet);
		this.restrictionSet = new HashSet<Restriction>();
		this.contradictions = context.contradictions;
		this.id = count;
		count++;
		for (Iterator<Restriction> iterator = context.restrictionSet.iterator(); iterator
				.hasNext();) {
			Restriction type = iterator.next();
			Restriction temp = new Restriction(type.restrictor, type.removedSet,type.restrictions);
			this.restrictionSet.add(temp);
		}
	}
	
    public Context(PropositionNode hyp, String name)
    {
            this.hypothesisSet = new PropositionSet();
            hypothesisSet.propositions.add(hyp);
            this.names = new HashSet<String>();
            names.add(name);
            this.conflicting = false;
            this.restrictionSet = new HashSet<Restriction>();
            this.contradictions= new HashSet<Contradiction>();
            this.id=count;
            count++;
    }
    public void setConflict(boolean x){
    	this.conflicting=x;
    }
    public HashSet<Contradiction> getCont(){
    	return this.contradictions;
    }
    public void addCont(Contradiction c){
    	this.contradictions.add(c);
    }
	public void addContextToProp(Context context) {
		for (Iterator<PropositionNode> iterator = this.hypothesisSet.propositions
				.iterator(); iterator.hasNext();) {
			PropositionNode prop = iterator.next();
			((Proposition) prop.getSemantic()).addContext(context);

		}
	}

	public void addToContext(PropositionNode proposition) {
		if (!this.hypothesisSet.propositions.contains(proposition)) {
			this.hypothesisSet.propositions.add(proposition);
		}

	}

	public boolean canBeAdded(PropositionNode prop) {
		if (!this.hypothesisSet.propositions.contains(prop)) {
			for (Iterator<Restriction> iterator = this.restrictionSet
					.iterator(); iterator.hasNext();) {
				Restriction temp = iterator.next();
				if (!temp.canBeAddedHelper(prop))
					return false;

			}
			return true;
		}
		return false;
	}

	public void removePropFromRestriction(PropositionNode prop) {
		for (Iterator<Restriction> iterator = this.restrictionSet.iterator(); iterator
				.hasNext();) {
			Restriction restriction = iterator.next();
			if (!restriction.removedSet.propositions.contains(prop)
					&& restriction.restrictor.hypothesisSet.propositions
							.contains(prop))
				restriction.addRemoved(prop);
		}
	}

	public void addPropToRestriction(PropositionNode prop) {
		for (Iterator<Restriction> iterator = this.restrictionSet.iterator(); iterator
				.hasNext();) {
			Restriction restriction = iterator.next();
			if (restriction.removedSet.propositions.contains(prop)
					&& restriction.restrictor.hypothesisSet.propositions
							.contains(prop))
				restriction.removeAdded(prop);
		}

	}

	public void addRestriction(Context restrictorCont, PropositionSet removedProp,HashSet<Support>supportSet2) {
		Restriction check = null;
		for (Iterator<Restriction> iterator = this.restrictionSet.iterator(); iterator
				.hasNext();) {
			Restriction temp = iterator.next();
			if (temp.restrictor == restrictorCont) {
				check = temp;
				break;
			}
		}
		if (check == null) {
			HashSet<PropositionSet> restrictions=new HashSet<PropositionSet>();
			for (Iterator<Support> iterator = supportSet2.iterator(); iterator
					.hasNext();) {
				restrictions.add(iterator.next().originSet.hypothesisSet);
			}
			PropositionSet removed = new PropositionSet();
			removed.addPropSet(removedProp);
			Restriction newRestriction = new Restriction(restrictorCont,
					removed,restrictions);
			this.restrictionSet.add(newRestriction);
		} else {
			for (Iterator<PropositionNode> iterator1 = removedProp.propositions.iterator(); iterator1
					.hasNext();){
				PropositionNode prop = iterator1.next();
				check.addRemoved(prop);	
			}
			
		}
	}

	public void removeRestrictor(Context restrictor) {
		for (Iterator<Restriction> iterator = this.restrictionSet.iterator(); iterator
				.hasNext();) {
			Restriction type = iterator.next();
			if (type.restrictor == restrictor) {
				restrictionSet.remove(type);
			}
		}

	}

	public void removeRestrictor(PropositionSet restrictor) {
		for (Iterator<Restriction> iterator = this.restrictionSet.iterator(); iterator
				.hasNext();) {
			Restriction type = iterator.next();
			if (type.restrictor.hypothesisSet == restrictor) {
				restrictionSet.remove(type);
			}
		}

	}

	public boolean findByName(String name) {
		return this.names.contains(name);
	}

	public HashSet<String> getNames() {
		return names;
	}

	public void addName(String name) {
		this.names.add(name);
	}
	public void addNames(HashSet<String> names) {
		this.names.addAll(names);
	}

	public void clearNames() {
		names.clear();
	}
	
    public boolean RemoveName(String name)
    {
            return names.remove(name);
    }
    
    public void unionRestriction (Context context)
    {
            for(Iterator<Restriction> iterator = context.restrictionSet.iterator();iterator.hasNext();)
            {
                    Restriction temp = iterator.next();
                    if(!this.restrictionSet.contains(temp))
                            this.restrictionSet.add(temp);
            }
    }

	public PropositionSet getHypothesisSet() {
		return hypothesisSet;
	}

	public void setHypothesisSet(PropositionSet hypothesisSet) {
		this.hypothesisSet = hypothesisSet;
	}

	public boolean isConflicting() {
		return conflicting;
	}

	public void setConflicting(boolean conflicting) {
		this.conflicting = conflicting;
	}

	public HashSet<Restriction> getRestrictionSet() {
		return restrictionSet;
	}

	public void setRestrictionSet(HashSet<Restriction> restrictionSet) {
		this.restrictionSet = restrictionSet;
	}

	public HashSet<Contradiction> getContradictions() {
		return contradictions;
	}

	public void setContradictions(HashSet<Contradiction> contradictions) {
		this.contradictions = contradictions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Context.count = count;
	}

	public void setNames(HashSet<String> names) {
		this.names = names;
	}

}
