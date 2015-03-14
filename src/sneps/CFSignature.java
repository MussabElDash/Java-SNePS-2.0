/**
 * @className CFSignature.java
 * 
 * @ClassDescription CFSignature stands for Case Frame Signature. The case frame signature 
 * 	is a list of constraints on the semantic type and number of nodes that are to be pointed 
 * 	to by the relations included in the case frame having this signature and a resulting 
 * 	semantic type. This class is a 3-tuple (resultingType, sdConstraints and id). 
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps;

import java.util.LinkedList;

public class CFSignature {
	
	/**
	 * a string specifying the name of the resulting semantic class that will replace the
	 * 	default semantic class specified by the case frame if all the constraints specified
	 * 	in the current case frame signature were satisfied.
	 */
	private String resultingType;
	
	/**
	 * a linked list of constraints on the semantic type and number of nodes pointed to 
	 * 	be some or all the relations included in the case frame that has this CFSignature.
	 */
	private LinkedList<SubDomainConstraint> sdConstraints;
	
	/**
	 * a string id for the current case frame signature.
	 */
	private String id;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param result
	 * 			the name of the resulting semantic class specified by the 
	 * 			case frame signature.
	 * @param rules
	 * 			the list of constraints specified by the case frame signature.
	 * @param caseframeId
	 * 			the string id of the case frame having this case frame signature.
	 * 			(The case frame id will be used in generating the id  of this 
	 * 			case frame signature).
	 */
	public CFSignature(String result, LinkedList<SubDomainConstraint> rules, String caseframeId){
		this.resultingType = result;
		this.sdConstraints = rules;
		generateId(caseframeId);
	}
	
	/**
     * @return the name of the semantic class specified by the current 
     * 	case frame signature.
     */
	public String getResultingType(){
		return this.resultingType;
	}
	
	/**
     * @return the linked list of constraints specified by the current
     * 	case frame signature.
     */
	public LinkedList<SubDomainConstraint> getSubDomainConstraints(){
		return this.sdConstraints;
	}
	
	/**
     * @return the string id of the current case frame signature.
     */
	public String getId(){
		return this.id;
	}
	
	/**
     * This method is invoked from the constructor of this class to generate the id of
     * 	the newly created case frame signature.
     */
	private void generateId(String caseframeId){
		String s = "";
		String[] relations = caseframeId.split(",");
		for(int i = 0; i < relations.length; i++){
			boolean found = false;
			for(int j = 0; j < sdConstraints.size(); j++){
				if(sdConstraints.get(j).getRelation().equals(relations[i])){
					found = true;
					s += sdConstraints.get(j).getId();
				}
			}
			if(!found)
				s += "*";
			if(i < relations.length -1)
				s += ";";
			if(i == relations.length -1)
				s += "/" + this.resultingType;
		}
		this.id = s;
	}
	
////////////////////////////////////// main method that was used for testing //////////////////////////////////////
	
//	public static void main(String[] args){
//
//		CableTypeConstraint cs = new CableTypeConstraint("Entity", null, null);
//		CableTypeConstraint cs2 = new CableTypeConstraint("Number", 0, null);
//		CableTypeConstraint cs3 = new CableTypeConstraint("Int", 2, 2);
//		System.out.println("Mini-IDs ................ ");
//		System.out.println(cs.getId());
//		System.out.println(cs2.getId());
//		System.out.println(cs3.getId());
//		CableTypeConstraint[] x = new CableTypeConstraint[]{cs,cs2,cs3};
//		LinkedList<CableTypeConstraint> list = new LinkedList<CableTypeConstraint>(java.util.Arrays.asList(x));
//		SubDomainConstraint sdc = new SubDomainConstraint("add", list);
//		System.out.println(sdc.getId());
//		CableTypeConstraint cs4 = new CableTypeConstraint("Individual", null, null);
//		CableTypeConstraint cs5 = new CableTypeConstraint("Float", 0, null);
//		CableTypeConstraint cs6 = new CableTypeConstraint("Real", 0, 2);
//		System.out.println("Mini-IDs ................ ");
//		System.out.println(cs4.getId());
//		System.out.println(cs5.getId());
//		System.out.println(cs6.getId());
//		CableTypeConstraint[] x1 = new CableTypeConstraint[]{cs4,cs5,cs6};
//		LinkedList<CableTypeConstraint> list1 = new LinkedList<CableTypeConstraint>(java.util.Arrays.asList(x1));
//		SubDomainConstraint sdc1 = new SubDomainConstraint("subtract", list1);
//		System.out.println(sdc1.getId());
//		SubDomainConstraint[] s = new SubDomainConstraint[]{sdc,sdc1};
//		LinkedList<SubDomainConstraint> sdCons = new LinkedList<SubDomainConstraint>(java.util.Arrays.asList(s));
//		CFSignature sig = new CFSignature("Number", sdCons,"add,minus,subtract,zstar" );
////		sig.generateId("add,minus,subtract,zstar");
//		System.out.println("Sidnature id ........... ");
//		System.out.println(sig.getId());
//		String[] rules = sig.getId().split("/");
//		System.out.println("after: " + sig.getId());
//		System.out.println("1st split");
//		System.out.println(rules[0]);
//		System.out.println(rules[1]);
//		System.out.println("2nd split");
//		String[] subdomains = rules[0].split(";");
//		System.out.println(subdomains[0]);
//		System.out.println(subdomains[1]);
//		System.out.println(subdomains[2]);
//		System.out.println(subdomains[3]);
//		System.out.println("3rd split");
//		String[] ctype = subdomains[0].split("-");
//		String[] ctype1 = subdomains[1].split("-");
//		String[] ctype2 = subdomains[2].split("-");
//		String[] ctype3 = subdomains[3].split("-");
//		System.out.println("1st subdomain");
//		if(! ctype[0].equals("*")){
//			System.out.println(ctype[0]);
//			System.out.println(ctype[1]);
//			System.out.println(ctype[2]);
//		} else{
//			System.out.println("*");
//		}
//		System.out.println("2nd subdomain");
//		if(! ctype1[0].equals("*")){
//			System.out.println(ctype1[0]);
//			System.out.println(ctype1[1]);
//			System.out.println(ctype1[2]);
//		} else{
//			System.out.println("*");
//		}
//	}

}