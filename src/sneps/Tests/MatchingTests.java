package sneps.Tests;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import sneps.CaseFrame;
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
import sneps.match.MatchingSet;
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
		MatchingSet terms=new MatchingSet();
		terms.add(x);
//		//test2 : pass
		sourceList.add(conflictingSSub);
		targetList.add(conflictingTSub);
//		//test3 : pass
//		sourceList2.add(emptySSub2);
	//	targetList2.add(emptyTSub2);  
//		//test4 : pass
		//sourceList2.add(conflictingSSub2);
		//targetList2.add(conflictingTSub2);
//		//test5 : pass
//		sourceList2.add(redundantSSub2);
//		targetList2.add(reduntantTSub2);
        //test 6  f(g(t),g(y)) y-> t  : pass
		//node sets
		NodeSet ns1=new NodeSet();
		ns1.addNode(y);
	
		NodeSet ns2=new NodeSet();
		ns2.addNode(cons);
		;
		//relations
		Relation r1=new Relation("r1", "Entity", "none", 5);
		Relation r2=new Relation("r2", "Entity", "none", 5);
		//down cables
		DownCable dc1=new DownCable(r1, ns1);
		DownCable dc2=new DownCable(r1, ns2);
		//down cable lists
		LinkedList<DownCable> dcl1=new LinkedList<DownCable>();
		LinkedList<DownCable> dcl2=new LinkedList<DownCable>();
		dcl1.add(dc1);
		dcl2.add(dc2);
		//RFCPs
		RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none",5);
		//RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 5);
		
		LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
		LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
		relCF1.add(rp1);
		relCF2.add(rp1);
		//case frames
		CaseFrame cs1=Network.defineCaseFrame("Entity", relCF1);
		//CaseFrame cs2=Network.defineCaseFrame("Entity", relCF2);
		//down cable sets
		DownCableSet dcs1=new DownCableSet(dcl1, cs1);
		DownCableSet dcs2=new DownCableSet(dcl2, cs1);
		
		
		PatternNode g=new PatternNode("Infimum", "f1", dcs1);
		PatternNode g2=new PatternNode("Infimum", "f1", dcs2);
		MatchingSet terms2=new MatchingSet();
		
		terms2.add(y);
		
		terms2.add(cons);
		terms2.add(g);
		terms2.add(g2);
		
		sourceList2.add(emptySSub);
		targetList2.add(emptyTSub);
		//pre test
		//printSubs(sourceList,"Source");		
	    //printSubs(targetList,"Target");
	    printSubs(sourceList2,"Source2");		
	    printSubs(targetList2,"Target2");
	    //test
	   // matcher.VARHERE(x, cons, sourceList, targetList, true, true,terms,1);
		//matcher.VARHERE(x, y, sourceList2, targetList2, true, true,terms,1);
	    //test 6
	
	   // System.out.println(Matcher.sameFunction(g, g2));
	  //  System.out.println(terms2);
	    System.out.println(Matcher.VARHERE(y, cons, sourceList2, targetList2, true, terms2, 4,new MatchingSet(),0));
        //post test
		//printSubs(sourceList,"Source");		
        //printSubs(targetList,"Target");	
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
	//	matcher.setUnify(ns1, ns2, sList, tList, true, true);
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
          //   System.out.println( matcher.violatesUTIRBrute(f1, new LinearSubstitutions()));
             //test 2 : pass
             Substitutions conflictingSub=new LinearSubstitutions();
             conflictingSub.putIn(new Binding(x,cons));
            // System.out.println(matcher.violatesUTIRBrute(f1,conflictingSub));
             //test 3 : pass
             ns2.addNode(f1);
            // System.out.println( matcher.violatesUTIRBrute(f2, new LinearSubstitutions()));
		     //test 4 : pass
             Substitutions conflictingSub2=new LinearSubstitutions();
             conflictingSub2.putIn(new Binding(q,cons));
            // System.out.println(matcher.violatesUTIRBrute(f2, conflictingSub2));
             //test 5 : pass
             ns2.addNode(cons);
            // System.out.println( matcher.violatesUTIRBrute(f2, new LinearSubstitutions()));
             //test 6 : pass
             Substitutions conflictingSub3=new LinearSubstitutions();
             conflictingSub3.putIn(new Binding(z,f1));
             //System.out.println(matcher.violatesUTIRBrute(f2, conflictingSub3));
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
        //test 1 : pass f1(x,y) x->c3 y->c4
        //System.out.println(matcher.vere(q, sourceR, targetR, sourceS, targetS));
        //test 2 : pass  q->f1(x) x->q
        targetR.update(targetR.getBindingByVariable(x), q);
        System.out.println(Matcher.vere(q, sourceR, targetR, sourceS, targetS));
	}
	public static void testHere() throws Exception{
		
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
				MatchingSet terms=new MatchingSet();
				terms.add(x);
//				//test 
//				sourceList.add(conflictingSSub);
//				targetList.add(conflictingTSub);
//				//test 
//				sourceList2.add(emptySSub2);
			//	targetList2.add(emptyTSub2);  
//				//test 
				//sourceList2.add(conflictingSSub2);
				//targetList2.add(conflictingTSub2);
//				//test 
//				sourceList2.add(redundantSSub2);
//				targetList2.add(reduntantTSub2);
		        //test 4  f(g(t),g(y)) y-> t  : pass
				//node sets
				NodeSet ns1=new NodeSet();
				ns1.addNode(y);
			
				NodeSet ns2=new NodeSet();
				ns2.addNode(cons);
				;
				NodeSet h1ns=new NodeSet();
				NodeSet h2ns=new NodeSet();
				NodeSet h3ns=new NodeSet();
				NodeSet h4ns=new NodeSet();
				//relations
				Relation r1=new Relation("r1", "Entity", "reduce", 5);
				Relation r2=new Relation("r2", "Entity", "reduce", 5);
				//down cables
				DownCable dc1=new DownCable(r1, ns1);
				DownCable dc2=new DownCable(r1, ns2);
				DownCable h1dc=new DownCable(r2,h1ns);
				DownCable h2dc=new DownCable(r2,h2ns);
				DownCable h3dc=new DownCable(r2,h3ns);
				DownCable h4dc=new DownCable(r1,h1ns);
				//down cable lists
				LinkedList<DownCable> dcl1=new LinkedList<DownCable>();
				LinkedList<DownCable> dcl2=new LinkedList<DownCable>();
				LinkedList<DownCable> h1dcl=new LinkedList<DownCable>();
				LinkedList<DownCable> h2dcl=new LinkedList<DownCable>();
				LinkedList<DownCable> h3dcl=new LinkedList<DownCable>();
				LinkedList<DownCable> h4dcl=new LinkedList<DownCable>();
				dcl1.add(dc1);
				dcl2.add(dc2);
				h1dcl.add(h1dc);
				h2dcl.add(h2dc);
				h3dcl.add(h3dc);
				h4dcl.add(h4dc);
				//RFCPs
				RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "reduce",1);
				RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "reduce", 1);
