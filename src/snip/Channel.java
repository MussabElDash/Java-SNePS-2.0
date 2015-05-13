package snip;

import java.util.ArrayList;

import sneps.Nodes.Node;
import sneps.match.LinearSubstitutions;
import sneps.match.Substitutions;
import SNeBR.Context;

public abstract class Channel {

	private Filter filter;
	private Switch switch_;
	private Context context;
	private Node destination;
	private boolean valve;
	private Substitutions tar;
	private ArrayList<Report> reportsBuffer;

	public Channel() {
		filter = new Filter();
		reportsBuffer = new ArrayList<Report>();
	}

	public Channel(Filter f, Switch s, Context c, Node d, boolean v) {
		this.filter = f;
		this.switch_ = s;
		this.context = c;
		this.destination = d;
		this.valve = v;
		this.tar = new LinearSubstitutions();
		reportsBuffer = new ArrayList<Report>();
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

	public boolean addReport(Report report) {
		if (filter.canPass(report)) {
			// TODO Akram: substitute using switch
			System.out.println("report " + report.getNode() + " "
					+ report.getSignature());
			reportsBuffer.add(report);
			return true;
		}
		return false;
	}

	public ArrayList<Report> getReportsBuffer() {
		return reportsBuffer;
	}
}
