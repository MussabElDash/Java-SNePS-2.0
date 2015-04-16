package snip;

import java.util.ArrayList;

import sneps.Nodes.Node;
import SNeBR.Context;

public abstract class Channel {
	
	private Filter filter;
	private Switch switch_;
	private Context context;
	private Node destination;
	private boolean valve;
	private Substitutions tar;
	private ArrayList<Report> reportsBuffer;
	
	public Channel(Filter f, Switch s, Context c, Node d, boolean v) {
		this.filter = f;
		this.switch_ = s;
		this.context = c;
		this.destination = d;
		this.valve = v;
		this.tar = new Substitutions();
	}
	
	public Context getContext() {
		return context;
	}
	
	public boolean isValveOpen() {
		return valve;
	}
	
	public void setValve(boolean valve) {
		this.valve = valve;
	}
	
	public void addReport(Report report) {
		reportsBuffer.add(report);
	}
	
	public ArrayList<Report> getReportsBuffer() {
		return reportsBuffer;
	}
}
