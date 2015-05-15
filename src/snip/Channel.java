package snip;

import java.util.ArrayList;

import sneps.Nodes.Node;
import sneps.match.Substitutions;

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
			System.out.println("\n\nThe Switch data:\n" + switch_);
			switch_.switchReport(report);
			reportsBuffer.add(report);
			Runner.addToHighQueue(destination);
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
