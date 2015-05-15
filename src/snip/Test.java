package snip;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Individual;
import sneps.match.Binding;
import sneps.match.LinearSubstitutions;
import sneps.match.Substitutions;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] ar) throws Exception {
		Runner.initiate();

		// building variable nodes
		Node var1 = Network.buildVariableNode("x");
		Node var2 = Network.buildVariableNode("y");

		// defining a new relation with the name: member
		Relation r1 = Network
				.defineRelation("husband", "Individual", "none", 1);

		// defining a new relation with the name: class
		Relation r2 = Network.defineRelation("wife", "Individual", "none", 1);

		// building a relation case frame properties structure for relation r1
		RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none", 1);

		// building a relation case frame properties structure for relation r1
		RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 1);

		// creating the linked list of properties to be used in the caseFrame
		// cf1
		// TODO do I need to make a method for this in Network
		LinkedList<RCFP> relProperties = new LinkedList<RCFP>();

		// adding the elements to the list
		relProperties.add(rp1);
		relProperties.add(rp2);

		// defining a new case frame
		CaseFrame cf1 = Network.defineCaseFrame("Proposition", relProperties);

		// the relation-node pair to be used in building Molecular Node m1
		Object[][] a1 = new Object[2][2];
		a1[0][0] = r1;
		a1[0][1] = var1;
		a1[1][0] = r2;
		a1[1][1] = var2;

		// building molecular node m1
		MolecularNode m1 = null;
		try {
			m1 = Network.buildMolecularNode(a1, cf1);
		} catch (Exception exp) {
		}
		
		ArrayList<Pair> list = getMatched(m1);
		System.out.println("Switch subs " + list.get(0).getSwitch());
		Channel match = new MatchChannel(list.get(0).getSwitch(), list.get(0).getFilter(), 0, m1, true);
		
		list.get(0).getNode().receiveRequest(match);
//		Runner.addToLowQueue(list.get(0).getNode());
		String n = Runner.run();
		System.out.println("Reports buffer " + match.getReportsBuffer().size());
		System.out.println(match.getReportsBuffer().get(0).getSubstitutions());
		System.out.println("Sequence " + n);
	}

	public static ArrayList<Pair> getMatched(Node node) throws Exception {
		ArrayList<Pair> ret = new ArrayList<Pair>();

		// building base nodes
		Individual i = new Individual();
		Node b1 = null;
		try {
			b1 = Network.buildBaseNode("mike", i);
		} catch (Exception exp) {
		}
		//
		Individual i1 = new Individual();
		Node b2 = null;
		try {
			b2 = Network.buildBaseNode("jane", i1);
		} catch (Exception exp) {
		}

		// defining a new relation with the name: member
		Relation r1 = Network
				.defineRelation("husband1", "Individual", "none", 1);

		// defining a new relation with the name: class
		Relation r2 = Network.defineRelation("wife1", "Individual", "none", 1);

		// building a relation case frame properties structure for relation r1
		RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none", 1);

		// building a relation case frame properties structure for relation r1
		RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 1);

		// creating the linked list of properties to be used in the caseFrame
		// cf1
		// TODO do I need to make a method for this in Network
		LinkedList<RCFP> relProperties = new LinkedList<RCFP>();

		// adding the elements to the list
		relProperties.add(rp1);
		relProperties.add(rp2);

		// defining a new case frame
		CaseFrame cf1 = Network.defineCaseFrame("Proposition", relProperties);

		// the relation-node pair to be used in building Molecular Node m1
		Object[][] a1 = new Object[2][2];
		a1[0][0] = r1;
		a1[0][1] = b1;
		a1[1][0] = r2;
		a1[1][1] = b2;

		// building molecular node m1
		MolecularNode m1 = null;
		try {
			m1 = Network.buildMolecularNode(a1, cf1);
		} catch (Exception exp) {
		}

		// building another variable node
		VariableNode varNode1 = Network.buildVariableNode("x");

		VariableNode varNode2 = Network.buildVariableNode("y");
		Binding bind1 = new Binding(varNode1, b1);
		Binding bind2 = new Binding(varNode2, b2);
		Substitutions switchSub = new LinearSubstitutions();
		switchSub.putIn(bind1);
		switchSub.putIn(bind2);
		
		Substitutions filterSub = new LinearSubstitutions();
		
		Pair firstMatch = new Pair(filterSub, switchSub, m1);
		
		ret.add(firstMatch);
		return ret;
	}

}
