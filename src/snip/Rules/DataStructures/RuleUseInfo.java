package snip.Rules.DataStructures;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sneps.match.Substitutions;
import SNeBR.Support;

public class RuleUseInfo {
	private Substitutions sub;
	private int pos;
	private int neg;
	private FlagNodeSet fns;

	/**
	 * Create new rule use info
	 * 
	 * @param s
	 *            substitutions list
	 * @param p
	 *            number of positive substitutions
	 * @param n
	 *            number of negative substitutions
	 * @param num
	 *            number of antecedents
	 */
	public RuleUseInfo(Substitutions s, int p, int n, FlagNodeSet f) {
		sub = s;
		pos = p;
		neg = n;
		fns = f;
	}

	/**
	 * Return the substitutions list
	 * 
	 * @return Substitutions
	 */
	public Substitutions getSub() {
		return sub;
	}

	/**
	 * Return the number of positive substitutions
	 * 
	 * @return int
	 */
	public int getPosCount() {
		return pos;
	}

	/**
	 * Return the number of negative substitutions
	 * 
	 * @return int
	 */
	public int getNegCount() {
		return neg;
	}

	/**
	 * Returns a flag node set with which antecedents are negative only
	 * 
	 * @return FlagNodeSet
	 */
	public FlagNodeSet getNegSubs() {
		FlagNodeSet res = new FlagNodeSet();
		// for (int i = 0; i < fns.cardinality(); i++) {
		// if (fns.getFlagNode(i).getFlag() == 2)
		// res.putIn(fns.getFlagNode(i));
		// }
		for (FlagNode fn : fns) {
			if (fn.getFlag() == 2)
				res.putIn(fn);
		}
		return res;
	}

	/**
	 * Returns a flag node set with which antecedents are positive only
	 * 
	 * @return FlagNodeSet
	 */
	public FlagNodeSet getPosSubs() {
		FlagNodeSet res = new FlagNodeSet();
		// for (int i = 0; i < fns.cardinality(); i++) {
		// if (fns.getFlagNode(i).getFlag() == 1)
		// res.putIn(fns.getFlagNode(i));
		// }
		for (FlagNode fn : fns) {
			if (fn.getFlag() == 1)
				res.putIn(fn);
		}
		return res;
	}

	/**
	 * Return the flag node set of the rule use info
	 * 
	 * @return FlagNodeSet
	 */
	public FlagNodeSet getFlagNodeSet() {
		return fns;
	}

	/**
	 * Check if this and r have no binding conflicts
	 * 
	 * @param r
	 *            rule use info
	 * @return true or false
	 */
	public boolean isVarsCompatible(RuleUseInfo r) {
		return sub.isCompatible(r.sub);
	}

	/**
	 * Check if this and r are joint
	 * 
	 * @param r
	 *            rule use info
	 * @return true or false
	 */
	public boolean isJoint(RuleUseInfo r) {
		// for (int i = 0; i < fns.cardinality(); i++) {
		// for (int j = 0; j < r.getFlagNodeSet().cardinality(); j++) {
		// if (fns.getFlagNode(i)
		// .getNode()
		// .getIdentifier()
		// .equals(r.getFlagNodeSet().getFlagNode(j).getNode()
		// .getIdentifier()))
		// return true;
		// }
		// }
		for (FlagNode fn1 : fns) {
			for (FlagNode fn2 : r.getFlagNodeSet()) {
				System.out.println("---->> " + fn1.getNode() + " " + fn1.getNode());
				if (fn1.getNode() == fn2.getNode())
					return true;
			}
		}
		return false;
	}

	/**
	 * Check if this and r are disjoint
	 * 
	 * @param r
	 *            rule use info
	 * @return true or false
	 */
	public boolean isDisjoint(RuleUseInfo r) {
		return !isJoint(r);
	}

	/**
	 * combine rui with this rule use info
	 * 
	 * @param rui
	 *            RuleUseInfo
	 * @return RuleUseInfo
	 */
	public RuleUseInfo combine(RuleUseInfo rui) {
		System.out.println(this.isDisjoint(rui) + " " +  this.isVarsCompatible(rui));
		if (this.isDisjoint(rui) && this.isVarsCompatible(rui)) {
			return new RuleUseInfo(this.getSub().union(rui.getSub()), this.pos
					+ rui.pos, this.neg + rui.neg, this.fns.union(rui.fns));
		}
		return null;
	}

	public Set<Support> getSupport(Set<Support> originSupport) {
		Set<Support> ruiSupports = getSupports();
		return Support.combine(originSupport, ruiSupports);
	}

	private Set<Support> getSupports() {
		if (fns.isNew())
			return new HashSet<Support>();
		if (fns.cardinality() == 1)
			return fns.iterator().next().getSupports();
		Iterator<FlagNode> fnIter = fns.iterator();
		Set<Support> res = fnIter.next().getSupports();
		while (fnIter.hasNext()) {
			Set<Support> toBeCombined = fnIter.next().getSupports();
			res = Support.combine(res, toBeCombined);
		}
		return res;
	}
}