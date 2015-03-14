package sneps.Tests;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;

public class CFConflictsTests {
	
	private static Hashtable<String, CaseFrame> caseFrames = new Hashtable<String, CaseFrame>();
	
	public static LinkedList<CaseFrame> CheckCFConflicts(CaseFrame cf){
		if(caseFrames.containsKey(cf.getId())){
			return null;
		}
		// loop over all defined case frames
		Enumeration<CaseFrame> caseframes = caseFrames.elements();
		LinkedList<CaseFrame> result = new LinkedList<CaseFrame>();
		// looping on the case frames with supersets or subsets of relations
		while(caseframes.hasMoreElements()){
			CaseFrame cf1 = caseframes.nextElement();
			// get intersecting relations
			Hashtable<String, RCFP> intersection = getIntersectingRelations(cf1.getRelations(), cf.getRelations());
			// if no intersecting relations not conflicting so skip
			if(intersection.size() == 0){
				continue;
			}
			// check new case frame
			Enumeration<RCFP> relations = cf.getRelations().elements();
			boolean satisfied = true;
			while(relations.hasMoreElements()){
				RCFP r = relations.nextElement();
				if(intersection.containsKey(r.getRelation().getName()))
					continue;
				if(r.getLimit() != 0){
					satisfied = false;
					break;
				}
			}
			if(satisfied){
				// check other case frame
				Enumeration<RCFP> relations1 = cf1.getRelations().elements();
				boolean satisfied1 = true;
				while(relations1.hasMoreElements()){
					RCFP r = relations1.nextElement();
					if(intersection.containsKey(r.getRelation().getName()))
						continue;
					if(r.getLimit() != 0){
						satisfied1 = false;
						break;
					}
				}
				if(satisfied1){
					result.add(cf1);
				}
			}
		}
		return result;
	}
	
	private static Hashtable<String, RCFP> getIntersectingRelations
		(Hashtable<String, RCFP> list1, Hashtable<String, RCFP> list2){
		Enumeration<RCFP> relations = list1.elements();
		Hashtable<String, RCFP> result = new Hashtable<String, RCFP>();
		while(relations.hasMoreElements()){
			RCFP r = relations.nextElement();
			if(list2.containsKey(r.getRelation().getName())){
				result.put(r.getRelation().getName(), r);
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception{
		
		// creating 5 relations
		Relation r1 = Network.defineRelation("r1", "Entity", "none", 1);
		Relation r2 = Network.defineRelation("r2", "Entity", "none", 1);
		Relation r3 = Network.defineRelation("r3", "Entity", "none", 1);
		Relation r4 = Network.defineRelation("r4", "Entity", "none", 1);
		Relation r5 = Network.defineRelation("r5", "Entity", "none", 1);
		
		// creating RCFP list for CF1
		RCFP rp11 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp12 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		RCFP rp13 = Network.defineRelationPropertiesForCF(r3, "none", 1);
		RCFP rp14 = Network.defineRelationPropertiesForCF(r4, "none", 0);
		LinkedList<RCFP> relCF1 = new LinkedList<RCFP>();
		relCF1.add(rp11);
		relCF1.add(rp12);
		relCF1.add(rp13);
		relCF1.add(rp14);
		// creating CF1
		CaseFrame cf1 = Network.defineCaseFrame("Entity", relCF1);
		caseFrames.put(cf1.getId(), cf1);
		
		// creating RCFP list for CF2
		RCFP rp21 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp22 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
		relCF2.add(rp21);
		relCF2.add(rp22);
		// creating CF2
		CaseFrame cf2 = Network.defineCaseFrame("Entity", relCF2);
		caseFrames.put(cf2.getId(), cf2);
		
		// creating RCFP list for CF
		RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		RCFP rp3 = Network.defineRelationPropertiesForCF(r3, "none", 0);
		LinkedList<RCFP> relCF = new LinkedList<RCFP>();
		relCF.add(rp1);
		relCF.add(rp2);
		relCF.add(rp3);
		// creating CF
		CaseFrame cf = Network.defineCaseFrame("Entity", relCF);
		
		// creating RCFP list for CF3
		RCFP rp31 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp32 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		RCFP rp35 = Network.defineRelationPropertiesForCF(r5, "none", 1);
		LinkedList<RCFP> relCF3 = new LinkedList<RCFP>();
		relCF3.add(rp31);
		relCF3.add(rp32);
		relCF3.add(rp35);
		// creating CF3
		CaseFrame cf3 = Network.defineCaseFrame("Entity", relCF3);
		caseFrames.put(cf3.getId(), cf3);
		
		// creating RCFP list for CF3
		RCFP rp41 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp42 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		RCFP rp43 = Network.defineRelationPropertiesForCF(r3, "none", 0);
		RCFP rp45 = Network.defineRelationPropertiesForCF(r5, "none", 0);
		LinkedList<RCFP> relCF4 = new LinkedList<RCFP>();
		relCF4.add(rp41);
		relCF4.add(rp42);
		relCF4.add(rp43);
		relCF4.add(rp45);
		// creating CF3
		CaseFrame cf4 = Network.defineCaseFrame("Entity", relCF4);
		caseFrames.put(cf4.getId(), cf4);
		
		
		System.out.println("Number of caseframes defined in the system: " + caseFrames.size());
		// checking conflicts
		LinkedList<CaseFrame> list1 =  CheckCFConflicts(cf);
		System.out.println("Conflicting:" + list1.size());
		System.out.println(list1.get(0).getId());
		System.out.println(list1.get(1).getId());
		System.out.println(list1.get(2).getId());
		
//		System.out.println("NEWWWWWWWWWWW");
//		Hashtable<String, RCFP> t = getIntersectingRelations(cf1.getRelations(), cf2.getRelations());
//		System.out.println(t.size());
//		Enumeration<String> e = t.keys();
//		while(e.hasMoreElements()){
//			System.out.println(e.nextElement().toString());
//		}
//		Hashtable<String, RCFP> t1 = getIntersectingRelations(cf1.getRelations(), cf.getRelations());
//		System.out.println(t1.size());
//		Enumeration<String> e1 = t1.keys();
//		while(e1.hasMoreElements()){
//			System.out.println(e1.nextElement().toString());
//		}
	}

}
