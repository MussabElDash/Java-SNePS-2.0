/**
 * @className Closed.java
 * 
 * @ClassDescription A closed term is the syntactic type of any molecular 
 * 	node that does not dominate any free variables. This class extends the 
 * 	Molecular class and has no more instance variables than those 
 * 	inherited from the super classes.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.SyntaticClasses;

import sneps.Cables.DownCableSet;

public class Closed extends Molecular {
	
	/**
	 * The constructor of this class.
	 * 
	 * @param identifier
	 * 			the name of the node that will be created.
	 * @param downCableSet
	 * 			the down cable set of the node that will be
	 * 			created.
	 */
	public Closed(String identifier, DownCableSet downCableSet){
		super(identifier, downCableSet);
	}

}
