package sneps.Tests;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.print.attribute.standard.Finishings;

import sneps.CFSignature;
import sneps.CableTypeConstraint;
import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.SubDomainConstraint;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.VariableNode;
import sneps.Paths.AndPath;
import sneps.Paths.BUnitPath;
import sneps.Paths.FUnitPath;
import sneps.Paths.KStarPath;
import sneps.Paths.OrPath;
import sneps.Paths.Path;
import sneps.SemanticClasses.Entity;
import sneps.SemanticClasses.Individual;
import sneps.SyntaticClasses.Pattern;
import SNeBR.Context;

	//import sneps.Network;

public class NetworkTesting {
	
	public static void main(String[] args) throws Exception{
		
		// building variable nodes
		Node x1 = Network.buildVariableNode();
		printBuildingNodeResults(x1, "x1");
		Node x2 = Network.buildVariableNode();
		printBuildingNodeResults(x2, "x2");
		Node x3 = Network.buildVariableNode();
		printBuildingNodeResults(x3, "x3");
		Node x4 = Network.buildVariableNode();
		printBuildingNodeResults(x4, "x4");
		Node x5 = Network.buildVariableNode();
		printBuildingNodeResults(x5, "x5");
		
		// building base nodes
		Individual i = new Individual();
		Node b1 = null;
		try{
			 b1 = Network.buildBaseNode("V3", i);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b1, "b1");
		//
		Individual i1 = new Individual();
		Node b2 = null;
		try{
			 b2 = Network.buildBaseNode("Bird", i1);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b2, "b2");

		Entity e = new Entity();
		Node b3 = null;
		try{
			 b3 = Network.buildBaseNode("V6", e);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b3, "b3");
		
		Individual i2 = new Individual();
		Node b4 = null;
		try{
			 b4 = Network.buildBaseNode("Tree", i2);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b4, "b4");
		
		// building another variable node
		Node x6 = Network.buildVariableNode();
		printBuildingNodeResults(x6, "x4");
		
		// defining a new relation with the name: member
		Relation r1 = Network.defineRelation("member", "Infimum", "none", 1);
		printDefiningRelationResults(r1, "r1");
		
		// defining a new relation with the name: class
		Relation r2 = Network.defineRelation("class", "Individual", "none", 1);
		printDefiningRelationResults(r2, "r2");
		
		// building a relation case frame properties structure for relation r1
		RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none", 2);
		
		// building a relation case frame properties structure for relation r1
		RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		
		// creating the linked list of properties to be used in the caseFrame cf1
		// TODO do I need to make a method for this in Network
		LinkedList<RCFP> relProperties = new LinkedList<RCFP>();
		
		// adding the elements to the list
		relProperties.add(rp1);
		relProperties.add(rp2);
		
		// defining a new case frame
		CaseFrame cf1 = Network.defineCaseFrame("Proposition", relProperties);
		printDefiningCaseFrameResults(cf1, "cf1");
		
		// creating the linked list of properties to be used in the caseFrame cf2
		// TODO do I need to make a method for this in Network
		LinkedList<RCFP> relP2 = new LinkedList<RCFP>();
		
		// adding the elements to the list
		relP2.add(rp2);
		relP2.add(rp1);
		
		// defining another case frame with exactly the same set of relations but different order
		CaseFrame cf2 = null;
		try{
			cf2 = Network.defineCaseFrame("Entity", relP2);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printDefiningCaseFrameResults(cf2, "cf2");
		
		// the relation-node pair to be used in building Molecular Node m1
		Object [][] a1 = new Object[2][2];
		a1[0][0] = r1;
		a1[0][1] = x1;
		a1[1][0] = r2;
		a1[1][1] = b2;
		
		// building molecular node m1
		MolecularNode m1 = null;
		try{
			m1 = Network.buildMolecularNode(a1, cf1);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(m1, "m1");
		
		// the relation-node pair to be used in building Molecular Node m2
		Object [][] a2 = new Object[3][2];
		a2[0][0] = r1;
		a2[0][1] = x1;
		a2[1][0] = r2;
		a2[1][1] = b2;
		a2[2][0] = r1;
		a2[2][1] = x2;
		
//		Object[][] relNodeSet = Network.turnIntoRelNodeSet(a2);
//		System.out.println("length " + relNodeSet.length);
//		for(int j = 0; j < relNodeSet.length; j++){
//			System.out.print(((Relation)relNodeSet[j][0]).toString() + " ");
//			System.out.println(((NodeSet)relNodeSet[j][1]).toString() + " ");
//		}
		
		// building molecular node m2
		MolecularNode m2 = null;
		try{
			m2 = Network.buildMolecularNode(a2, cf1);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(m2, "m2");
		
		// the relation-node pair to be used in building Molecular Node m3
		Object [][] a3 = new Object[3][2];
		a3[0][0] = r1;
		a3[0][1] = x1;
		a3[1][0] = r2;
		a3[1][1] = b2;
		a3[2][0] = r1;
		a3[2][1] = x3;
		
		// building molecular node m3
		MolecularNode m3 = null;
		try{
			m3 = Network.buildMolecularNode(a3, cf1);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(m3, "m3");
		
		// building m4 with the same down cable set as m2
		MolecularNode m4 = null;
		try{
			m4 = Network.buildMolecularNode(a2, cf1);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(m4, "m4");
		
		// the relation-node pair to be used in building Molecular Node m5
		Object [][] a4 = new Object[4][2];
		a4[0][0] = r1;
		a4[0][1] = x1;
		a4[1][0] = r2;
		a4[1][1] = b4;
		a4[2][0] = r1;
		a4[2][1] = x3;
		a4[3][0] = r2;
		a4[3][1] = b2;
		
		MolecularNode m5 = null;
		try{
			m5 = Network.buildMolecularNode(a4, cf1);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(m5, "m5");

		// removing node x2 from the network
		try{
			Network.removeNode(x2);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		checkNodeRemoved(x2, "x2");
		
		// removing node x4 from the network
		try{
			Network.removeNode(x4);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		checkNodeRemoved(x4, "x4");
		
		// removing node x5 from the network
		try{
			Network.removeNode(x5);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		checkNodeRemoved(x5, "x5");
		
		// removing node b3 from the network
		try{
			Network.removeNode(b3);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		checkNodeRemoved(b3, "b3");
		
		printCompactStart();
		Network.compact();
		printCompactEnd();
		
		// removing case frame cf1 from the network
		try{
			Network.undefineCaseFrame(cf1.getId());
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}

////////////////////////////////////////////////////////////////// Case Frame Signatures /////////////////////////////////////////////////////////////		
		
		// building structure as example to check case frame rules
		System.out.println("Testing Case Frame Signatures");
		
		// building four base nodes
		Node b5 = null;
		try{
			 b5 = Network.buildBaseNode("page1", i);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b5, "b5");
		
		Node b6 = null;
		try{
			 b6 = Network.buildBaseNode("page2", i);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b6, "b6");
		
		Node b7 = null;
		try{
			 b7 = Network.buildBaseNode("coverFront", i);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b7, "b7");
		
		Node b8 = null;
		try{
			 b8 = Network.buildBaseNode("coverBack", e);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(b8, "b8");
		
		Relation r3 = Network.defineRelation("contains", "Entity", "none", 1);
		printDefiningRelationResults(r3, "r3");
		
		Relation r4 = Network.defineRelation("covered with", "Entity", "none", 1);
		printDefiningRelationResults(r4, "r4");
		
		RCFP rp3 = Network.defineRelationPropertiesForCF(r3, "none", 1);
		
		RCFP rp4 = Network.defineRelationPropertiesForCF(r4, "none", 1);
		
		LinkedList<RCFP> relP3 = new LinkedList<RCFP>();
		relP3.add(rp3);
		relP3.add(rp4);
		
		CaseFrame cf3 = null;
		try{
			cf3 = Network.defineCaseFrame("Entity", relP3);
		}
		catch(Exception exp){
			printException(exp.getMessage());
		}
		printDefiningCaseFrameResults(cf3, "cf3");
		
		// adding case frame rules
		CableTypeConstraint ct = new CableTypeConstraint("Individual", null, null);
		
		SubDomainConstraint st = new SubDomainConstraint("contains", new LinkedList(Arrays.asList(ct)));
		
		CableTypeConstraint ct1 = new CableTypeConstraint("Individual", 1, null);
		
		SubDomainConstraint st1 = new SubDomainConstraint("covered with", new LinkedList(Arrays.asList(ct1)));
		
		CFSignature cf3sig1 = new CFSignature("Proposition", new LinkedList(Arrays.asList(st, st1)), cf3.getId());
		
		cf3.addSignature(cf3sig1, 0);
		
		Object[][] a5 = new Object[4][2];
		a5[0][0] = r3;
		a5[0][1] = b5;
		a5[1][0] = r3;
		a5[1][1] = b6;
		a5[2][0] = r4;
		a5[2][1] = b7;
		a5[3][0] = r4;
		a5[3][1] = b8;
//		a5[4][0] = r4;
//		a5[4][1] = b6;
		
		Node m6 = null;
		try{
			m6 = Network.buildMolecularNode(a5, cf3);
		} catch(Exception exp){
			printException(exp.getMessage());
		}
		printBuildingNodeResults(m6, "m6");	
		
/////////////////////////////////////////////////////// Testing Case Frame Conflicts ////////////////////////////////////////////////////////////
		
		
}
	
	private static void printBuildingNodeResults(Node n, String name){
		System.out.println("");
		if(n != null){
			System.out.println("Node " + name + " was successfully built ... ");
			System.out.println("");
			System.out.println("Current count of the nodes in the system: " + Node.getCount());
			System.out.println("");
			System.out.println("Checking its paramters: ");
			System.out.println("-----------------------");
			System.out.println("ID: " + n.getId());
			System.out.println("Syntactic type: " + n.getSyntacticType());
			System.out.println("Semantic type: " + n.getSemanticType());
			System.out.println("Identifier: " + n.getIdentifier());
			if(n.getSyntacticType().equals("Pattern")){
				System.out.println("");
				System.out.println("The free variables dominated by this node: ");
				System.out.println("------------------------------------------");
				LinkedList<VariableNode> freeVars =  ((Pattern)n.getSyntactic()).getFreeVariables();
				for(int i = 0; i < freeVars.size(); i++){
					System.out.println((i + 1) + ". " + freeVars.get(i).getIdentifier());
				}
			}
		}
		else{
			System.out.println("The Node named, " + name + ", was not successfully built ... ");
		}
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
		
	}
	
	private static void printDefiningRelationResults(Relation r, String name){
		System.out.println("");
		if(r != null){
			System.out.println("Relation " + name + " was successfully defined ... ");
			System.out.println("");
			System.out.println("Checking its paramters: ");
			System.out.println("-----------------------");
			System.out.println("Name: " + r.getName());
			System.out.println("The Semantic type of nodes that this relation can point at: " + r.getType());
			System.out.println("Adjustability: " + r.getAdjust());
			System.out.println("Limit: " + r.getLimit());
			System.out.println("Path: " + r.getPath());
			System.out.println("toString: " + r.toString());
		}
		else{
			System.out.println("The Relation named, " + name + ", was not successfully defined ... ");
		}
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
		
	}
	
	private static void printDefiningCaseFrameResults(CaseFrame cf, String name){
		System.out.println("");
		if(cf != null){
			System.out.println("Case frame " + name + " was successfully defined ... ");
			System.out.println("");
			System.out.println("Checking its paramters: ");
			System.out.println("-----------------------");
			System.out.println("The default Semantic type of nodes implementing this case frame: " + 
			cf.getSemanticClass());
			System.out.println("ID: " + cf.getId());
			System.out.println("");
			System.out.println("The relation(s) included in this case frame: ");
			System.out.println("--------------------------------------------");
			Hashtable<String, RCFP> rel = cf.getRelations();
			Enumeration<RCFP> e = rel.elements();
			while(e.hasMoreElements()) {
			   System.out.println(e.nextElement().getRelation().getName());
			}
			System.out.println("");
			System.out.println("The relation(s) along with their properties within this case frame" +
					" included in this case frame: ");
			System.out.println("---------------------------------------------------------------------" +
					"---------------------------");
			Enumeration<RCFP> e2 = rel.elements();
			while(e2.hasMoreElements()) {
			   System.out.println(e2.nextElement().toString());
			}
		}
		else{
			System.out.println("The case frame  named, " + name + ", was not successfully defined ... ");
		}
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
		
	}
	
	private static void checkNodeRemoved(Node n, String name){
		try{
			Node n1 = Network.getNode(n.getIdentifier());
		}
		catch(Exception e){
			System.out.println("");
			System.out.println("The node named " + name + " was successfully removed from the network");
			System.out.println("");
			System.out.println("...................................................................................." +
					"....................................");
			System.out.println("...................................................................................." +
					"....................................");
			return;
		}
		System.out.println("");
		System.out.println("The node named " + name + " was not successfully removed from the network");
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
	}
	
	private static void printCompactStart(){
		System.out.println("");
		System.out.println("Compacting the storage structure for the nodes after the deletion of some nodes");
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("");
	}
	
	private static void printCompactEnd(){
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
	}
	
	private static void printException(String message){
		System.out.println("");
		System.out.println("An exception was caught ... ");
		System.out.println("\"" + message + "\"");
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
	}
	
	public static void printConflictingCaseframes(LinkedList<CaseFrame> cfList, String name){
		System.out.println("");
		System.out.println("Printing the case frames conflicting with case frame named " + name + "...");
		if(cfList.size() == 0){
			System.out.println("No Conflicting case frames");
			return;
		}
		System.out.println("The number of conflicting case frames: " + cfList.size());
		for(int i = 0; i < cfList.size(); i++){
			cfList.get(i).getId();
			
		}
	}

}