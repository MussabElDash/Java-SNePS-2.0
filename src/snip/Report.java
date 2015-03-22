package snip;

import sneps.Node;
import SNeBR.Context;
import SNeBR.Support;

public class Report {

	private Substitutions substitution;
	private Support support;
	private boolean sign;
	private Node signature;
	private Node node;
	private Context context;

	public Report(Substitutions substitution, Support support, boolean sign,
			Node signature, Node node, Context context) {
		// TODO Auto-generated constructor stub
		this.substitution = substitution;
		this.support = support;
		this.node = node;
		this.sign = sign;
		this.signature = signature;
		this.context = context;
	}

	public Substitutions getSubstituions() {
		// TODO
		return null;
	}

	 public Support getSupport() {
		 // TODO
		 return null;
	 }

	public boolean getSign() {
		return sign;
	}

	public Node getSignature() {
		return signature;
	}

	public Node getNode() {
		return node;
	}

	public Context getContext() {
		return context;
	}

	public void addBinding(Binding m) {
		// TODO
		// this.substitution.putIn(m);
	}

	@Override
	public boolean equals(Object report) {
		Report castedReport = (Report) report;
		return this.substitution.equals(castedReport.substitution)
				&& this.sign == castedReport.sign
				&& this.node.eqauls(castedReport.node)
				&& this.context.equals(castedReport.context));
	}
	
	public boolean isPositive() {
		return sign;
	}
	
	public boolean isNegative() {
		return !sign;
	}
	
	public boolean hasContext() {
		return !(context == null);
	}
	
	public void setNode(Node node) {
		this.node = node;
	}

}
