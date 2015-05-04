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