//				
				
				LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
				LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
				LinkedList<RCFP> HrelCF = new LinkedList<RCFP>();
				relCF1.add(rp1);
				relCF2.add(rp1);
				HrelCF.add(rp2);
//				//case frames
				CaseFrame cs1=Network.defineCaseFrame("Entity", relCF1);
				CaseFrame cs2=Network.defineCaseFrame("Entity", HrelCF);
				
//				//down cable sets
				DownCableSet dcs1=new DownCableSet(dcl1, cs1);
				DownCableSet dcs2=new DownCableSet(dcl2, cs1);
				DownCableSet h1dcs=new DownCableSet(h1dcl, cs2);
                DownCableSet h2dcs=new DownCableSet(h2dcl, cs2);
                DownCableSet h3dcs=new DownCableSet(h3dcl, cs2);
                DownCableSet h4dcs=new DownCableSet(h4dcl, cs1);
//				
				PatternNode g=new PatternNode("Infimum", "f1", dcs1);
				PatternNode g2=new PatternNode("Infimum", "f3", dcs2);
				PatternNode h1=new PatternNode("Infimum", "h1", h1dcs);
				PatternNode h2=new PatternNode("Infimum", "h2", h2dcs);
				PatternNode h3=new PatternNode("Infimum", "h3", h3dcs);
				PatternNode h4=new PatternNode("Infimum", "h4", h4dcs);
				h1ns.addNode(g);
				h1ns.addNode(g2);
				h2ns.addNode(g2);
				MatchingSet Sterms=new MatchingSet();
			    MatchingSet Tterms=new MatchingSet();
			    Sterms.add(y);
			    Sterms.add(g);
			    Sterms.add(h1);
			   
			    Tterms.add(cons);
			    Tterms.add(g2);
			    Tterms.add(h2);
			   // h2ns.addNode(cons);
//				terms2.add(g2);
//			System.out.println(g);
//			System.out.println(g2);
				sourceList2.add(emptySSub);
				targetList2.add(emptyTSub);
//				//pre test
//				printSubs(sourceList,"Source");		
//			    printSubs(targetList,"Target");
			    printSubs(sourceList2,"Source2");		
			    printSubs(targetList2,"Target2");
			    //test 1 : pass
			  //  matcher.hERE(x, cons, sourceList, targetList, true, true,terms,1);
			    //test 2 : pass
			   // terms.remove(x);
			    //terms.add(cons);
			  //  matcher.hERE(cons, x, sourceList, targetList, true, true,terms,1);
			    //test 3 ; pass
			  //matcher.hERE(cons, cons2, sourceList, targetList, true, true,terms,1);
			  //test 4 : pass
		//matcher.hERE(g, g2, sourceList2, targetList2, true, true, terms2, 2);
			//test 5 : pass
			  
			    //System.out.println(h1);
			    //System.out.println(h2);
			    //pass h1(f1(y)) / h2(f3(c)) y->c
			  //  Matcher.hERE(h1, h2, sourceList2, targetList2, true,Sterms, 3,Tterms,3);
			    
			 
				//test 6
			   // System.out.println(Matcher.sameFunction(g, g2));
			  //  System.out.println(terms2);
