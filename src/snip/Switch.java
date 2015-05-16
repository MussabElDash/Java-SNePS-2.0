package snip;

import sneps.Nodes.VariableNode;
import sneps.match.Binding;
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

	public void switchReport(Report r) {
		for (int i = 0; i < this.substitution.cardinality(); i++) {
			
			Binding b = r.getSubstitutions().getBindingByVariable(
					this.substitution.getBinding(i).getVariable());
			System.out.println(this.substitution.getBinding(i).getVariable());
			System.out.println("i: " + i + " binding: " + b);
			if (b != null) {
				b.setVariable((VariableNode) this.substitution.getBinding(i)
						.getNode());
			}else {
				System.out.println("there u go " + this.substitution.getBinding(i));
				r.getSubstitutions().putIn(this.substitution.getBinding(i));
				System.out.println("size now " + r.getSubstitutions().cardinality());
			}
			
		}
		System.out.println(r.getSubstitutions().isNew());
		System.out.println("Done Switching:\n" + r.getSubstitutions());
		// {a/X, b/Y}, {X/W, Y/Z, K/C} => {a/W, b/Z, K/C}
//		r.getSubstitutions().unionIn(s);
	}
	
	public String toString() {
		return substitution.toString();
	}
}
