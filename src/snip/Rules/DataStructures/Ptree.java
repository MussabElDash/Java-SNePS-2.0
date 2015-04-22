/**
 * @(#)Ptree.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/25
 */

package snip.Rules.DataStructures;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import sneps.Nodes.NodeSet;
import sneps.Nodes.VariableNode;
import snip.Rules.Interfaces.NodeWithVar;
import SNeBR.Context;

public class Ptree extends ContextRUIS {
	private TreeNode root;

	/**
	 * Create a new Ptree and associate it with the Context c
	 * 
	 * @param c
	 *            Context
	 */
	public Ptree(Context c) {
		super(c);
		root = null;
	}

	/**
	 * Build the ptree from nodes ids
	 * 
	 * @param nodes
	 */
	public void buildTree(NodeSet pns) {
		NodeWithVar[] patns = new NodeWithVar[pns.size()];
		for (int i = 0; i < patns.length; i++) {
			patns[i] = (NodeWithVar) pns.getNode(i);
		}
		Vector<Vector<Integer>> patvar = getPatVar(patns);
		Vector<Vector<Integer>> varpat = getVarPat(patvar);
		int[] patseq = getPatSeq(patvar, varpat);
		Vector<TreeNode> tn = new Vector<TreeNode>();
		int[][] vars = new int[patseq.length][1];
		for (int i = 0; i < patseq.length; i++) {
			for (int j = 0; j < patvar.size(); j++) {
				Vector<Integer> pvtemp = patvar.get(j);
				if (patseq[i] == pvtemp.get(0)) {
					Vector<Integer> varstemp = new Vector<Integer>();
					for (int k = 1; k < pvtemp.size(); k++) {
						varstemp.add(pvtemp.get(k));
					}
					vars[i] = new int[varstemp.size()];
					for (int k = 0; k < vars[i].length; k++) {
						vars[i][k] = varstemp.get(k);
					}
				}
			}
		}
		for (int i = 0; i < patns.length; i++) {
			int[] x = new int[1];
			x[0] = patns[i].getId();
			tn.add(new TreeNode(x, vars[i], null));
		}
		buildTree(tn, true);
	}

	/**
	 * Check if the node lists share variables or not
	 * 
	 * @param nodes1
	 *            nodes ids
	 * @param nodes2
	 *            nodes ids
	 * @return true or false
	 */
	private boolean shareVars(int[] nodes1, int[] nodes2) {
		for (int i = 0; i < nodes1.length; i++) {
			for (int j = 0; j < nodes2.length; j++) {
				if (nodes1[i] == nodes2[j])
					return true;
			}
		}
		return false;
	}

	/**
	 * Union x1 and x2 and return the result
	 * 
	 * @param x1
	 *            int[]
	 * @param x2
	 *            int[]
	 * @return int[]
	 */
	private int[] union(int[] x1, int[] x2) {
		Vector<Integer> r = new Vector<Integer>();
		for (int i = 0; i < x1.length; i++) {
			r.add(x1[i]);
		}
		for (int i = 0; i < x2.length; i++) {
			r.add(x2[i]);
		}
		for (int i = 0; i < r.size(); i++) {
			for (int j = i + 1; j < r.size(); j++) {
				if (r.get(i) == r.get(j)) {
					r.remove(j);
					j--;
				}
			}
		}
		int[] res = new int[r.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = r.get(i);
		}
		return res;
	}

	/**
	 * Create the direction array
	 * 
	 * @param size
	 *            total number of nodes
	 * @param t
	 *            number of nodes in the left side
	 * @return boolean[]
	 */
	private boolean[] makeDir(int size, int t) {
		boolean[] res = new boolean[size];
		for (int i = 0; i < t; i++) {
			res[i] = true;
		}
		return res;
	}

