package snip.Rules.DataStructures;

import java.util.Hashtable;
import java.util.Set;

import sneps.Nodes.NodeSet;

public class SIndex extends RuisHandler {
	private Hashtable<Integer, RuisHandler> map;
	private Set<Integer> sharedVars;
	private NodeSet nodesWithVars;
	private byte ruiHandlerType;
	public static final byte SINGLETONRUIS = 0, RUIS = 1, PTREE = 2;

	/**
	 * Create new Empty SIndex table and associate it with the Context context
	 * and use the sharedVars as the vars to hash on.<br>
	 * nodesWithVars is a NodeSet containing the nodes used building the PTree
	 * and it's only needed in case AndEntailment with all its patterns share
	 * the same set of variables. <br>
	 * contextType = <br>
	 * &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;
	 * <b>SINGLETONRUIS</b> in case all the patterns share the same set of
	 * variables. <br>
	 * &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;
	 * <b>PTREE</b> in case of using AndEntailment and at the same time all the
	 * patterns does not <br>
	 * &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;
	 * &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp; share
	 * the same set of variables. <br>
	 * &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;
	 * <b>RUIS</b> in any other case
	 * 
	 * @param context
	 *            Context
	 * @param sharedVars
	 *            Set<Integer>
	 * @param nodesWithVars
	 *            NodeSet
	 */
	public SIndex(int context, Set<Integer> sharedVars, byte contextType,
			NodeSet nodesWithVars) {
		super(context);
		this.sharedVars = sharedVars;
		this.nodesWithVars = nodesWithVars;
		this.ruiHandlerType = contextType;
		map = new Hashtable<Integer, RuisHandler>();
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
	private RuleUseInfoSet insertInIndex(int x, RuleUseInfo rui) {
		RuisHandler tempRui = map.get(x);
		if (tempRui == null) {
			tempRui = getNewContextRUIS();
			map.put(x, tempRui);
		}
		RuleUseInfoSet res = tempRui.insertRUI(rui);
		return res;
	}

	private RuisHandler getNewContextRUIS() {
		RuisHandler tempRui = null;
		switch (ruiHandlerType) {
		case PTREE:
			tempRui = new PTree(getContext());
			((PTree) tempRui).buildTree(nodesWithVars);
			break;
		case SINGLETONRUIS:
			tempRui = new RuleUseInfoSet(getContext(), true);
			break;
		case RUIS:
			tempRui = new RuleUseInfoSet(getContext(), false);
		default:
			break;
		}
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
	private RuleUseInfoSet insert(RuleUseInfo rui, int[] ids) {
		int index = getIndex(ids);
		return insertInIndex(index, rui);
	}

	@Override
	public RuleUseInfoSet insertRUI(RuleUseInfo rui) {
		int[] vars = new int[sharedVars.size()];
		int index = 0;
		for (int varId : sharedVars) {
			vars[index++] = rui.getSub().termID(varId);
		}
		return insert(rui, vars);
	}

	/**
	 * Used in Testing
	 * 
	 * @return int the number of different substitutions of the shared variables
	 */
	public int getSize() {
		return map.size();
	}

}
