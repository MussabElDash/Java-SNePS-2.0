package snip;

public class Filter {
	
	private Substitutions substitution;
	
	public Filter() {
		
	}
	
	public Filter(Substitutions substitution) {
		this.substitution = substitution;
	}
	
	public Substitutios getSubstitution() {
		return substitution;
	}
	
	public boolean equals(Object filter) {
//		TODO
	}
	
	public boolean canPass(Report report) {
		//TODO
	}
	
}
