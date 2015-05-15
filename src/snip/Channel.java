package snip;

import java.util.ArrayList;

import sneps.Network;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.match.LinearSubstitutions;
import sneps.match.Substitutions;
import SNeBR.Context;

public abstract class Channel {
	
	private Filter filter;
	private Switch switch_;
	private int contextID;
	private Node destination;
	private boolean valve;
	private ArrayList<Report> reportsBuffer;
	
	public Channel() {
		filter = new Filter();
		switch_ = new Switch();
		reportsBuffer = new ArrayList<Report>();
	}

	public Channel(Substitutions switchSubstitution, Substitutions filterSubstitutions, int contextID, Node d, boolean v) {
		this.filter = new Filter(filterSubstitutions);
		this.switch_ = new Switch(switchSubstitution);
		this.contextID = contextID;
		this.destination = d;
		this.valve = v;
		reportsBuffer = new ArrayList<Report>();
	}
	
	public int getContextID() {
		return contextID;
	}
	
	public boolean isValveOpen() {
		return valve;
	}
	
	public void setValve(boolean valve) {
		this.valve = valve;
	}
	
	public boolean addReport(Report report) {
		System.out.println("Can pass " + filter.canPass(report));
		if(filter.canPass(report) && contextID == report.getContextID()) {
			//TODO Akram: substitute using switch
			System.out.println("switch " + switch_);
			switch_.switchReport(report);
//			Substitutions s = new Substitutions();
//			s.unionIn(r.getSubstitutions());
			
//			//TODO Ahmed Akram: what is all this for ?
//			if (s.cardinality() != 0) {
//				Network n = Network.getInstance();
//				Object[] res = n.applyRestrictions(
//						(MolecularNode) des.getNode(), s);
//				MolecularNode rest = (MolecularNode) res[0];
//				boolean wasAB = (Boolean.valueOf(res[1].toString()));
//				if (!wasAB) {
//					rest.setTemp(false);
//				}
//				r.setNode(rest);
//			}
//			des.getNode().getEntity().getProcess().receiveReport(r);
//			System.out.println("Report sent to "
//					+ des.getNode().getIdentifier()
//					+ " with the substitution: \n"
//					+ r.getSubstitutions().toString());
//			System.out.println("report " + report.getNode() + " " + report.getSignature());
			// TODO Akram: add the node to hq
			reportsBuffer.add(report);
			return true;
		}
		return false;
	}
	
	public Filter getFilter() {
		return filter;
	}
	
	public Switch getSwitch() {
		return switch_;
	}
	
	public Node getDestination() {
		return destination;
	}

	public ArrayList<Report> getReportsBuffer() {
		return reportsBuffer;
	}
}
