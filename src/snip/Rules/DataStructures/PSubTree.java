package snip.Rules.DataStructures;


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
		Integer key = leaf.insertIntoTree(rui);
		if (key == null)
			return new RuleUseInfoSet();
		return root.getRUIS(key);
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

	// private Rs
}
