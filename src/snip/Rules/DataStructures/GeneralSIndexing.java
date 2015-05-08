package snip.Rules.DataStructures;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Set;

import sneps.Nodes.NodeSet;
import SNeBR.Context;

public class GeneralSIndexing<E extends ContextRUIS> extends ContextRUIS {
	private Hashtable<Integer, E> map;
	private Class<E> clazz;
	private Set<Integer> sharedVars;
	private NodeSet nodesWithVars;

	/**
	 * Create new Empty GeneralSIndexing table and associate it with the Context
	 * context and use the sharedVars as the vars to hash on, clazz a class of
	 * RuleUseInfoSet or PTreeis and used to instantiate either of them. <br>
	 * <br>
	 * nodesWithVars is a NodeSet containing the nodes used building the PTree
	 * and it's not needed in case RuleUseInfoSet is used.
	 * 
	 * @param context
	 *            Context
	 * @param sharedVars
	 *            Set<Integer>
	 * @param nodesWithVars
	 *            NodeSet
	 * @param clazz
	 *            Class<E extends ContextRUIS>
	 */
	public GeneralSIndexing(Context context, Set<Integer> sharedVars,
			NodeSet nodesWithVars, Class<E> clazz) {
		super(context);
		this.clazz = clazz;
		this.sharedVars = sharedVars;
		this.nodesWithVars = nodesWithVars;
		map = new Hashtable<Integer, E>();
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
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private RuleUseInfoSet insertInIndex(int x, RuleUseInfo rui)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		E tempRui = map.get(x);
		if (tempRui == null) {
			Constructor<E> cons = clazz.getConstructor(Context.class);
			tempRui = cons.newInstance(getContext());
			if (tempRui instanceof PTree)
				((PTree) tempRui).buildTree(nodesWithVars);
		}
		RuleUseInfoSet res = tempRui.insertRUI(rui);
		return res;
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
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private RuleUseInfoSet insert(RuleUseInfo rui, int[] ids)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		int index = getIndex(ids);
		return insertInIndex(index, rui);
	}

	@Override
	public RuleUseInfoSet insertRUI(RuleUseInfo rui) {
		try {
			int[] vars = new int[sharedVars.size()];
			int index = 0;
			for (int varId : sharedVars) {
				vars[index++] = rui.getSub().termID(varId);
			}
			return insert(rui, vars);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			return new RuleUseInfoSet();
		}
	}

}
