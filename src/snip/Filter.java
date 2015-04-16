package snip;

public class Filter {
	
	private Substitutions substitution;
	
	public Filter() {
		
	}
	
	public Filter(Substitutions substitution) {
		this.substitution = substitution;
	}
	
	public Substitutions getSubstitution() {
		return substitution;
	}
	
	public boolean equals(Object filter) {
//		TODO
		return false;
	}
	
	public boolean canPass(Report report) {
		//TODO
		return false;
	}
	
}
