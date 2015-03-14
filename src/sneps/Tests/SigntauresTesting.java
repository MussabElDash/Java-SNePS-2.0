package sneps.Tests;

import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Network;
import sneps.RCFP;
import sneps.Relation;

public class SigntauresTesting {

	public static void main(String[] args) throws Exception{
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
		
		// creating RCFP list for CF2
		RCFP rp21 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp22 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		LinkedList<RCFP> relCF2 = new LinkedList<RCFP>();
		relCF2.add(rp21);
		relCF2.add(rp22);
		// creating CF2
		CaseFrame cf2 = Network.defineCaseFrame("Entity", relCF2);
		
		
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
		
		// creating RCFP list for CF4
		RCFP rp41 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp42 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		RCFP rp43 = Network.defineRelationPropertiesForCF(r3, "none", 0);
		RCFP rp45 = Network.defineRelationPropertiesForCF(r5, "none", 0);
		LinkedList<RCFP> relCF4 = new LinkedList<RCFP>();
		relCF4.add(rp41);
		relCF4.add(rp42);
		relCF4.add(rp43);
		relCF4.add(rp45);
		// creating CF4
		CaseFrame cf4 = Network.defineCaseFrame("Entity", relCF4);
		
		// creating RCFP list for CF
		RCFP rp1 = Network.defineRelationPropertiesForCF(r1, "none", 1);
		RCFP rp2 = Network.defineRelationPropertiesForCF(r2, "none", 1);
		RCFP rp3 = Network.defineRelationPropertiesForCF(r3, "none", 0);
		LinkedList<RCFP> relCF = new LinkedList<RCFP>();
		relCF.add(rp1);
		relCF.add(rp2);
		relCF.add(rp3);
		// creating CF
		CaseFrame cf = new CaseFrame("Entity", relCF);
		
		
		// checking conflicts
		LinkedList<CaseFrame> list1 =  Network.CheckCFConflicts(cf);
		printConflictingCaseframes(list1, "cf");
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
			System.out.println(cfList.get(i).getId());
			
		}
	}
}
