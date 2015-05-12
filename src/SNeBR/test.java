package SNeBR;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Entity;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;
import sneps.SemanticClasses.*;
import sneps.SyntaticClasses.Pattern;
public class test {
	
	public static void main(String[] args){
		
		Entity e = new Entity();
		
		Node x1 = Network.buildVariableNode();
		Node x2 = Network.buildVariableNode();
		Node x3 = Network.buildVariableNode();
		
		Node b1 = null;
		try{
			b1 = Network.buildBaseNode("0", e);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		
		Relation arg = null;
		try{
		arg = Network.defineRelation("arg", "Entity", "none", 1);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		Relation rel2 = null;
		try{
		rel2 = Network.defineRelation("rel2", "Entity", "none", 1);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		System.out.println(arg.isQuantifier());
		Relation min = null;
		try{
			min = Network.defineRelation("min", "Entity", "none", 1);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		
		Relation max = null;
		try{
		max = Network.defineRelation("max", "Entity", "none", 1);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		
		Relation rel = null;
		try{
		rel = Network.defineRelation("rel", "Entity", "none", 1);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		
		RCFP p1 = Network.defineRelationPropertiesForCF(arg, "none", 1);
		RCFP p2 = Network.defineRelationPropertiesForCF(min, "none", 1);
		RCFP p3 = Network.defineRelationPropertiesForCF(max, "none", 1);
		RCFP p4 = Network.defineRelationPropertiesForCF(rel, "none", 1);
		RCFP p5 = Network.defineRelationPropertiesForCF(rel2, "none", 1);
		LinkedList<RCFP> plist2 = new LinkedList<RCFP>();
		plist2.add(p5);
		LinkedList<RCFP> plist = new LinkedList<RCFP>();
		plist.add(p1);
		plist.add(p2);
		plist.add(p3);
		
		LinkedList<RCFP> plist1 = new LinkedList<RCFP>();
		plist1.add(p4);
		
		CaseFrame cf = null;
		try{
		cf = Network.defineCaseFrame("Entity", plist);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		
		CaseFrame cf1 = null;
		try{
		cf1 = Network.defineCaseFrame("Entity", plist1);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		
		CaseFrame cf2 = null;
		try{
		cf2 = Network.defineCaseFrame("Proposition", plist2);
		} catch(Exception exp){
			System.out.println(exp.getMessage());
		}
		NodeSet tst = new NodeSet();
		tst.addNode(x2);
		tst.addNode(x3);
		DownCable dc22=new DownCable(rel2,tst);
		LinkedList<DownCable> dCables2 = new LinkedList<DownCable>();
		dCables2.add(dc22);
		DownCableSet dcss=new DownCableSet(dCables2,cf2);
		Pattern bat = new Pattern("m3",dcss);
		NodeSet ns = new NodeSet();
		ns.addNode(x1);
		DownCable dc = new DownCable(rel, ns);
		LinkedList<DownCable> dCables = new LinkedList<DownCable>();
		dCables.add(dc);
		DownCableSet dcs = new DownCableSet(dCables, cf1);
		

		Pattern pat = new Pattern("m1", dcs);
		Proposition prop = new Proposition();
		PropositionNode m1 = new PropositionNode(pat, prop);
		
		NodeSet ns1 = new NodeSet();
		ns1.addNode(b1);
		DownCable dc1 = new DownCable(min, ns1);
		DownCable dc2 = new DownCable(max, ns1);
		NodeSet ns2 = new NodeSet();
		ns2.addNode(m1);
		DownCable dc3 = new DownCable(arg, ns2);
		LinkedList<DownCable> dCables1 = new LinkedList<DownCable>();
		dCables1.add(dc1);
		dCables1.add(dc2);
		dCables1.add(dc3);
		DownCableSet dcs1 = new DownCableSet(dCables1, cf);
		
		
		
		Pattern pat1 = new Pattern("m2", dcs1);
		Proposition prop1 = new Proposition();
		PropositionNode m2 = new PropositionNode(pat1, prop1);
		Proposition pr1 = new Proposition();
		PropositionNode m3 = new PropositionNode(bat, pr1);
		Context c = new Context(m3);
		Support s =new Support(c);
		((Proposition)m1.getSemantic()).getOriginSupport().add(s);
		//SNeBR.assertProposition(m1, "a");
		//SNeBR.assertProposition(m2, "a");
		Context a =SNeBR.getContextSet().getContext("a");
		//Iterator<Contradiction> iterator = a.contradictions.iterator();
		PropositionSet x=new PropositionSet();
		x.propositions.add(m2);
		x.propositions.add(m1);
		HashSet<Support>s2=prop1.calcOrigin(x);
		Iterator<Support> iterator2=s2.iterator();
		//SNeBR.propositionsToBeDiscarded(x, "", a, iterator.next());
		//Context r=SNeBR.contextSet.getContext("a");
		System.out.println(iterator2.next().originSet.hypothesisSet.propositions.contains(m1));
		System.out.println(iterator2.next().originSet.hypothesisSet.propositions.contains(m3));
		
	}

}
