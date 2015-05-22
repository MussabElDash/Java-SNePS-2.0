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
import sneps.SemanticClasses.Individual;
import sneps.SemanticClasses.Infimum;
import sneps.match.Binding;
import sneps.match.LinearSubstitutions;
import sneps.match.Substitutions;
import snip.Rules.DataStructures.FlagNode;
import snip.Rules.DataStructures.FlagNodeSet;
import snip.Rules.DataStructures.PTree;
import snip.Rules.DataStructures.RuleUseInfo;
import snip.Rules.DataStructures.RuleUseInfoSet;
import snip.Rules.Interfaces.NodeWithVar;

public class PTreeTest {
	public static void main(String[] args) throws Exception {
		CaseFrame.createRuleCaseFrame();
		Relation[] relations = new Relation[18];
		for (int i = 0; i < relations.length; i += 2) {
			relations[i] = Network.defineRelation((char) ('A' + i / 2) + "1",
					"Infimum", "none", 1);
			relations[i + 1] = Network.defineRelation((char) ('A' + i / 2)
					+ "2", "Infimum", "none", 1);
		}

		VariableNode[] varNodes = getVarNodes(10);

		ArrayList<LinkedList<RCFP>> relationsProperties = getRelProp(relations);

		CaseFrame[] caseFrames = getCaseFrames(relationsProperties);

		NodeWithVar[] antNodesTemp = buildNodes(varNodes, relations, caseFrames);

		MolecularNode consequentNode = buildConsequent(varNodes);

		System.out.println("Relations: " + Arrays.toString(relations));
		System.out.println("Variable Nodes: " + Arrays.toString(varNodes));
		System.out
				.println("Antecedent Nodes: " + Arrays.toString(antNodesTemp));
		System.out.println("Consequent Node: " + consequentNode);

		getAndNode(antNodesTemp, consequentNode);
		NodeSet nodeSet = new NodeSet();
		for (NodeWithVar mn : antNodesTemp)
			nodeSet.addNode((Node) mn);
		PTree tree = new PTree(-1);
		tree.buildTree(nodeSet);

		tree.printSubTrees();

		System.out.println("====================================");

		Node bob = Network.buildBaseNode("Bob", new Individual());
		Node mary = Network.buildBaseNode("mary", new Individual());
		Node mussab = Network.buildBaseNode("mussab", new Individual());
		Node eldash = Network.buildBaseNode("eldash", new Individual());

		System.out.println("====================================");
		System.out.println("Inserting a RUI in the Tree");

		insertIntoTree(tree, bob, mary, mussab, eldash, antNodesTemp, varNodes);
		System.out.println("Inserting another RUI in the Tree");
		insertIntoTree(tree, mussab, bob, mary, eldash, antNodesTemp, varNodes);
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
			res[index++] = Network.defineCaseFrame("Proposition", link);
		return res;
	}