//			    System.out.println(matcher.hERE(y, cons, sourceList2, targetList2, true, true, terms2, 4));
		        //post test
//				printSubs(sourceList,"Source");		
//		        printSubs(targetList,"Target");	
		        printSubs(sourceList2,"Source2");		
			    printSubs(targetList2,"Target2");
			
			    
			    h3ns.addNode(cons);
			    h1ns.addNode(y);
			    ns2.removeNode(cons);
			    ns2.addNode(cons2);
			  //  h2ns.addNode(h3);
			    h2ns.addNode(cons2);
			    
			    Sterms=new MatchingSet();
			    Tterms=new MatchingSet();
			    Sterms.add(h1);
			    Sterms.add(h1ns);
			    Sterms.add(y);
			    Sterms.add(cons2);
			   // Sterms.add(cons);
			    h2ns.addNode(x);
			    Tterms.add(h2);
			    Tterms.add(h2ns);
			    Tterms.add(x);
			    //Tterms.remove(cons);
			    System.out.println(h1);
			    System.out.println(h2);
			  //  System.out.println(h3);
			    System.out.println(Sterms);
                                            
			    System.out.println(Tterms);
			    LinkedList<Substitutions> sl3=new LinkedList<Substitutions>();
			    LinearSubstitutions es=new LinearSubstitutions();
			    sl3.add(es);
			    printSubs(sl3,"Source");
			    Matcher.hERE(h1, h2, sl3, targetList, true,Sterms, Sterms.size(),Tterms,Tterms.size());
			    
			    printSubs(sl3,"Source2");		
			    printSubs(targetList,"Target2");
		
	}
	public static void testMatch(){
		
	}
	public static void testVioaltesUTIRorOccurs() throws Exception{
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
				//ns1.addNode(y);
				//ns1.addNode(cons);
				//ns1.addNode(cons2);
				NodeSet ns2=new NodeSet();
				ns2.addNode(z);
				//ns2.addNode(q);
				//ns2.addNode(cons3);
				//ns2.addNode(cons4);
				NodeSet ns3=new NodeSet();
				ns3.addNode(cons);
				//relations
				Relation r1=new Relation("r1", "Entity", "none", 5);
				Relation r2=new Relation("r2", "Entity", "none", 5);
				
				//down cables
				DownCable dc1=new DownCable(r1, ns1);
				DownCable dc2=new DownCable(r2, ns2);
				DownCable dc3=new DownCable(r2, ns3);
				//down cable lists
				LinkedList<DownCable> dcl1=new LinkedList<DownCable>();
				LinkedList<DownCable> dcl2=new LinkedList<DownCable>();
				LinkedList<DownCable> dcl3=new LinkedList<DownCable>();
				dcl1.add(dc1);
				dcl2.add(dc2);
				dcl3.add(dc3);
				//RFCPs
				RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none",5);
				RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 5);
				RCFP rp3 = Network.defineRelationPropertiesForCF(r2, "none", 5);
				LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
				LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
				LinkedList<RCFP> relCF3 = new LinkedList<RCFP>();
				relCF1.add(rp1);
				relCF2.add(rp2);
				relCF3.add(rp3);
				//case frames
				CaseFrame cs1=Network.defineCaseFrame("Entity", relCF1);
				CaseFrame cs2=Network.defineCaseFrame("Entity", relCF2);
				//CaseFrame cs2=Network.defineCaseFrame("Entity", relCF2);
				
				//down cable sets
				DownCableSet dcs1=new DownCableSet(dcl1, cs1);
				DownCableSet dcs2=new DownCableSet(dcl2, cs2);
				DownCableSet dcs3=new DownCableSet(dcl3, cs2);
				
				//molecular nodes
				PatternNode f1=new PatternNode("Infimum", "f1", dcs1);
				
				PatternNode f2=new PatternNode("Infimum","f2",dcs2);
			    
				PatternNode f3=new PatternNode("Infimum","f2",dcs3);
				//test 1 : pass
				ns1.addNode(f2);//f1(x,f2(z))
				Substitutions vutirSub=new LinearSubstitutions();
				vutirSub.putIn(new Binding(z,cons));
				
				MatchingSet terms=new MatchingSet();
				terms.add( x);
				terms.add( z);
				terms.add(f2);
               Matcher matcher=new Matcher();
              System.out.println(Matcher.violatesUTIRorOccursCheck(terms, vutirSub, new LinearSubstitutions(), 3));
	          //test 2 : pass 
              vutirSub.putIn(new Binding(x,f3));
              //System.out.println(f2);
              //System.out.println(vutirSub);
             // System.out.println(matcher.termVere(f2, new LinearSubstitutions(),vutirSub,  new LinearSubstitutions(), new LinearSubstitutions()));
            // System.out.println(matcher.violatesUTIRorOccursCheck(terms, vutirSub, new LinearSubstitutions(), 3));
	          
	}
	
	
	public static void main(String[] args) throws Exception {
		//testVarhere();// pass
		//testSetUnify(); fail
		//testViolatesUTIR(); //pass 
	//	testVere(); //pass
		//testVioaltesUTIRorOccurs(); pass
		testHere();
	}

}
