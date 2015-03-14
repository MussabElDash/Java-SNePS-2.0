package SNeBR;
import sneps.SemanticClasses.*;
public class Contradiction {
	Proposition newContradictingProp;
	PropositionSet oldContradictingPropSet;

	public Contradiction(Proposition prop, PropositionSet propSet) {
		newContradictingProp = prop;
		oldContradictingPropSet = propSet;
	}

	public Proposition getNewContradictingProp() {
		return newContradictingProp;
	}

	public void setNewContradictingProp(Proposition newContradictingProp) {
		this.newContradictingProp = newContradictingProp;
	}

	public PropositionSet getOldContradictingPropSet() {
		return oldContradictingPropSet;
	}

	public void setOldContradictingPropSet(PropositionSet oldContradictingPropSet) {
		this.oldContradictingPropSet = oldContradictingPropSet;
	}
}
