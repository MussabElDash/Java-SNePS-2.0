package snip;


import sneps.match.Substitutions;
import SNeBR.Support;

public class Report {

	private Substitutions substitution;
	private Support support;
	private boolean sign;
	private int contextID;

	public Report(Substitutions substitution, Support support, boolean sign, int contextID) {
		this.substitution = substitution;
		this.support = support;
		this.sign = sign;
		this.contextID = contextID;
	}

	public Substitutions getSubstitutions() {
		return substitution;
	}

	 public Support getSupport() {
		 return support;
	 }

	public int getContextID() {
		return contextID;
	}

	@Override
	public boolean equals(Object report) {
		Report castedReport = (Report) report;
		return this.substitution.equals(castedReport.substitution)
				&& this.sign == castedReport.sign
				&& this.contextID == castedReport.contextID;
	}
	
	public boolean getSign() {
		return sign;
	}
	
	public boolean isPositive() {
		return sign;
	}
	
	public boolean isNegative() {
		return !sign;
	}

}