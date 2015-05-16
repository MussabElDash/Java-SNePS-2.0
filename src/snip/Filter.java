package snip;

import sneps.match.Binding;
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

	@Override
	public boolean equals(Object filter) {
		Filter typeCastedObject = (Filter) filter;
		if(typeCastedObject == null)
			return false;
		return this.substitution.isEqual(typeCastedObject.getSubstitution());
	}

	public boolean canPass(Report report) {
		for (int i = 0; i < this.substitution.cardinality(); i++) {
			Binding currentFilterBinding = substitution.getBinding(i);
			Binding currentReportBinding = report.getSubstitutions().getBindingByVariable(currentFilterBinding.getVariable());
			System.out.println("Bindings " + currentFilterBinding + " " + report.getSubstitutions());
			if (currentReportBinding != null && currentFilterBinding.getNode() != currentReportBinding.getNode())
				return false;
		}
		return true;
	}

}
