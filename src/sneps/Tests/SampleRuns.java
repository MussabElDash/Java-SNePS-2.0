package sneps.Tests;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.print.attribute.standard.Finishings;

import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Nodes.Node;
import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;
import sneps.Paths.AndPath;
import sneps.Paths.BUnitPath;
import sneps.Paths.FUnitPath;
import sneps.Paths.KStarPath;
import sneps.Paths.OrPath;
import sneps.Paths.Path;
import sneps.SemanticClasses.Entity;
import sneps.SemanticClasses.Individual;
import SNeBR.Context;

public class SampleRuns {
		
		public static void main(String[] args) throws Exception{
			
			// building a new relation
			Relation r1 = Network.defineRelation("member", "Infimum", "none", 1);
			printDefiningRelationResults(r1, "r1");
			
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
			Entity e = new Entity();
			Node b1 = null;
			try{
				 b1 = Network.buildBaseNode("V3", i);
			}
			catch(Exception exp){
				printException(exp.getMessage());
			}
			printBuildingNodeResults(b1, "b1");
			Node b2 = null;
			try{
				 b2 = Network.buildBaseNode("Bird", i);
			}
			catch(Exception exp){
				printException(exp.getMessage());
			}
			printBuildingNodeResults(b2, "b2");
			Node b3 = null;
			try{
				 b3 = Network.buildBaseNode("V6", e);
			}
			catch(Exception exp){
				printException(exp.getMessage());
			}
			printBuildingNodeResults(b3, "b3");
			
			Node x6 = Network.buildVariableNode();
			printBuildingNodeResults(x6, "x6");
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

}
