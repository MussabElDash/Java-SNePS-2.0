package snip.Rules.DataStructures;

import java.util.Iterator;

public class PSubTree {
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
		int pattern = rui.getFlagNodeSet().iterator().next().getNode().getId();
		PTreeNode leaf = getLeafPattern(pattern, root);
		leaf.getRUIS().putIn(rui);
		return insertIn(leaf.getParent());
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

	private RuleUseInfoSet insertIn(PTreeNode parent) {
		Iterator<RuleUseInfo> iter1 = parent.getLeft().getRUIS().iterator();
		while (iter1.hasNext()) {
			RuleUseInfo rui1 = iter1.next();
			Iterator<RuleUseInfo> iter2 = parent.getRight().getRUIS()
					.iterator();
			while (iter2.hasNext()) {
				RuleUseInfo rui2 = iter2.next();
				RuleUseInfo combined = rui1.combine(rui2);
				if (combined == null)
					continue;
				iter1.remove();
				iter2.remove();
				parent.getRUIS().putIn(combined);
				break;
			}
		}
		if (parent == root)
			return parent.getRUIS();
		else
			return insertIn(parent.getParent());
	}
}
