package snip.Rules.DataStructures;

import java.util.Hashtable;
import java.util.Set;

import SNeBR.Context;

public class SIndexing extends ContextRUIS{
	private Hashtable<Integer, RuleUseInfo> map;
	private Set<Integer> sharedVars;

	/**
	 * Create new Empty Sindexing table and associate it with the Context c
	 * @param c Context
	 */
	public SIndexing(Context c) {
		super(c);
		map = new Hashtable<Integer, RuleUseInfo>();
	}

	/**
	 * Insert the rule use info rui in this table in the index x, if there was a
	 * rule use info in the index x then it should be replaced by the merge of
	 * rui and the existed one
	 * 
	 * @param x
	 *            int
	 * @param rui
	 *            RuleUseInfo
	 * @return the final RuleUseInfo
	 */
	private RuleUseInfo insertInIndex(int x, RuleUseInfo rui) {
		RuleUseInfo tempRui = map.get(x);
		if (tempRui == null) {
			tempRui = rui;
		}else{
			tempRui = tempRui.combine(rui);
		}
		map.put(x, tempRui);
		return tempRui;
	}

	/**
	 * Get the index of a RuleUseInfo in the table by the id's of its
	 * substitutions
	 * 
	 * @param x
	 *            int[]
	 * @return int
	 */
	private int getIndex(int[] x) {
		int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < x.length; ++i) {
			hash += (hash ^ x[i]) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		return hash;
	}

	/**
	 * Insert rui in the table
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @param ids
	 *            int []
	 * @return the final RuleUseInfo
	 */
	private RuleUseInfo insert(RuleUseInfo rui, int[] ids) {
		int index = getIndex(ids);
		return insertInIndex(index, rui);
	}

	@Override
	public RuleUseInfoSet insertRUI(RuleUseInfo rui) {
		RuleUseInfoSet res = new RuleUseInfoSet();
		rui = insert(rui, null);
		res.add(rui);
		return res;
	}
	
	public void setSharedVars(Set<Integer> sharedVars){
		this.sharedVars = sharedVars;
	}
}
