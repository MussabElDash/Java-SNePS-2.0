package snip;

import sneps.match.LinearSubstitutions;
import sneps.match.Substitutions;

public class Switch {

	private Substitutions substitution;

	public Switch() {
		this.substitution = new LinearSubstitutions();
	}

	public Switch(Substitutions substitution) {
		this.substitution = substitution;
	}

}
