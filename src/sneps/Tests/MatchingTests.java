package sneps.Tests;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.CustomException;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;
import sneps.match.Binding;
import sneps.match.LinearSubstitutions;
import sneps.match.Matcher;
import sneps.match.Substitutions;
public class MatchingTests {
	
	
	public static void testVarhere() throws Exception{
		//Nodes
		VariableNode x=new VariableNode("Entity", "x");
		VariableNode y=new VariableNode("Entity", "y");
		Node cons = new Node("Base", "Entity","constant");
		Node cons2 = new Node("Base", "Entity","constant2");
		Matcher matcher=new Matcher();
		//sub lists
		LinkedList<Substitutions> sourceList=new LinkedList<Substitutions>();
		LinkedList<Substitutions> targetList=new LinkedList<Substitutions>();
		
		LinkedList<Substitutions> sourceList2=new LinkedList<Substitutions>();
		LinkedList<Substitutions> targetList2=new LinkedList<Substitutions>();
		//subs
		Substitutions emptySSub=new LinearSubstitutions();
		Substitutions emptyTSub=new LinearSubstitutions();
		
		Substitutions emptySSub2=new LinearSubstitutions();
		Substitutions emptyTSub2=new LinearSubstitutions();
		
		Substitutions conflictingSSub=new LinearSubstitutions();
		Substitutions conflictingTSub=new LinearSubstitutions();
		
		Substitutions conflictingSSub2=new LinearSubstitutions();
		Substitutions conflictingTSub2=new LinearSubstitutions();
		
		Substitutions redundantSSub2=new LinearSubstitutions();
		Substitutions reduntantTSub2=new LinearSubstitutions();
		
		
		conflictingSSub.putIn(new Binding(x,cons2));
		
		conflictingTSub2.putIn(new Binding(y,cons2));
		
		redundantSSub2.putIn(new Binding(x,y));
		reduntantTSub2.putIn(new Binding(y,x));
		
		//test1 : pass
		sourceList.add(emptySSub);
		targetList.add(emptyTSub);
		//test2 : pass
		sourceList.add(conflictingSSub);
		targetList.add(conflictingTSub);
		//test3 : pass
		sourceList2.add(emptySSub2);
		targetList2.add(emptyTSub2);  
		//test4 : pass
		sourceList2.add(conflictingSSub2);
		targetList2.add(conflictingTSub2);
		//test5 : pass
		sourceList2.add(redundantSSub2);
		targetList2.add(reduntantTSub2);
		
		//pre test
		printSubs(sourceList,"Source");		
	    printSubs(targetList,"Target");
	    printSubs(sourceList2,"Source2");		
	    printSubs(targetList2,"Target2");
	    //test
	    matcher.VARHERE(x, cons, sourceList, targetList, true, true);
		matcher.VARHERE(x, y, sourceList2, targetList2, true, true);
        //post test
		printSubs(sourceList,"Source");		
        printSubs(targetList,"Target");	
        printSubs(sourceList2,"Source2");		
	    printSubs(targetList2,"Target2");
	}
	
	
	public static void printSubs(LinkedList<Substitutions> subs,String source){
		for (int i = 0; i < subs.size(); i++) {
			System.out.println(source+ " Match "+i);
			Substitutions sub=subs.get(i);
			if(sub.cardinality()==0)
				System.out.println("Empty");
			for (int j = 0; j < sub.cardinality(); j++) {
				System.out.println(sub.getBinding(j).getVariable().getIdentifier()+"->"+sub.getBinding(j).getNode().getIdentifier());
			}
		}
	}
	
	
	public static void testSetUnify() throws Exception{
		//variable nodes
		VariableNode x=new VariableNode("Entity", "x");
		VariableNode y=new VariableNode("Entity", "y");
		VariableNode z=new VariableNode("Entity", "z");
		VariableNode q=new VariableNode("Entity", "q");
		
		//base nodes
		Node cons = new Node("Base", "Entity","constant");
		Node cons2 = new Node("Base", "Entity","constant2");
		Node cons3 = new Node("Base", "Entity","constant3");
		Node cons4 = new Node("Base", "Entity","constant4");
		
		//node sets
		NodeSet ns1=new NodeSet();
		ns1.addNode(x);
		ns1.addNode(y);
		ns1.addNode(cons);
		ns1.addNode(cons2);
		NodeSet ns2=new NodeSet();
		ns2.addNode(z);
		ns2.addNode(q);
		ns2.addNode(cons3);
		ns2.addNode(cons4);
		//sublists
		LinkedList<Substitutions> sList=new LinkedList<Substitutions>();
		LinkedList<Substitutions> tList=new LinkedList<Substitutions>();
		Substitutions sSub=new LinearSubstitutions();
		Substitutions tSub=new LinearSubstitutions();
		sList.add(sSub);
		tList.add(tSub);
		//matching
		Matcher matcher = new Matcher();
		matcher.setUnify(ns1, ns2, sList, tList, true, true);
		//post test
		printSubs(sList, "Source");
		printSubs(tList, "Target");
	}
	