	private static NodeWithVar[] buildNodes(VariableNode[] varNodes,
			Relation[] relations, CaseFrame[] caseFrames) throws Exception {
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
		res[8] = (NodeWithVar) buildPropositionNode(relations[14],
				relations[15], caseFrames[7], "a", "b");
		res[9] = (NodeWithVar) buildPropositionNode(relations[16],
				relations[17], caseFrames[8], "c", "d");
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

	private static MolecularNode buildPropositionNode(Relation relation,
			Relation relation2, CaseFrame caseFrame, String name1, String name2)
			throws CustomException, Exception {
		Object[][] a1 = new Object[2][2];
		a1[0][0] = relation;
		a1[0][1] = Network.buildBaseNode(name1, new Infimum());
		a1[1][0] = relation2;
		a1[1][1] = Network.buildBaseNode(name2, new Infimum());
		return Network.buildMolecularNode(a1, caseFrame);
	}

	private static MolecularNode buildConsequent(VariableNode... varNodes)
			throws Exception {
		Object[][] a1 = new Object[varNodes.length][2];
		Relation[] relations = new Relation[varNodes.length];
		LinkedList<RCFP> rcfps = new LinkedList<RCFP>();
		for (int i = 0; i < varNodes.length; i++) {
			relations[i] = Network.defineRelation("Z" + (i + 1), "Proposition",
					"none", 1);
			rcfps.add(Network.defineRelationPropertiesForCF(relations[i],
					"none", 1));
		}
		CaseFrame caseFrame = Network.defineCaseFrame("Proposition", rcfps);
		for (int i = 0; i < varNodes.length; i++) {
			a1[i][0] = relations[i];
			a1[i][1] = varNodes[i];
		}
		return Network.buildMolecularNode(a1, caseFrame);
	}

	private static Node getAndNode(NodeWithVar[] antNodesTemp,
			MolecularNode consequentNode) throws Exception {
		Relation ant = Relation.andAnt;
		Relation conq = Relation.cq;
		CaseFrame antcqCaseFrame = CaseFrame.andRule;
		Object[][] a1 = new Object[8][2];
		for (int i = 0; i < 7; i++) {
			a1[i][0] = ant;
			a1[i][1] = (MolecularNode) antNodesTemp[i];
		}
		a1[7][0] = conq;
		a1[7][1] = consequentNode;
		MolecularNode mc = Network.buildMolecularNode(a1, antcqCaseFrame);
		System.out.println(mc.getClass().getSimpleName());
		return mc;
	}

	private static RuleUseInfo getRui(Node signature, VariableNode var,
			Node subs) {
		FlagNode fn = new FlagNode(signature, null, 1);
		FlagNodeSet fns = new FlagNodeSet();
		fns.insert(fn);
		Substitutions sub = new LinearSubstitutions();
		sub = sub.insert(new Binding(var, subs));
		RuleUseInfo rui = new RuleUseInfo(sub, 1, 0, fns);
		return rui;
	}

	private static RuleUseInfo getRui(Node signature, VariableNode var1,
			VariableNode var2, Node sub1, Node sub2) {
		FlagNode fn = new FlagNode(signature, null, 1);
		FlagNodeSet fns = new FlagNodeSet();
		fns.insert(fn);
		Substitutions sub = new LinearSubstitutions();
		sub = sub.insert(new Binding(var1, sub1));
		sub = sub.insert(new Binding(var2, sub2));
		RuleUseInfo rui = new RuleUseInfo(sub, 1, 0, fns);
		return rui;
	}

	private static void insertIntoTree(PTree tree, Node c1, Node c2, Node c3,
			Node c4, NodeWithVar[] antNodesTemp, VariableNode[] varNodes) {
		RuleUseInfo rui = getRui((Node) antNodesTemp[0], varNodes[0],
				varNodes[2], c1, c3);
		printRuis(tree.insertRUI(rui));
		rui = getRui((Node) antNodesTemp[1], varNodes[1], varNodes[3], c2, c4);
		printRuis(tree.insertRUI(rui));
		rui = getRui((Node) antNodesTemp[2], varNodes[2], varNodes[3], c3, c4);
		printRuis(tree.insertRUI(rui));
		rui = getRui((Node) antNodesTemp[7], varNodes[0], c1);
		printRuis(tree.insertRUI(rui));
	}

	private static void printRuis(RuleUseInfoSet ruis) {
		System.out.println("Number of RUIs is: " + ruis.cardinality());
		for (RuleUseInfo tRui : ruis) {
			System.out.println("====================================");
			for (int i = 0; i < tRui.getSub().cardinality(); i++) {
				System.out.print(tRui.getSub().getBinding(i).getVariable()
						.getIdentifier());
				System.out.print(" is Bound to: ");
				System.out.println(tRui.getSub().getBinding(i).getNode()
						.getIdentifier());
			}
		}
		System.out.println("====================================");
		System.out.println("====================================");
	}
}
