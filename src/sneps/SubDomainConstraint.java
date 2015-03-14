/**
 * @className SubDomainConstraint.java
 * 
 * @ClassDescription A sub-domain constraint is a pair consisting of a relation and 
 * 	a list of constraints on the nodes that are pointed to by the arcs labeled with
 * 	this relation. This class is implemented as a 3-tuple (relation, nodeChecks and
 * 	id). 
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps;

import java.util.Collections;
import java.util.LinkedList;

import sneps.Paths.Path;

public class SubDomainConstraint {
	
	/**
	 * the name of the relation included in this sub-domain constraint.
	 */
	private String relation;
	
	/**
	 * a list of cableTypeConstraints specifying the constraints on the nodes pointed to 
	 * 	by the arcs labeled with the relation included in this sub-domain constraint. 
	 */
	private LinkedList<CableTypeConstraint> nodeChecks;
	
	/**
	 * the string id of the current sub-domain constraint.
	 */
	private String id;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param relName
	 * 			the name of the relation included in this sub-domain constraint.
	 * @param checks
	 * 			the list of constraints specified by this sub-domain constraint.
	 */
	public SubDomainConstraint(String relName, LinkedList<CableTypeConstraint> checks){
		this.relation = relName;
		this.nodeChecks = checks;
		this.id = generateId();
	}
	
	/**
     * @return the name of the relation included in the current sub-domain constraint.
     */
	public String getRelation(){
		return this.relation;
	}
	
	/**
     * @return the list constraints specified by the current sub-domain constraint.
     */
	public LinkedList<CableTypeConstraint> getNodeChecks(){
		return this.nodeChecks;
	}
	
	/**
     * @return the string id of the current sub-domain constraint.
     */
	public String getId(){
		return this.id;
	}
	
	/**
     * This method is invoked from the constructor of this class to generate the id of
     * 	the newly created sub-domain constraint.
     */
	private String generateId(){
		id = "";
		LinkedList<String> types = new LinkedList<String>();
		for(int i = 0; i < nodeChecks.size(); i++){
			types.add(nodeChecks.get(i).getSemanticType());
		}
		Collections.sort(types);
		for(int i = 0; i < types.size(); i++){
			for (int j = 0; j < nodeChecks.size(); j++){
				if(nodeChecks.get(j).getSemanticType().equals(types.get(i))){
					id += nodeChecks.get(j).getId();
					if(i < nodeChecks.size()-1)
						id += "-";
				}
			}
		}
		return id;
	}
	
//////////////////////////////////////main method that was used for testing //////////////////////////////////////
	
//	public static void main(String[] args){
//		CableTypeConstraint cs = new CableTypeConstraint("Entity", null, null);
//		CableTypeConstraint cs2 = new CableTypeConstraint("Number", 0, null);
//		CableTypeConstraint cs3 = new CableTypeConstraint("Int", 2, 2);
//		System.out.println("Mini-IDs ................ ");
//		System.out.println(cs.getId());
//		System.out.println(cs2.getId());
//		System.out.println(cs3.getId());
//		CableTypeConstraint[] x = new CableTypeConstraint[]{cs,cs2,cs3};
//		LinkedList<CableTypeConstraint> list = new LinkedList<CableTypeConstraint>(java.util.Arrays.asList(x));
//		SubDomainConstraint sdc = new SubDomainConstraint("+", list);
//		System.out.println(sdc.getId());
//	}

}
