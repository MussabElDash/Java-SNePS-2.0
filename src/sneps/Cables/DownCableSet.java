/**
 * @className DownCableSet.java
 * 
 * @ClassDescription A down cable set is a set of down cables that a certain node has. 
 * 	The DownCableSet class is implemented as a 2-tuple (downCables and caseFrame).
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Cables;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import sneps.CaseFrame;

public class DownCableSet {
	
	/**
	 * A hash table of the down cables that this down cable set includes. Each
	 * 	Entry in this hash table has the name of the relation of the down cable as
	 * 	the key and the corresponding down cable as the value.
	 */
	private Hashtable<String, DownCable> downCables;
	
	/**
	 * The case frame specified by this down cable set. Each down cable set has 
	 * 	a specific case frame that restricts the relations that can label the arcs 
	 * 	of the down cables included in this set.
	 */
	private CaseFrame caseFrame;
	
	/**
	 * 
	 * @param downCables
	 * 			a list of the down cables included in this set.
	 * 
	 * @param caseFrame
	 * 			the case frame specified by this down cable set.
	 */
	public DownCableSet(LinkedList<DownCable> downCables, CaseFrame caseFrame){
		this.downCables = generateDCablesHashtable(downCables);
		this.caseFrame = caseFrame;
	}
	
	/**
	 * This method generate a hash table of down cables from a given list of 
	 * 	down cables. Each entry in the generated hash table has the relation
	 * 	name as the key and the corresponding down cable as the value.
	 * 
	 * @param downCables
	 * 			the list of down cables included in this down cable set.
	 * 
	 * @return a hash table of the down cables included in thus down cable set.
	 */
	private Hashtable<String, DownCable> generateDCablesHashtable(LinkedList<DownCable> downCables){
		Hashtable<String, DownCable> dCables = new Hashtable<String, DownCable>();
		for(int i = 0; i < downCables.size(); i++){
			dCables.put(downCables.get(i).getRelation().getName(), downCables.get(i));
		}
		return dCables;
	}

	/**
	 * 
	 * @return the case frame specified by the current down cable set.
	 */
	public CaseFrame getCaseFrame(){
		return this.caseFrame;
	}
	
	/**
	 * 
	 * @param relationName
	 * 			a relation name that will be used to check whether this given 
	 * 			relation included in this down cable set.
	 * 
	 * @return the down cable that has the given relation and returns null if no 
	 * 	cable with this relation is included in this down cable set.
	 */
	public DownCable getDownCable(String relationName){
		if(this.downCables.containsKey(relationName)){
			return this.downCables.get(relationName);
		}
		return null;
	}
	
	/**
	 * 
	 * @return the number of down cables included in this down cable set.
	 */
	public int size(){
		return this.downCables.size();
	}
	
	/**
	 * 
	 * @return the hash table of the down cables included in this 
	 * 	down cable set.
	 */
	public Hashtable<String, DownCable> getDownCables(){
		return this.downCables;
	}

	/**
	 * 
	 * @param relationName
	 * 			the relation name that will be used to check whether there is a
	 * 			down cable with this specified relation included in the current 
	 * 			down cable set.
	 * 
	 * @return true if a down cable with this specified relation exists, and false 
	 * 	otherwise. 
	 */
	public boolean contains(String relationName){
		if(this.downCables.containsKey(relationName)){
			return true;
		}
		return false;
	}
	
	/**
	 * This method overrides the default toString method inherited from the Object class.
	 */
	@Override
    public String toString()
    {
            String s = "";
    		Enumeration<DownCable> dCables = this.downCables.elements();
    		int i = 0;
    		while(dCables.hasMoreElements()) {
    				if(i > 0){
    					 s += " ";
    				}
                    s += dCables.nextElement().toString();
                    i++;
            }
            return s;
    }
	
	
}
