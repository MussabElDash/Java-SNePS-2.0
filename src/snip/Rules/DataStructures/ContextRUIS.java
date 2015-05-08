/**
 * @(#)ContextRUIS.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/5/4
 */

package snip.Rules.DataStructures;

import SNeBR.Context;

public abstract class ContextRUIS {
	private Context context;

	// private ChannelsSet cqChannels;
	// private Sindexing s;
	// private Ptree p;
	// private RuleUseInfoSet r;

	// /**
	// * Create new ContextRUIS
	// *
	// * @param c
	// * Context
	// * @param cq
	// * ChannelsSet
	// * @param t
	// * 's' for Sindexing, 'p' for Ptree and 'r' for RuleUseInfoSet
	// */
	// public ContextRUIS(Context c,/* ChannelsSet cq, */char t) {
	// context = c;
	// // cqChannels=cq;
	// if (t == 's') {
	// s = new Sindexing();
	//
	// } else if (t == 'p') {
	// p = new Ptree();
	// } else {
	// r = new RuleUseInfoSet();
	// }
	// }

	/**
	 * Create new ContextRUIS
	 * 
	 * @param context
	 *            Context
	 */
	public ContextRUIS(Context context) {
		this.context = context;
	}

	//
	// /**
	// * Return the Sindexing
	// *
	// * @return Sindexing
	// */
	// public Sindexing getSindexing() {
	// return s;
	// }

	// /**
	// * Return the Ptree
	// *
	// * @return Ptree
	// */
	// public Ptree getPtree() {
	// return p;
	// }
	//
	// /**
	// * Return the rule use info set
	// *
	// * @return RuleUseInfoSet
	// */
	// public RuleUseInfoSet getRUIS() {
	// return r;
	// }

	/**
	 * Add a channel to the consequent channels set
	 * 
	 * @param c
	 */
	// public void addChannel(Channel c) {
	// cqChannels.putIn(c);
	// }

	/**
	 * Return the context
	 * 
	 * @return Context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Return the channels set of the ContextRUIS
	 * 
	 * @return ChannelsSet
	 */
	// public ChannelsSet getChannels() {
	// return cqChannels;
	// }

	/**
	 * Insert a new rule use info in the this ContextRUI and return the rule use
	 * info set result from combining it with rule use infos in the way
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @param pattern
	 *            the pattern this rule use info is about
	 * @return RuleUseInfoSet
	 */
	abstract public RuleUseInfoSet insertRUI(RuleUseInfo rui);
}