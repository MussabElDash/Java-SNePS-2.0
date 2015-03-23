/**
 * @className UpCableSet.java
 * 
 * @ClassDescription A up cable set is a set of up cables that a certain node has. 
 * 	The UpCableSet class is implemented as a 2-tuple (downCables and caseFrame).
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Cables;

import java.util.Enumeration;
import java.util.Hashtable;

import sneps.Relation;

public class UpCableSet {
	

	/**
	 * A hash table of the up cables that this up cable set includes. Each
	 * 	Entry in this hash table has the name of the relation of the up cable as
	 * 	the key and the corresponding up cable as the value.
	 */
	private Hashtable<String, UpCable> upCables;
	
	/**
	 * The constructor of this class. It initialize an empty hash table.
	 */
	public UpCableSet(){
		this.upCables = new Hashtable<String, UpCable>();
	}
	
	/**
	 * 
	 * @param upCable
	 * 			the new up cable that will be added to the current up cable set.
	 */
	public void addUpCable(UpCable upCable){
		this.upCables.put(upCable.getRelation().getName(), upCable);
	}
	
	/**
	 * 
	 * @param upCable
	 * 			the up cable that will be removed from the current up cable set.
	 */
	public void removeUpCable(UpCable upCable){
		this.upCables.remove(upCable.getRelation().getName());
	}
	
	/**
	 * 
	 * @param relationName
	 * 			The name of the relation that its related up cable is needed.
	 * 
	 * @return the up cable that has the specified relation and null if the 
	 * 	needed up cable does not exist.
	 */
	public UpCable getUpCable(String relationName){
		if(this.upCables.containsKey(relationName)){
			return this.upCables.get(relationName);
		}
		return null;
	}
	
	/**
	 * 
	 * @return the hash table of the up cables included in this 
	 * 	up cable set.
	 */
	public Hashtable<String, UpCable> getUpCables(){
		return this.upCables;
	}
	
	/**
	 * 
	 * @return true if the current up cable set is empty, and false 
	 * 	otherwise.
	 */
	public boolean isEmpty(){
		return this.upCables.isEmpty();
	}
	
	/**
	 * 
	 * @return the number of up cables included in this up cable set.
	 */
	public int size(){
		return this.upCables.size();
	}
	
	/**
	 * 
	 * @param relation
	 * 			the relation that will be used to check whether there is a
	 * 			up cable with this specified relation included in the current 
	 * 			up cable set.
	 * 
	 * @return true if an up cable with this specified relation exists, and false 
	 * 	otherwise. 
	 */
	public boolean contains(Relation relation){
		if(this.upCables.containsKey(relation.getName())){
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
    		Enumeration<UpCable> uCables = this.upCables.elements();
			int i = 0;
    		while(uCables.hasMoreElements()) {
				if(	i > 0){
					s += " ";
				}
				s += uCables.nextElement().toString();
              i++;
            }
            return s;
    }

}
