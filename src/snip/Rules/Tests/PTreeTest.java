package snip.Rules.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.CustomException;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Entity;
import snip.Substitutions;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.PSubTree;
import snip.Rules.DataStructures.PTree;
import snip.Rules.DataStructures.RuleUseInfo;
import snip.Rules.DataStructures.RuleUseInfoSet;
import snip.Rules.Interfaces.NodeWithVar;
import snip.Rules.RuleNodes.AndNode;
import SNeBR.Support;

public class PTreeTest {
	public static void main(String[] args) throws Exception {
		Relation[] relations = new Relation[14];
		for (int i = 0; i < 14; i += 2) {
			relations[i] = Network.defineRelation((char) ('A' + i / 2) + "1",
					"Entity", "none", 1);
			relations[i + 1] = Network.defineRelation((char) ('A' + i / 2)
					+ "2", "Entity", "none", 1);
		}

		VariableNode[] varNodes = getVarNodes(10);

		ArrayList<LinkedList<RCFP>> relationsProperties = getRelProp(relations);

		CaseFrame[] caseFrames = getCaseFrames(relationsProperties);

		NodeWithVar[] antNodes = buildNodes(varNodes, relations, caseFrames);

		MolecularNode consequentNode = buildConsequent(varNodes);

		System.out.println("Relations: " + Arrays.toString(relations));
		System.out.println("Variable Nodes: " + Arrays.toString(varNodes));
		System.out.println("Antecedent Nodes: " + Arrays.toString(antNodes));
		System.out.println("Consequent Node: " + consequentNode);

		// AndNode and = getAndNode(antNodes, consequentNode);
		NodeSet nodeSet = new NodeSet();
		for (NodeWithVar mn : antNodes)
			nodeSet.addNode((Node) mn);
		PTree tree = new PTree(null);
		tree.buildTree(nodeSet);

		for (PSubTree subTree : tree.getSubTrees()) {
			System.out.println("====================================");
			System.out.println("A PSubTree:");
			subTree.printTreePatterns();
		}
		System.out.println("====================================");

		RuleUseInfo rui = getRui((Node) antNodes[0]);
		RuleUseInfoSet ruis = tree.insert(rui);
		System.out.println(ruis.cardinality());
	}

	private static VariableNode[] getVarNodes(int n) {
		VariableNode[] varNodes = new VariableNode[n];
		for (int i = 0; i < n; i++) {
			Entity e = new Entity();
			varNodes[i] = Network.buildVariableNode(e);
		}
		return varNodes;
	}

	private static ArrayList<LinkedList<RCFP>> getRelProp(Relation[] relations) {
		ArrayList<LinkedList<RCFP>> res = new ArrayList<LinkedList<RCFP>>();
		for (int i = 0; i < relations.length; i += 2) {
			RCFP rel1 = Network.defineRelationPropertiesForCF(relations[i],
					"none", 1);
			RCFP rel2 = Network.defineRelationPropertiesForCF(relations[i + 1],
					"none", 1);
			LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
			relCF1.add(rel1);
			relCF1.add(rel2);
			res.add(relCF1);
		}
		return res;
	}

	private static CaseFrame[] getCaseFrames(
			ArrayList<LinkedList<RCFP>> relationsProperties)
			throws CustomException {
		CaseFrame[] res = new CaseFrame[relationsProperties.size()];
		int index = 0;
		for (LinkedList<RCFP> link : relationsProperties)
			res[index++] = Network.defineCaseFrame("Entity", link);
		return res;
	}

	private static NodeWithVar[] buildNodes(VariableNode[] varNodes,
			Relation[] relations, CaseFrame[] caseFrames) throws Exception {
		// TODO Auto-generated method stub
		NodeWithVar[] res = new NodeWithVar[(relations.length / 2) + 1];
		res[0] = (NodeWithVar) buildMolecularNode(varNodes[0], varNodes[2],
				relations[0], relations[1], caseFrames[0]);
		res[1] = (NodeWithVar) buildMolecularNode(varNodes[1], varNodes[3],
				relations[2], relations[3], caseFrames[1]);
		res[2] = (NodeWithVar) buildMolecularNode(varNodes[2], varNodes[3],
				relations[4], relations[5], caseFrames[2]);
		res[3] = (NodeWithVar) buildMolecularNode(varNodes[4], varNodes[5],
				relations[6], relations[7], caseFrames[3]);
		res[4] = (NodeWithVar) buildMolecularNode(varNodes[4], varNodes[6],
				relations[8], relations[9], caseFrames[4]);
		res[5] = (NodeWithVar) buildMolecularNode(varNodes[7], varNodes[9],
				relations[10], relations[11], caseFrames[5]);
		res[6] = (NodeWithVar) buildMolecularNode(varNodes[8], varNodes[9],
				relations[12], relations[13], caseFrames[6]);
		res[7] = varNodes[0];
		return res;
	}

	private static MolecularNode buildMolecularNode(VariableNode variableNode,
			VariableNode variableNode2, Relation relation, Relation relation2,
			CaseFrame caseFrame) throws Exception {
		Object[][] a1 = new Object[2][2];
		a1[0][0] = relation;
		a1[0][1] = variableNode;
		a1[1][0] = relation2;
		a1[1][1] = variableNode2;
		return Network.buildMolecularNode(a1, caseFrame);
	}

	private static MolecularNode buildConsequent(VariableNode... varNodes)
			throws Exception {
		Object[][] a1 = new Object[varNodes.length][2];
		Relation[] relations = new Relation[varNodes.length];
		LinkedList<RCFP> rcfps = new LinkedList<RCFP>();
		for (int i = 0; i < varNodes.length; i++) {
			relations[i] = Network.defineRelation("H" + (i + 1), "Entity",
					"none", 1);
			rcfps.add(Network.defineRelationPropertiesForCF(relations[i],
					"none", 1));
		}
		CaseFrame caseFrame = Network.defineCaseFrame("Entity", rcfps);
		for (int i = 0; i < varNodes.length; i++) {
			a1[i][0] = relations[i];
			a1[i][1] = varNodes[i];
		}
		return Network.buildMolecularNode(a1, caseFrame);
	}

	// private static AndNode getAndNode(MolecularNode[] antNodes,
	// MolecularNode consequentNode) throws Exception {
	// // TODO Auto-generated method stub
	// Relation ant = Network.defineRelation("&ant", "Entity", "none", 1);
	// Relation conq = Network.defineRelation("cq", "Entity", "none", 1);
	// LinkedList<RCFP> antcqRCFP = new LinkedList<RCFP>();
	// antcqRCFP.add(Network.defineRelationPropertiesForCF(ant, "none", 1));
	// antcqRCFP.add(Network.defineRelationPropertiesForCF(conq, "none", 1));
	// CaseFrame antcqCaseFrame = Network.defineCaseFrame("Entity", antcqRCFP);
	// Object[][] a1 = new Object[8][2];
	// for (int i = 0; i < 7; i++) {
	// a1[i][0] = ant;
	// a1[i][1] = antNodes[i];
	// }
	// a1[7][0] = conq;
	// a1[7][1] = consequentNode;
	// AndNode mc = Network.buildAndNode(a1, antcqCaseFrame);
	// System.out.println(mc);
	// return mc;
	// }

	private static RuleUseInfo getRui(Node n) {
		FlagNode fn = new FlagNode(n, new Support(), 1);
		FlagNodeSet fns = new FlagNodeSet();
		fns.insert(fn);
		RuleUseInfo rui = new RuleUseInfo(new Substitutions(), 1, 0, fns);
		return rui;
	}
}
