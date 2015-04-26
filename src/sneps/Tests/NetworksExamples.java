package sneps.Tests;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Nodes.Node;
import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;
import sneps.SemanticClasses.Entity;
import sneps.SemanticClasses.Individual;
import sneps.SyntaticClasses.Molecular;

public class NetworksExamples {
	
	public static void main(String[] main) throws Exception{
		// Defining Relations
		
				Relation r1 = Network.defineRelation("agent", "Individual", "none", 1);
				printDefiningRelationResults(r1, "r1");
				
				Relation r2 = Network.defineRelation("act", "Entity", "none", 1);
				printDefiningRelationResults(r2, "r2");
				
				Relation r3 = Network.defineRelation("object", "Entity", "none", 1);
				printDefiningRelationResults(r3, "r3");
				
				Relation r4 = Network.defineRelation("property", "Entity", "none", 1);
				printDefiningRelationResults(r4, "r4");
				
				// creating RCFP Lists
				RCFP rel1 = Network.defineRelationPropertiesForCF(r1, "none", 1);
				RCFP rel2 = Network.defineRelationPropertiesForCF(r2, "none", 1);
				RCFP rel3 = Network.defineRelationPropertiesForCF(r3, "none", 1);
				RCFP rel4 = Network.defineRelationPropertiesForCF(r4, "none", 1);
				
				LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
				relCF1.add(rel1);
				relCF1.add(rel2);
				relCF1.add(rel3);
				
				LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
				relCF2.add(rel3);
				relCF2.add(rel4);
				
				// Defining case frames
				CaseFrame agentAct = Network.defineCaseFrame("Entity", relCF1);
				printDefiningCaseFrameResults(agentAct, "agentAct");
				
				CaseFrame objectProperty = Network.defineCaseFrame("Entity", relCF2);
				printDefiningCaseFrameResults(objectProperty, "objectProperty");
				
				// building base nodes
				
				
				Individual i = new Individual();
				Node bob = null;
				try{
					 bob = Network.buildBaseNode("Bob", i);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(bob, "bob");
				
				
				Individual i2 = new Individual();
				Node mary = null;
				try{
					 mary = Network.buildBaseNode("Mary", i2);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(mary, "mary");
				
				
				Entity e = new Entity();
				Node believe = null;
				try{
					 believe = Network.buildBaseNode("believe", e);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(believe, "believe");
				
				
				Entity e2 = new Entity();
				Node know = null;
				try{
					 know = Network.buildBaseNode("know", e2);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(know, "know");
				
				
				Entity e3 = new Entity();
				Node csience = null;
				try{
					csience = Network.buildBaseNode("Computer Science", e3);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(csience, "csience");
				
				
				Entity e4 = new Entity();
				Node interesting = null;
				try{
					interesting = Network.buildBaseNode("interesting", e3);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(interesting, "interesting");
				
				// Building molecular nodes
				
				// build molecular node M1
				Object[][] a1 = new Object[2][2];
				a1[0][0] = r3;
				a1[0][1] = csience;
				a1[1][0] = r4;
				a1[1][1] = interesting;
				
				Node m1 = null;
				try{
					m1 = Network.buildMolecularNode(a1, objectProperty);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(m1, "m1");
				
				// build molecular node M2
				Object[][] a2 = new Object[3][2];
				a2[0][0] = r1;
				a2[0][1] = mary;
				a2[1][0] = r2;
				a2[1][1] = believe;
				a2[2][0] = r3;
				a2[2][1] = m1;
				
				Node m2 = null;
				try{
					m2 = Network.buildMolecularNode(a2, agentAct);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(m2, "m2");
				
				// build molecular node M3
				Object[][] a3 = new Object[3][2];
				a3[0][0] = r1;
				a3[0][1] = bob;
				a3[1][0] = r2;
				a3[1][1] = know;
				a3[2][0] = r3;
				a3[2][1] = m2;
				
				Node m3 = null;
				try{
					m3 = Network.buildMolecularNode(a3, agentAct);
				}
				catch(Exception exp){
					printException(exp.getMessage());
				}
				printBuildingNodeResults(m3, "m3");
				
				printNetwork(m3);
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
				LinkedList<VariableNode> freeVars =  ((PatternNode)n).getFreeVariables();
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
	
	private static void printFollowResults(LinkedList<Object[]> list, Node node){
		System.out.println("");
		System.out.println("starting follow at node " + node.getIdentifier() + " ....");
		if(list != null ){
			if(list.size() > 0){
				System.out.println("");
				System.out.println("follow list contains:");
				System.out.println("---------------------");
				for(int i = 0; i < list.size(); i++){
					System.out.println(((Node)list.get(i)[0]).getIdentifier());
//					System.out.println(" - PathTrace: " + ((PathTrace)list.get(i)[1]).getSupports());
				}
			} else{
				System.out.println("follow is empty");
			}
		}
		else {
			System.out.println("follow list is null");
		}
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
	}
	
	public static void printNetwork(Node n){
		System.out.println("");
		System.out.println("Printing the toString of the network:");
		System.out.println("--------------------------------------");
		System.out.println("");
		System.out.println(n.getIdentifier() + ": (" + ((Molecular)n.getSyntactic()).getDownCableSet().toString() + ")");
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
	}

}
