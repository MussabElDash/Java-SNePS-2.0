package snip;

import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.SemanticClasses.Entity;
import sneps.SemanticClasses.Individual;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] ar) throws Exception{
		Runner.initiate();

		// building variable nodes
		Node x1 = Network.buildVariableNode();
		Node x2 = Network.buildVariableNode();
		Node x3 = Network.buildVariableNode();
		Node x4 = Network.buildVariableNode();
		Node x5 = Network.buildVariableNode();

		// building base nodes
		Individual i = new Individual();
		Node b1 = null;
		try {
			b1 = Network.buildBaseNode("Twity", i);
		} catch (Exception exp) {
		}
		//
		Individual i1 = new Individual();
		Node b2 = null;
		try {
			b2 = Network.buildBaseNode("Bird", i1);
		} catch (Exception exp) {
		}

		Entity e = new Entity();
		Node b3 = null;
		try {
			b3 = Network.buildBaseNode("V6", e);
		} catch (Exception exp) {
		}

		Individual i2 = new Individual();
		Node b4 = null;
		try {
			b4 = Network.buildBaseNode("Tree", i2);
		} catch (Exception exp) {
		}

		// building another variable node
		Node x6 = Network.buildVariableNode();

		// defining a new relation with the name: member
		Relation r1 = Network.defineRelation("member", "Individual", "none", 1);

		// defining a new relation with the name: class
		Relation r2 = Network.defineRelation("class", "Individual", "none", 1);

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

		// creating the linked list of properties to be used in the caseFrame
		// cf2
		// TODO do I need to make a method for this in Network
		LinkedList<RCFP> relP2 = new LinkedList<RCFP>();

		// adding the elements to the list
		relP2.add(rp2);
		relP2.add(rp1);
		System.out.println("hah");

		// defining another case frame with exactly the same set of relations
		// but different order
		CaseFrame cf2 = null;
		try {
			cf2 = Network.defineCaseFrame("Entity", relP2);
		} catch (Exception exp) {
		}

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
		
		Channel match = new MatchChannel();
		m1.receiveRequest(match);
		Runner.addToLowQueue(m1);
		Runner.run();
		
		System.out.println(match.getReportsBuffer().size());
	}

}
