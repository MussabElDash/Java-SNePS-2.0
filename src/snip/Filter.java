package snip;

public class Filter {
	
	private Substitutions substitution;
	
	public Filter() {
		this.substitution = new Substitutions();
	}
	
	public Filter(Substitutions substitution) {
		this.substitution = substitution;
	}
	
	public Substitutions getSubstitution() {
		return substitution;
	}
	
	public boolean equals(Object filter) {
//		TODO Akram
		return false;
	}
	
	public boolean canPass(Report report) {
		//TODO Akram
		return true;
	}
	
}