	/**
	 * Build the tree with the TreeNode set tn, sharing is true when the
	 * patterns may share variables, and when it is found that patterns don't
	 * share variables sharing will be false every time
	 * 
	 * @param tn
	 *            TreeNode set
	 * @param sharing
	 *            true or false
	 */
	private void buildTree(Vector<TreeNode> tn, boolean sharing) {
		if (tn.size() == 2) {
			int[] p1 = tn.get(0).getPats();
			int[] p2 = tn.get(1).getPats();
			int[] p = union(p1, p2);
			boolean[] d = makeDir(p.length, p1.length);
			int[] v1 = tn.get(0).getVars();
			int[] v2 = tn.get(1).getVars();
			int[] v = union(v1, v2);
			root = new TreeNode(p, v, d);
			root.insertLeft(tn.get(0));
			root.insertRight(tn.get(1));
			return;
		}
		Vector<TreeNode> tntemp = new Vector<TreeNode>();
		int combined = 0;
		boolean s = true;
		if (sharing) {
			for (int i = 0; i < tn.size(); i += 2) {
				if (i != tn.size() - 1) {
					if (shareVars(tn.get(i).getVars(), tn.get(i + 1).getVars())) {
						int[] p1 = tn.get(i).getPats();
						int[] p2 = tn.get(i + 1).getPats();
						int[] p = union(p1, p2);
						boolean[] d = makeDir(p.length, p1.length);
						int[] v1 = tn.get(0).getVars();
						int[] v2 = tn.get(1).getVars();
						int[] v = union(v1, v2);
						TreeNode tmp = new TreeNode(p, v, d);
						tmp.insertLeft(tn.get(i));
						tmp.insertRight(tn.get(i + 1));
						tntemp.add(tmp);
						combined++;
					} else {
						tntemp.add(tn.get(i));
						i--;
					}
				} else {
					tntemp.add(tn.get(i));
					i--;
				}
			}
		}
		if (combined == 0) {
			s = false;
			tntemp = new Vector<TreeNode>();
			for (int i = 0; i < tn.size(); i += 2) {
				if (i != tn.size() - 1) {
					int[] p1 = tn.get(i).getPats();
					int[] p2 = tn.get(i + 1).getPats();
					int[] p = union(p1, p2);
					boolean[] d = makeDir(p.length, p1.length);
					int[] v1 = tn.get(0).getVars();
					int[] v2 = tn.get(1).getVars();
					int[] v = union(v1, v2);
					TreeNode tmp = new TreeNode(p, v, d);
					tmp.insertLeft(tn.get(i));
					tmp.insertRight(tn.get(i + 1));
					tntemp.add(tmp);
				} else {
					tntemp.add(tn.get(i));
				}
			}
		}
		if (tntemp.size() == 2) {
			int[] p1 = tntemp.get(0).getPats();
			int[] p2 = tntemp.get(1).getPats();
			int[] p = union(p1, p2);
			boolean[] d = makeDir(p.length, p1.length);
			int[] v1 = tn.get(0).getVars();
			int[] v2 = tn.get(1).getVars();
			int[] v = union(v1, v2);
			root = new TreeNode(p, v, d);
			root.insertLeft(tntemp.get(0));
			root.insertRight(tntemp.get(1));
		} else {
			buildTree(tntemp, s);
		}
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
	public RuleUseInfoSet insert(RuleUseInfo rui) {
		return insertInTree(rui, rui.getFlagNodeSet().getFlagNode(0).getNode()
				.getId(), root);
	}

	/**
	 * Insert rui of the pattern pat in the TreeNode tn or in it's sub tree, and
	 * return the rule use info set result from combining it with rule use infos
	 * in the way up
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @param pat
	 *            int
	 * @param tn
	 *            TreeNode
	 * @return RuleUseInfoSet
	 */
	private RuleUseInfoSet insertInTree(RuleUseInfo rui, int pat, TreeNode tn) {
		int[] pats = tn.getPats();
		if (pats.length == 1) {
			tn.insertRUI(rui);
			return combine(rui, tn.getParent(), pat);
		} else {
			boolean d = tn.getPatDir(pat);
			if (d)
				return insertInTree(rui, pat, tn.getLeft());
			return insertInTree(rui, pat, tn.getRight());
		}
	}

	/**
	 * Combine rui with the rule use info set in tn. Pattern is the pattern this
	 * rui is from.
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @param tn
	 *            TreeNode
	 * @param pattern
	 *            int
	 * @return RuleUseInfoSet
	 */
	private RuleUseInfoSet combine(RuleUseInfo rui, TreeNode tn, int pattern) {
		RuleUseInfoSet s = new RuleUseInfoSet();
		s.putIn(rui);
		return combine(s, tn, pattern);
	}

	/**
	 * Combine ruis with the rule use info set in tn. Pattern is the pattern
	 * this ruis is from.
	 * 
	 * @param ruis
	 *            RuleUseInfoSet
	 * @param tn
	 *            TreeNode
	 * @param pattern
	 *            int
	 * @return RuleUseInfoSet
	 */
	private RuleUseInfoSet combine(RuleUseInfoSet ruis, TreeNode tn, int pattern) {
		if (tn == null) {
			return ruis;
		} else {
			boolean d = tn.getPatDir(pattern);
			RuleUseInfoSet temp;
			if (d) {
				temp = ruis.combine(tn.getRight().getRUIS());
			} else {
				temp = ruis.combine(tn.getLeft().getRUIS());
			}
			if (temp.isNew())
				return null;
			tn.insertRUIS(temp);
			return combine(temp, tn.getParent(), pattern);
		}
	}

	/**
	 * Return the patvar given the pattern nodes set. For example, for
	 * antecedents P(x,y), Q(y,z), and R(x,z),the input is (P Q R), and the
	 * output is ((P x y) (Q y z) (R x z)).
	 * 
	 * @param pns
	 *            PatternNode []
	 * @return Vector<Vector<Integer>>
	 */
	private Vector<Vector<Integer>> getPatVar(NodeWithVar[] pns) {
		Vector<Vector<Integer>> res = new Vector<Vector<Integer>>();
		for (int i = 0; i < pns.length; i++) {
			Vector<Integer> temp = new Vector<Integer>();
			temp.add(pns[i].getId());
			if (pns[i] instanceof VariableNode) {
				continue;
			}
			LinkedList<VariableNode> vars = pns[i].getFreeVariables();
			for (int j = 0; j < vars.size(); j++) {
				temp.add(vars.get(j).getId());
			}
			res.add(temp);
		}
		return res;
	}

	/**
	 * Return the varpat given the patvar. For example, ((P x y) (Q y z) (R x
	 * z)) is converted to ((x P R) (y P Q) (z Q R)).
	 * 
	 * @param patvar
	 *            Vector<Vector<Integer>>
	 * @return Vector<Vector<Integer>>
	 */
	private Vector<Vector<Integer>> getVarPat(Vector<Vector<Integer>> patvar) {
		Vector<Vector<Integer>> res = new Vector<Vector<Integer>>();
		Vector<Integer> pats = new Vector<Integer>();
		Vector<Integer> vars = new Vector<Integer>();
		Set<Integer> varsSet = new HashSet<Integer>();
		for (int i = 0; i < patvar.size(); i++) {
			pats.add(patvar.get(i).get(0));
			for (int j = 1; j < patvar.get(i).size(); j++) {
				varsSet.add(patvar.get(i).get(j));
			}
		}
		vars.addAll(varsSet);
		// for (int i = 0; i < vars.size(); i++) {
		// for (int j = i + 1; j < vars.size(); j++) {
		// if (vars.get(i) == vars.get(j)) {
		// vars.remove(j);
		// j--;
		// }
		// }
		// }
		for (int i = 0; i < vars.size(); i++) {
			Vector<Integer> temp = new Vector<Integer>();
			int var = vars.get(i);
			temp.add(var);
			for (int j = 0; j < patvar.size(); j++) {
				for (int n = 1; n < patvar.get(j).size(); n++) {
					if (var == patvar.get(j).get(n)) {
						temp.add(pats.get(j));
						break;
					}
				}
			}
			res.add(temp);
		}
		return res;
	}

	/**
	 * Return the pattern sequence given the patvar and varpat. For example, a
	 * 'varpat-list' ((x P R) (y P Q) (z Q R))produces a 'patseq' (P R Q).
	 * 'patvar-list' ((P x y) (Q y z) (R x z))
	 * @param pv
	 *            Vector<Vector<Integer>>
	 * @param vp
	 *            Vector<Vector<Integer>>
	 * @return int []
	 */
	private int[] getPatSeq(Vector<Vector<Integer>> pv,
			Vector<Vector<Integer>> vp) {
		Vector<Integer> vars = new Vector<Integer>();
		Vector<Integer> pats = new Vector<Integer>();
		for (int i = 0; i < vp.size(); i++) {
			vars.add(vp.get(i).get(0));
		}
		Vector<Integer> varuni = new Vector<Integer>();
		varuni.add(vars.get(0));
		int presize = 0;
		int patinpats = 0;
		boolean varsdone = false;
		for (int p = 0; !vars.isEmpty(); p++) {
			int var = -1;
			if (varuni.size() != presize)
				var = varuni.get(p);
			else
				var = vars.get(0);
			int varpos;
			for (varpos = 0; varpos < vp.size(); varpos++) {
				if (vp.get(varpos).get(0) == var) {
					break;
				}
			}
			for (int i = 1; i < vp.get(varpos).size(); i++) {
				pats.add(vp.get(varpos).get(i));
			}
			for (int i = 0; i < pats.size(); i++) {
				for (int j = i + 1; j < pats.size(); j++) {
					if (pats.get(i) == pats.get(j)) {
						pats.remove(j);
						j--;
					}
				}
			}
			presize = varuni.size();
			if (!varsdone) {
				for (; patinpats < pats.size(); patinpats++) {
					int pat = pats.get(patinpats);
					int patpos;
					for (patpos = 0; patpos < pv.size(); patpos++) {
						if (pv.get(patpos).get(0) == pat)
							break;
					}
					for (int j = 1; j < pv.get(patpos).size(); j++) {
						varuni.add(pv.get(patpos).get(j));
					}
				}
				for (int i = 0; i < varuni.size(); i++) {
					for (int j = i + 1; j < varuni.size(); j++) {
						if (varuni.get(i) == varuni.get(j)) {
							varuni.remove(j);
							j--;
						}
					}
				}
			}
			vars.removeElement(var);
			if (varuni.size() == vp.size())
				varsdone = true;
			if (pats.size() == pv.size())
				break;
		}
		int[] res = new int[pats.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = pats.get(i);
		}
		return res;
	}

}