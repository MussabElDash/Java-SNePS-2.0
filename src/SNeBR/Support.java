package SNeBR;

import java.util.HashSet;
import java.util.Iterator;

import sneps.PropositionNode;


public class Support {
	Context originSet;
	
    public Support (PropositionNode proposition)
    {
            this.originSet=new Context(proposition);
    }
    
    public Support()
    {
            this.originSet= new Context();
    }
    
    public Support(Context c){
    	this.originSet=c;
    }
	public boolean assertedInContext(Context context) {
		if (context.hypothesisSet.propositions
				.containsAll(this.originSet.hypothesisSet.propositions))
			return true;
		return false;
	}

	public void addRestriction(Context restrictor, PropositionSet removed,HashSet<Support> supportSet) {
		HashSet<PropositionSet> restrictions=new HashSet<PropositionSet>();
		for (Iterator<Support> iterator = supportSet.iterator(); iterator
				.hasNext();) {
			restrictions.add(iterator.next().originSet.hypothesisSet);
		}
		PropositionSet newRemoved = new PropositionSet(removed.propositions);
		Restriction newRestriction = new Restriction(restrictor, newRemoved,restrictions);
		this.originSet.restrictionSet.add(newRestriction);
	}
	
    public void addToOriginSet(PropositionNode proposition)
    {
            this.originSet.addToContext(proposition);
    }

	public Context getOriginSet() {
		return originSet;
	}

	public void setOriginSet(Context originSet) {
		this.originSet = originSet;
	}
}
