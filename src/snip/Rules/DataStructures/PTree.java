/**
 * @(#)Ptree.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/25
 */

package snip.Rules.DataStructures;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.VariableNode;
import snip.Rules.Interfaces.NodeWithVar;

import com.google.common.collect.Sets;

public class PTree extends RuisHandler {
	private Hashtable<Integer, Set<Integer>> patternVariables,
			variablePatterns;
	private Set<Integer> varNotProccessed, patterns;
	private Hashtable<Integer, PSubTree> subTreesMap;
	private Set<PSubTree> subTrees;

	/**
	 * Create a new Ptree and associate it with the Context c
	 * 
	 * @param c
	 *            Context
	 */
	public PTree(int c) {
		super(c);
		patternVariables = new Hashtable<Integer, Set<Integer>>();
		variablePatterns = new Hashtable<Integer, Set<Integer>>();
		varNotProccessed = new HashSet<Integer>();
		patterns = new HashSet<Integer>();
		subTreesMap = new Hashtable<Integer, PSubTree>();
		subTrees = new HashSet<PSubTree>();
	}

	/**
	 * Insert a new rule use info in the tree and return the rule use info set
	 * result from combining it with rule use infos in the way up
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @param pattern
	 *            the pattern this rule use info is about
	 * @return RuleUseInfoSet
	 */
	@Override
	public RuleUseInfoSet insertRUI(RuleUseInfo rui) {
		int pattern = rui.getFlagNodeSet().iterator().next().getNode().getId();
		PSubTree subTree = subTreesMap.get(pattern);
		RuleUseInfoSet returned = subTree.insert(rui);
		Stack<RuleUseInfoSet> stack = new Stack<RuleUseInfoSet>();
		for (PSubTree sub : subTrees) {
			if (sub == subTree)
				continue;
			RuleUseInfoSet tSet = sub.getRootRUIS();
			if (tSet == null)
				continue;
			stack.push(tSet);
		}
		stack.push(returned);
		return multiply(stack);
	}

	private RuleUseInfoSet multiply(Stack<RuleUseInfoSet> infoSets) {
		RuleUseInfoSet first = infoSets.pop();
		while (!infoSets.isEmpty()) {
			RuleUseInfoSet second = infoSets.pop();
			first = first.combine(second);
		}
		return first;
	}

	/**
	 * Build the ptree from nodes ids
	 * 
	 * @param nodes
	 */
	public void buildTree(NodeSet pns) {
		fillPVandVP(pns);
		// System.out.println(variablePatterns);
		// System.out.println(patternVariables);
		LinkedHashSet<Integer> patSeq = getPatternSequence();
		// System.out.println(patSeq);
		varNotProccessed = null;
		Queue<PTreeNode> tnv = new LinkedList<PTreeNode>();
		for (int pat : patSeq) {
			Set<Integer> patterns = new HashSet<Integer>();
			patterns.add(pat);
			tnv.add(new PTreeNode(patterns, patternVariables.get(pat)));
		}
		constructTree(tnv);
		patternVariables = null;
		variablePatterns = null;
	}

	private void fillPVandVP(NodeSet pns) {
		for (int i = 0; i < pns.size(); i++) {
			Node pat = pns.getNode(i);
			int patId = pat.getId();
			patterns.add(patId);
			Set<Integer> vars = patternVariables.get(patId);
			if (vars == null) {
				vars = new HashSet<Integer>();
				patternVariables.put(patId, vars);
			}
			Iterator<VariableNode> nodeIter = ((NodeWithVar) pat)
					.getFreeVariables().iterator();
			while (nodeIter.hasNext()) {
				Node var = nodeIter.next();
				int varId = var.getId();
				vars.add(varId);
				varNotProccessed.add(varId);
				Set<Integer> pats = variablePatterns.get(varId);
				if (pats == null) {
					pats = new HashSet<Integer>();
					variablePatterns.put(varId, pats);
				}
				pats.add(patId);
			}
		}
	}

	private LinkedHashSet<Integer> getPatternSequence() {
		LinkedHashSet<Integer> res = new LinkedHashSet<Integer>();
		Set<Integer> toBeProccessed = new HashSet<Integer>();
		while (!varNotProccessed.isEmpty()) {
			toBeProccessed.add(peek(varNotProccessed));
			while (!toBeProccessed.isEmpty()) {
				int var = peek(toBeProccessed);
				Iterator<Integer> varPatsIter = variablePatterns.get(var)
						.iterator();
				while (varPatsIter.hasNext()) {
					int pat = varPatsIter.next();
					if (res.contains(pat))
						continue;
					res.add(pat);
					Set<Integer> patVarSet = patternVariables.get(pat);
					toBeProccessed.addAll(patVarSet);
				}
				toBeProccessed.remove(var);
				varNotProccessed.remove(var);
			}
		}
		res.addAll(patterns);
		return res;
	}