	public static void testViolatesUTIR() throws Exception{
		
		//variable nodes
				VariableNode x=new VariableNode("Entity", "x");
				VariableNode y=new VariableNode("Entity", "y");
				VariableNode z=new VariableNode("Entity", "z");
				VariableNode q=new VariableNode("Entity", "q");
				
				//base nodes
				Node cons = new Node("Base", "Entity","constant");
				Node cons2 = new Node("Base", "Entity","constant2");
				Node cons3 = new Node("Base", "Entity","constant3");
				Node cons4 = new Node("Base", "Entity","constant4");
				
				//node sets
				NodeSet ns1=new NodeSet();
				ns1.addNode(x);
				ns1.addNode(y);
				ns1.addNode(cons);
				ns1.addNode(cons2);
				NodeSet ns2=new NodeSet();
				ns2.addNode(z);
				ns2.addNode(q);
				ns2.addNode(cons3);
				ns2.addNode(cons4);
				//relations
				Relation r1=new Relation("r1", "Entity", "none", 5);
				Relation r2=new Relation("r2", "Entity", "none", 5);
				//down cables
				DownCable dc1=new DownCable(r1, ns1);
				DownCable dc2=new DownCable(r2, ns2);
				//down cable lists
				LinkedList<DownCable> dcl1=new LinkedList<DownCable>();
				LinkedList<DownCable> dcl2=new LinkedList<DownCable>();
				dcl1.add(dc1);
				dcl2.add(dc2);
				//RFCPs
				RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none",5);
				RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 5);
				
				LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
				LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
				relCF1.add(rp1);
				relCF2.add(rp2);
				//case frames
				CaseFrame cs1=Network.defineCaseFrame("Entity", relCF1);
				CaseFrame cs2=Network.defineCaseFrame("Entity", relCF2);
				//down cable sets
				DownCableSet dcs1=new DownCableSet(dcl1, cs1);
				DownCableSet dcs2=new DownCableSet(dcl2, cs2);
				
				//test 1 node
				PatternNode f1=new PatternNode("Infimum", "f1", dcs1);
				//test 3 node
				PatternNode f2=new PatternNode("Infimum","f2",dcs2);
			           	
               Matcher matcher=new Matcher();
             //test 1 : pass
             System.out.println( matcher.violatesUTIR(f1, new LinearSubstitutions()));
             //test 2 : pass
             Substitutions conflictingSub=new LinearSubstitutions();
             conflictingSub.putIn(new Binding(x,cons));
             System.out.println(matcher.violatesUTIR(f1,conflictingSub));
             //test 3 : pass
             ns2.addNode(f1);
             System.out.println( matcher.violatesUTIR(f2, new LinearSubstitutions()));
		     //test 4 : pass
             Substitutions conflictingSub2=new LinearSubstitutions();
             conflictingSub2.putIn(new Binding(q,cons));
             System.out.println(matcher.violatesUTIR(f2, conflictingSub2));
             //test 5 : pass
             ns2.addNode(cons);
             System.out.println( matcher.violatesUTIR(f2, new LinearSubstitutions()));
             //test 6 : pass
             Substitutions conflictingSub3=new LinearSubstitutions();
             conflictingSub3.putIn(new Binding(z,f1));
             System.out.println(matcher.violatesUTIR(f2, conflictingSub3));
	}
	public static void testVere() throws Exception{
		//variable nodes
		VariableNode x=new VariableNode("Entity", "x");
		VariableNode y=new VariableNode("Entity", "y");
		VariableNode z=new VariableNode("Entity", "z");
		VariableNode q=new VariableNode("Entity", "q");
		
		//base nodes
		Node cons = new Node("Base", "Entity","constant");
		Node cons2 = new Node("Base", "Entity","constant2");
		Node cons3 = new Node("Base", "Entity","constant3");
		Node cons4 = new Node("Base", "Entity","constant4");
		
		//node sets
		NodeSet ns1=new NodeSet();
		ns1.addNode(x);
		ns1.addNode(y);
		ns1.addNode(cons);
		ns1.addNode(cons2);
		NodeSet ns2=new NodeSet();
		ns2.addNode(z);
		ns2.addNode(q);
		ns2.addNode(cons3);
		ns2.addNode(cons4);
		//relations
		Relation r1=new Relation("r1", "Entity", "none", 5);
		Relation r2=new Relation("r2", "Entity", "none", 5);
		//down cables
		DownCable dc1=new DownCable(r1, ns1);
		DownCable dc2=new DownCable(r2, ns2);
		//down cable lists
		LinkedList<DownCable> dcl1=new LinkedList<DownCable>();
		LinkedList<DownCable> dcl2=new LinkedList<DownCable>();
		dcl1.add(dc1);
		dcl2.add(dc2);
		//RFCPs
		RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none",5);
		RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 5);
		
		LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
		LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
		relCF1.add(rp1);
		relCF2.add(rp2);
		//case frames
		CaseFrame cs1=Network.defineCaseFrame("Entity", relCF1);
		CaseFrame cs2=Network.defineCaseFrame("Entity", relCF2);
		//down cable sets
		DownCableSet dcs1=new DownCableSet(dcl1, cs1);
		DownCableSet dcs2=new DownCableSet(dcl2, cs2);
		
		//test 1 node
		PatternNode f1=new PatternNode("Infimum", "f1", dcs1);
		
		
		//subs
		Substitutions sourceR=new LinearSubstitutions();
		Substitutions sourceS=new LinearSubstitutions();
		Substitutions targetR=new LinearSubstitutions();
		Substitutions targetS=new LinearSubstitutions();
		//bindings
		sourceR.putIn(new Binding(q,f1));
		targetR.putIn(new Binding(x,cons3));
        targetR.putIn(new Binding(y,cons4));
        
        Matcher matcher = new Matcher();
        //test 1 : pass
        System.out.println(matcher.vere(q, sourceR, targetR, sourceS, targetS));
        //test 2 : fail
        targetR.update(targetR.getBindingByVariable(x), q);
        System.out.println(matcher.vere(q, sourceR, targetR, sourceS, targetS));
	}
	public static void testHere(){
		
	}
	public static void testMatch(){
		
	}
	
	
	public static void main(String[] args) throws Exception {
		//testVarhere(); pass partial
		//testSetUnify(); fail
		testViolatesUTIR(); //pass partial
		//testVere(); pass partial
	}

}
