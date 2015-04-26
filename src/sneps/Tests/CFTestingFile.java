package sneps.Tests;

import java.util.Hashtable;
import java.util.LinkedList;

import org.w3c.dom.css.Counter;

import sneps.CFSignature;
import sneps.CableTypeConstraint;
import sneps.CaseFrame;
import sneps.RCFP;
import sneps.Relation;
import sneps.SubDomainConstraint;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;

public class CFTestingFile {
	
	
	public LinkedList<CaseFrame> checkCaseFrameConflicting(LinkedList<RCFP> relationSet){
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static String getCFSignature(Hashtable<String, NodeSet> relNodeSet, CaseFrame caseframe){
		LinkedList<String> signatureIds =  caseframe.getSignatureIDs();
		Hashtable<String, CFSignature>	signatures = caseframe.getSignatures();
		for(int i = 0; i < signatureIds.size(); i++){
			String currentId = signatureIds.get(i);
			if(signatures.containsKey(currentId)){
				LinkedList<SubDomainConstraint> rules = (LinkedList<SubDomainConstraint>) 
						signatures.get(currentId).getSubDomainConstraints().clone();
				for(int j = 0; j < rules.size(); j++){
					SubDomainConstraint c = rules.get(j);
					LinkedList<CableTypeConstraint> checks = (LinkedList<CableTypeConstraint>) 
							c.getNodeChecks().clone();
					NodeSet ns = relNodeSet.get(c.getRelation());
					for(int k = 0; k < checks.size(); k++){
						CableTypeConstraint check = checks.get(k);
						int counter = 0;
						for(int l = 0; l < ns.size(); l++){
							if(ns.getNode(l).getSemanticType().equals(check.getSemanticType()) || 
									ns.getNode(l).getSemanticSuperClass().equals(check.getSemanticType())){
								counter ++;
								System.out.println(check.getSemanticType() + counter);
							}	
						}
						System.out.println(check.getSemanticType() + " " + counter);
						if(check.getLowerLimit() == null && check.getUpperLimit() == null){
							if(counter == ns.size()){
								checks.remove(k);
								k--;
							}
						} else {
							if(check.getUpperLimit() == null){
								if (counter >= check.getLowerLimit()){
									checks.remove(k);
									k--;
								}
							} else {
								if(counter >= check.getLowerLimit() && counter <= check.getUpperLimit()){
									checks.remove(k);
									k--;
								}
							}
						}
					}
					if(checks.isEmpty()){
						System.out.println("empty checks");
						rules.remove(j);
						j--;
					} else {
						break;
					}
				}
				if(rules.isEmpty()){
					System.out.println("Satisfied");
					return signatures.get(currentId).getResultingType();
				}
			}
		}
		System.out.println("Not Satisfied");
		return caseframe.getSemanticClass();
	}
	
	public static void main(String[] args) throws Exception{
		Relation add = new Relation("+", "Entity", "none", 1);
		RCFP prop = new RCFP(add, "none", 1);
		LinkedList<RCFP> props = new LinkedList<RCFP>();
		CaseFrame cf = new CaseFrame("Entity", props);
		CableTypeConstraint c = new CableTypeConstraint("Individual", null, null);
		CableTypeConstraint c2 = new CableTypeConstraint("Entity", 1, 3);
		LinkedList<CableTypeConstraint> cList = new LinkedList<CableTypeConstraint>();
		cList.add(c);
		cList.add(c2);
		SubDomainConstraint sdc = new SubDomainConstraint("+", cList);
		LinkedList<SubDomainConstraint> rules = new LinkedList<SubDomainConstraint>();
		rules.add(sdc);
		CFSignature cfSig = new CFSignature("Individual", rules, cf.getId());
		cf.addSignature(cfSig, 0);
		CableTypeConstraint c3 = new CableTypeConstraint("Individual", 1, 3);
		CableTypeConstraint c4 = new CableTypeConstraint("Entity", 1, 3);
		LinkedList<CableTypeConstraint> cList1 = new LinkedList<CableTypeConstraint>();
		cList1.add(c3);
		cList1.add(c4);
		SubDomainConstraint sdc1 = new SubDomainConstraint("+", cList1);
		LinkedList<SubDomainConstraint> rules1 = new LinkedList<SubDomainConstraint>();
		rules1.add(sdc1);
		CFSignature cfSig1 = new CFSignature("HOPPAAA", rules1, cf.getId());
		cf.addSignature(cfSig1, 1);
		Node node1 = new Node("Base", "Individual", "1");
		Node node2 = new Node("Base", "Individual", "1.5");
		Node node3 = new Node("Base", "Individual", "2");
		NodeSet ns = new NodeSet();
		ns.addNode(node1);
		ns.addNode(node2);
		ns.addNode(node3);
		Hashtable<String, NodeSet> relNodeSet = new Hashtable<String, NodeSet>();
		relNodeSet.put(add.getName(), ns);
		System.out.println("Resulting type: " + getCFSignature(relNodeSet, cf));
	}
	
	
//	public static String getCFSignature(Hashtable<String, NodeSet> relNodeSet, CaseFrame caseframe){
//		LinkedList<CFSignature> signatures = caseframe.getRules();
//		for (int i = 0; i < signatures.size(); i++){
//			LinkedList<SubDomainConstraint> rules = signatures.get(i).getSubDomainConstraints();
//			for(int j = 0; j < rules.size(); j++){
//				SubDomainConstraint c = rules.get(j);
//				LinkedList<CableTypeConstraint> checks = c.getNodeChecks();
//				NodeSet ns = relNodeSet.get(c.getRelation());
//				for(int k = 0; k < checks.size(); k++){
//					CableTypeConstraint check = checks.get(k);
//					int counter = 0;
//					for(int l = 0; l < ns.size(); l++){
//						if(ns.getNode(l).getSemanticType().equals(check.getSemanticType())){
//							counter ++;
//							System.out.println(check.getSemanticType() + counter);
//						}	
//					}
//					System.out.println(check.getSemanticType() + " " + counter);
//					if(counter >= check.getLowerLimit() && counter <= check.getUpperLimit()){
//						checks.remove(k);
//						k--;
//					}
//				}
//				if(checks.isEmpty()){
//					System.out.println("empty checks");
//					rules.remove(j);
//					j--;
//				} else {
//					break;
//				}	
//			}
//			if(rules.isEmpty()){
//				System.out.println("Satisfied");
//				return signatures.get(i).getResultingType();
//			}
//		}
//		System.out.println("Not Satisfied");
//		return caseframe.getSemanticClass();
//	}

}
