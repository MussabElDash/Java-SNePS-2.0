package snip.tests;

import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Individual;
import sneps.match.LinearSubstitutions;
import snip.Channel;
import snip.RuleToConsequentChannel;
import snip.Runner;
import snip.Rules.RuleNodes.AndNode;
import SNeBR.SNeBR;

public class TestGameel {
	
	public static void main(String[]a) throws Exception {
		CaseFrame.createRuleCaseFrame();
		Runner.initiate();
		// building variable nodes

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
		System.out.println(cf1.getSemanticClass().toString());
		// building base nodes
		Individual i = new Individual();
		Node b1 = null;
		try {
			b1 = Network.buildBaseNode("fido", i);
		} catch (Exception exp) {
		}
		//
		Individual i1 = new Individual();
		Node b2 = null;
		try {
			b2 = Network.buildBaseNode("dog", i1);
		} catch (Exception exp) {
		}

		// the relation-node pair to be used in building Molecular Node m1
		Object[][] a1 = new Object[2][2];
		a1[0][0] = r1;
		a1[0][1] = b1;
		a1[1][0] = r2;
		a1[1][1] = b2;

		// building molecular node m1
		MolecularNode ant1 = null;
		try {
			ant1 = Network.buildMolecularNode(a1, cf1);
		} catch (Exception exp) {
		}
		System.out.println(ant1.getSemanticType() + " " + ant1);
		// building base nodes
		i = new Individual();
		b1 = null;
		try {
			b1 = Network.buildBaseNode("socrates", i);
		} catch (Exception exp) {
		}
		//
		i1 = new Individual();
		b2 = null;
		try {
			b2 = Network.buildBaseNode("men", i1);
		} catch (Exception exp) {
		}

		// the relation-node pair to be used in building Molecular Node m1
		a1 = new Object[2][2];
		a1[0][0] = r1;
		a1[0][1] = b1;
		a1[1][0] = r2;
		a1[1][1] = b2;

		// building molecular node m1
		MolecularNode ant2 = null;
		try {
			ant2 = Network.buildMolecularNode(a1, cf1);
		} catch (Exception exp) {
		}
		System.out.println(ant2.getSemanticType() + " " + ant2);
		// building base nodes
		i = new Individual();
		b1 = null;
		try {
			b1 = Network.buildBaseNode("twitty", i);
		} catch (Exception exp) {
		}
		//
		i1 = new Individual();
		b2 = null;
		try {
			b2 = Network.buildBaseNode("bird", i1);
		} catch (Exception exp) {
		}

		// the relation-node pair to be used in building Molecular Node m1
		a1 = new Object[2][2];
		a1[0][0] = r1;
		a1[0][1] = b1;
		a1[1][0] = r2;
		a1[1][1] = b2;

		// building molecular node m1
		MolecularNode cons = null;
		try {
			cons = Network.buildMolecularNode(a1, cf1);
		} catch (Exception exp) {
		}
		System.out.println(cons.getSemanticType() + " " + cons);
		// ============================================================

		// the relation-node pair to be used in building Molecular Node m1
		a1 = new Object[3][2];
		a1[0][0] = Relation.andAnt;
		a1[0][1] = ant1;
		a1[1][0] = Relation.andAnt;
		a1[1][1] = ant2;
		a1[2][0] = Relation.cq;
		a1[2][1] = cons;

		// building molecular node m1
		AndNode andRule = (AndNode)Network.buildMolecularNode(a1, CaseFrame.andRule);
//		} catch (Exception exp) {
//			System.out.println(exp.getMessage());
//		}
		SNeBR.assertProposition((PropositionNode) ant1);
		SNeBR.assertProposition((PropositionNode) ant2);
		SNeBR.assertProposition((PropositionNode) andRule);

		System.out.println("built");
		System.out.println(andRule.getClass().toString());
		
//		ArrayList<Pair> list = getMatched(m1);
		Channel csqRule = new RuleToConsequentChannel(new LinearSubstitutions(), new LinearSubstitutions(), SNeBR.getCurrentContext().getId(), cons, andRule, true);
//		System.out.println("Switch subs " + list.get(0).getSwitch());
//		Channel match = new MatchChannel(list.get(0).getSwitch(), list.get(0)
//				.getFilter(), 0, m1, true);
		andRule.receiveRequest(csqRule);
//		list.get(0).getNode().receiveRequest(match);
//		// Runner.addToLowQueue(list.get(0).getNode());
		String n = Runner.run();
		System.out.println("Call one seq is " + n);
		System.out.println("Reports buffer " + csqRule.getReportsBuffer().size());
		
	}
}