	private int peek(Set<Integer> set) {
		return set.iterator().next();
	}

	private void constructTree(Queue<PTreeNode> tnv) {
		PTreeNode head = tnv.poll();
		if (tnv.isEmpty()) {
			processSubTree(head);
			return;
		}
		Queue<PTreeNode> newTnv = new LinkedList<PTreeNode>();
		boolean sharing = false;
		while (!tnv.isEmpty()) {
			PTreeNode second = tnv.poll();
			if (sharingVars(head, second)) {
				sharing = true;
				Set<Integer> pats = Sets
						.union(head.getPats(), second.getPats());
				Set<Integer> vars = Sets
						.union(head.getVars(), second.getVars());
				PTreeNode tn = new PTreeNode(pats, vars);
				Set<Integer> intersection = getSharedVars(head, second);
				tn.insertLeftAndRight(head, second, intersection);
				newTnv.add(tn);
				head = tnv.poll();
			} else {
				newTnv.add(head);
				head = second;
			}
		}
		if (head != null)
			newTnv.add(head);
		if (sharing)
			constructTree(newTnv);
		else
			constructSubTree(newTnv);
	}

	private void constructSubTree(Queue<PTreeNode> newTnv) {
		for (PTreeNode head : newTnv) {
			processSubTree(head);
		}
	}

	private void processSubTree(PTreeNode head) {
		PSubTree subTree = new PSubTree(head);
		subTrees.add(subTree);
		for (int id : head.getPats()) {
			subTreesMap.put(id, subTree);
		}
	}

	private boolean sharingVars(PTreeNode head, PTreeNode second) {
		Set<Integer> smaller = null, bigger = null;
		if (head.getVars().size() > second.getVars().size()) {
			smaller = second.getVars();
			bigger = head.getVars();
		} else {
			bigger = second.getVars();
			smaller = head.getVars();
		}
		for (int i : smaller)
			if (bigger.contains(i))
				return true;
		return false;
	}

	private Set<Integer> getSharedVars(PTreeNode first, PTreeNode second) {
		Set<Integer> smaller = null, bigger = null, intersection = new HashSet<Integer>();
		if (first.getVars().size() > second.getVars().size()) {
			smaller = second.getVars();
			bigger = first.getVars();
		} else {
			bigger = second.getVars();
			smaller = first.getVars();
		}
		for (int i : smaller)
			if (bigger.contains(i))
				intersection.add(i);
		return intersection;
	}

	/**
	 * Used in Testing. <br>
	 * prints the list of propositions in each PSubTree of this PTree
	 */
	public void printSubTrees() {
		for (PSubTree subTree : subTrees) {
			System.out.println("====================================");
			System.out.println("A PSubTree:");
			subTree.printTreePatterns();
		}
	}

	private class PSubTree {
		private PTreeNode root;

		public PSubTree(PTreeNode root) {
			this.root = root;
		}

		public void printTreePatterns() {
			printTreePatterns(root, "");
		}

		private void printTreePatterns(PTreeNode root, String s) {
			if (root == null)
				return;
			System.out.println(s + root.getPats());
			printTreePatterns(root.getLeft(), "Left: ");
			printTreePatterns(root.getRight(), "Right: ");
		}

		public RuleUseInfoSet insert(RuleUseInfo rui) {
			int pattern = rui.getFlagNodeSet().iterator().next().getNode()
					.getId();
			PTreeNode leaf = getLeafPattern(pattern, root);
			RuleUseInfoSet res = new RuleUseInfoSet();
			leaf.insertIntoTree(rui, res);
			return res;
		}

		private PTreeNode getLeafPattern(int pattern, PTreeNode pNode) {
			if (pNode.getLeft() == null)
				return pNode;
			PTreeNode left = pNode.getLeft(), right = pNode.getRight();
			if (left.getPats().contains(pattern))
				return getLeafPattern(pattern, left);
			else
				return getLeafPattern(pattern, right);
		}

