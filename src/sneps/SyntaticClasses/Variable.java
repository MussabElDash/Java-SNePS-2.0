/**
 * @className Variable.java
 * 
 * @ClassDescription A variable term is the syntactic type of any node that represents 
 * 	a variable entity and has no outgoing arcs. This class extends Term 
 *  class and has no more instance variables that those inherited from the super 
 *  class.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.SyntaticClasses;


public class Variable extends Term {
	
	/**
	 * 
	 * @param identifier
	 * 			the name of the node that will be created.
	 */
	public Variable(String identifier){
		super(identifier);
	}
	
}
