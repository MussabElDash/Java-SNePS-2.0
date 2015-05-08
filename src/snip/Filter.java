package snip;

import sneps.match.LinearSubstitutions;
import sneps.match.Substitutions;

public class Filter {

	private Substitutions substitution;

	public Filter() {
		this.substitution = new LinearSubstitutions();
	}

	public Filter(Substitutions substitution) {
		this.substitution = substitution;
	}

	public Substitutions getSubstitution() {
		return substitution;
	}

	public boolean equals(Object filter) {
		// TODO Akram
		return false;
	}

	public boolean canPass(Report report) {
		// TODO Akram
		return true;
	}

}
