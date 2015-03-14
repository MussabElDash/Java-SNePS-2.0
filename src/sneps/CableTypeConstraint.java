/**
 * @className CableTypeConstraint.java
 * 
 * @ClassDescription A cable type constraint can be roughly described as a triplet 
 * 	of a semantic type, a lower limit and an upper limit. For example, if a sub-domain 
 * 	constraint of relation R1 has <Individual, 2, 4> in its set of cable type constraints, 
 * 	then relation R1 should at least point to two nodes having Individual as their semantic 
 * 	type and at most to four nodes having Individual as their semantic type to satisfy this 
 * 	cable type constraint. A sub-domain constraint is said to be satisfied if all its 
 * 	cable type constraints were satisfied.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps;

public class CableTypeConstraint {
	
	/**
	 * The name of the semantic class representing the semantic type 
	 * specified by this cable type constraint.
	 */
	private String semanticType;
	
	/**
	 * The minimum number of nodes that should be having the semantic 
	 * 	type specified by this cable type constraint and having the arcs 
	 * 	labeled with the relation specified in the sub-domain constraint 
	 * 	pointing to them. 
	 * 
	 * If (min = null), then all the nodes, pointed to by the arcs labeled
	 * 	with the relation specified in the sub-domain constraint, should
	 * 	have the semantic type specified by this cable type constraint.
	 */
	private Integer min;
	
	/**
	 * The maximum number of nodes that should be having the semantic type 
	 * 	specified by this cable type constraint and having the arcs labeled 
	 * 	with the relation specified in the sub-domain constraint pointing to 
	 * 	them. 
	 * 
	 * If (max = null), then no maximum limit is specified.
	 */
	private Integer max;
	
	/**
	 * The id of the current cable type constraint.
	 */
	private String id;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param semantic
	 * 			the semantic type specified in the cable type constraint.
	 * @param minimum
	 * 			the minimum number of nodes specified in the cable type
	 * 			constraint. 
	 * @param maximum
	 * 			the maximum number of nodes specified in the cable type
	 * 			constraint.
	 */
	public CableTypeConstraint(String semantic, Integer minimum, Integer maximum){
		this.semanticType = semantic;
		this.min = minimum;
		this.max = maximum;
		this.id = semantic + "," + minimum + "," + maximum;
	}
	

	/**
	 * 
	 * @return the semantic type specified in the current cable type constraint.
	 */
	public String getSemanticType(){
		return this.semanticType;
	}
	
	/**
	 * 
	 * @return the lower limit specified in the current cable type constraint.
	 */
	public Integer getLowerLimit(){
		return this.min;
	}
	
	/**
	 * 
	 * @return the upper limit specified in the current cable type constraint.
	 */
	public Integer getUpperLimit(){
		return this.max;
	}
	
	/**
	 * 
	 * @return the ID of the current cable type constraint.
	 */
	public String getId(){
		return this.id;
	}
	
//////////////////////////////////////the main method that was used for testing //////////////////////////////////////////
	
//	public static void main(String[] args){
//		CableTypeConstraint cs = new CableTypeConstraint("Int", null, null);
//		CableTypeConstraint cs2 = new CableTypeConstraint("Int", 0, null);
//		CableTypeConstraint cs3 = new CableTypeConstraint("Int", 2, 2);
//		System.out.println(cs.id);
//		System.out.println(cs2.id);
//		System.out.println(cs3.id);
//	}
}
