/**
 * @(#)ContextRUIS.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/5/4
 */

package snip.Rules.DataStructures;

public abstract class RuisHandler {
	private int context;

	/**
	 * Create new ContextRUIS
	 * 
	 * @param contextID
	 *            Context
	 */
	public RuisHandler(int contextID) {
		this.context = contextID;
	}

	/**
	 * Return the context
	 * 
	 * @return Context
	 */
	public int getContext() {
		return context;
	}

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