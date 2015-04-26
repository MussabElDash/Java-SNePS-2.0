/**
 * @className Base.java
 * 
 * @ClassDescription A base term is the syntactic type of any node with no outgoing 
 * 	arcs; a node that does not dominate any other nodes. This class extends Term 
 *  class and has no more instance variables that those inherited from the super 
 *  class.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.SyntaticClasses;


public class Base extends Term{
	
	/**
	 * 
	 * @param identifier
	 * 			the name of the node that will be created.
	 */
	public Base(String identifier){
		super(identifier);
	}

}
