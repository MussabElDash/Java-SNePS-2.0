/**
 * @className RCFP.java
 * 
 * @ClassDescription RCFP stands for Relation Case Frame Properties. This class is 
 * 	a 3-tuple that consists of a relation and its related adjustability and limit 
 * 	constraints that are to be applied within a certain case frame. 
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps;

public class RCFP{

	/**
	 * The relation included in this 3-tuple (Relation, adjust and limit).
	 */
	private Relation relation;
	
	/**
	 * The specified adjustability of the relation in the current RCFP. This adjustability is to be 
	 * 	applied and override the default adjustability of the relation within a certain case frame.
	 */
	private String adjust;
	
	/**
	 * The specified limit of the relation in the current RCFP. This limit is to be 
	 * 	applied and override the default limit of the relation within a certain case frame.
	 */
	private int limit;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param relation
	 * 			the Relation included in the current RCFP
	 * @param adjust
	 * 			the adjustability defined for the relation within
	 * 			the current RCFP.
	 * @param limit
	 * 			the limit defined for the relation within
	 * 			the current RCFP.
	 */
	public RCFP(Relation relation, String adjust, int limit){
		this.relation = relation;
		this.adjust = adjust;
		this.limit = limit;
	}
	
	/**
     * @return the adjustability defined for the relation within the current RCFP.
     */
	public String getAdjust() {
		return adjust;
	}

	/**
     * @return the limit defined for the relation within the current RCFP.
     */
	public int getLimit() {
		return limit;
	}

	/**
     * @return the Relation included in the current RCFP.
     */
	public Relation getRelation() {
		return relation;
	}
	
	/**
	 * This method overrides the default toString method inherited from the Object class.
	 */
	@Override
	public String toString(){
		String s = "(";
		s += "relation:" + this.relation + ", ";
		s += "adjust:" + this.adjust + ", ";
		s += "limit:" + this.limit;
		s += ")";
		return s;
	}

}