		public RuleUseInfoSet getRootRUIS() {
			return root.getRUIS(0);
		}
	}

	private class PTreeNode {
		private Hashtable<Integer, RuleUseInfoSet> ruisMap;
		private Set<Integer> pats;
		private Set<Integer> vars;
		private Set<Integer> intersectionWithSibling;
		private PTreeNode parent;
		private PTreeNode right;
		private PTreeNode left;
		private PTreeNode sibling;

		/**
		 * Create a new subtree with the parent up (if up is null then this node
		 * is the root)
		 * 
		 * @param p
		 *            list of patterns ids
		 * @param d
		 *            list of directions
		 * @param parent
		 *            TreeNode
		 */
		public PTreeNode(Set<Integer> p, Set<Integer> v) {
			ruisMap = new Hashtable<Integer, RuleUseInfoSet>();
			pats = p;
			vars = v;
			parent = null;
			right = null;
			left = null;
		}

		public void insertIntoTree(RuleUseInfo rui, RuleUseInfoSet set) {
			int key = insertRUI(rui);
			// Since it has no sibling, it has no parent, and it is the root
			if (sibling == null) {
				set.add(rui);
				return;
			}
			RuleUseInfoSet siblingSet = sibling.getRUIS(key);
			if (siblingSet == null)
				return;
			for (RuleUseInfo tRui : siblingSet) {
				RuleUseInfo combinedRui = rui.combine(tRui);
				if (combinedRui == null)
					continue;
				parent.insertIntoTree(combinedRui, set);
			}
		}

		/**
		 * Add rui to the rule use info set of this node and returns the key
		 * that was mapped to the rui
		 * 
		 * @param rui
		 *            RuleUseInfo
		 *
		 * @return int
		 *
		 */
		public int insertRUI(RuleUseInfo rui) {
			// Since it has no sibling, it has no parent, and it is the root
			if (sibling == null) {
				RuleUseInfoSet ruis = ruisMap.get(0);
				if (ruis == null) {
					ruis = new RuleUseInfoSet();
					ruisMap.put(0, ruis);
				}
				ruis.add(rui);
				return 0;
			}
			// The upper part of this method is for incase this node is aroot
			// node
			// and the lower part for other nodes in the tree
			int[] ids = new int[intersectionWithSibling.size()];
			int index = 0;
			for (int id : intersectionWithSibling) {
				ids[index++] = rui.getSub().termID(id);
			}
			int key = getKey(ids);
			RuleUseInfoSet ruis = ruisMap.get(key);
			if (ruis == null) {
				ruis = new RuleUseInfoSet();
				ruisMap.put(key, ruis);
			}
			ruis.add(rui);
			return key;
		}

		/**
		 * Return the rule use info set that maps to set of variables'
		 * substitutions' key
		 * 
		 * @param index
		 *            int
		 * 
		 * @return RuleUseInfoSet
		 */
		public RuleUseInfoSet getRUIS(int index) {
			return ruisMap.get(index);
		}

		/**
		 * Return the right child of this node
		 * 
		 * @return
		 */
		public PTreeNode getRight() {
			return right;
		}

		/**
		 * Return the left child of this node
		 * 
		 * @return
		 */
		public PTreeNode getLeft() {
			return left;
		}

		/**
		 * insert a left and right children
		 * 
		 * @param leftNode
		 *            PTreeNode that to be inserted as a left child
		 * @param rightNode
		 *            PTreeNode that to be inserted as a right child
		 * @param intersection
		 */
		public void insertLeftAndRight(PTreeNode leftNode, PTreeNode rightNode,
				Set<Integer> intersection) {
			leftNode.parent = this;
			rightNode.parent = this;
			leftNode.sibling = rightNode;
			rightNode.sibling = leftNode;
			leftNode.intersectionWithSibling = intersection;
			rightNode.intersectionWithSibling = intersection;
			left = leftNode;
			right = rightNode;
		}

		/**
		 * Return the list of patterns
		 * 
		 * @return int[]
		 */
		public Set<Integer> getPats() {
			return pats;
		}

		/**
		 * Return the list of variables
		 * 
		 * @return
		 */
		public Set<Integer> getVars() {
			return vars;
		}

		/**
		 * Get the index of a RuleUseInfo in the table by the id's of its
		 * substitutions
		 * 
		 * @param x
		 *            int[]
		 * @return int
		 */
		private int getKey(int[] x) {
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

		@Override
		public String toString() {
			return pats.toString();
		}

	}
}
