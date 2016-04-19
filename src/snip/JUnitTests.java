package snip;

import java.util.LinkedList;

import junit.framework.TestCase;
import sneps.CaseFrame;
import sneps.CustomException;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.VariableNode;

public class JUnitTests extends TestCase{
	
	public CaseFrame cf1;
	public Relation r1, r2;
	private VariableNode var1;
	private VariableNode var2;

	public void setUp() throws CustomException {
		Runner.initiate();
		// building variable nodes
		
		var1 = Network.buildVariableNode();
		var2 = Network.buildVariableNode();
		
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
		
		
	}
	public void testRunner() throws CustomException {
//		setUp();

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
			System.out.println(exp.getMessage() + " msg ");
		}
//		SNeBR.assertProposition(m1);
		Channel ch = new MatchChannel();
		System.out.println(m1);
		m1.receiveRequest(ch);
		String seq = Runner.run();
		assertEquals(seq, "L");
	}
}
