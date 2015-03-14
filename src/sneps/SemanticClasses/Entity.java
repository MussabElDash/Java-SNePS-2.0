/**
 * @className Entity.java
 * 
 * @ClassDescription This is the class that represents the super class of 
 * all the semantic classes in this implementation of SNePS.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */

package sneps.SemanticClasses;

import java.util.LinkedList;

public class Entity {

	/**
     * gets a list of the simple names of the classes representing 
     * 	the super classes of the current semantic class
     *
     * @return a linked list of strings representing the simple names of the semantic 
     * 	super classes of the current semantic class.
     */
	public LinkedList<String> getSuperClassesNames(){	
		LinkedList<String> superClasses = new LinkedList<String>();
		getSuperClasses (this.getClass(), superClasses);
		return superClasses;		
	}
	
	/**
     * updates the list of the simple names of the classes representing 
     * 	the super classes of certain semantic class by adding the simple 
     * 	names of the super classes of a given semantic class
     *
     * @param c
     * 			the semantic class that the current method will get its super classes.
     * @param superClasses
     * 			the linked list of strings representing the simple names of the super classes 
     * 				of the given semantic class passed to the current method as a parameter.
     * 
     * @return a linked list of strings representing the simple names of the
     * 	super classes of the current semantic class.
     */
	@SuppressWarnings("rawtypes")
	private void getSuperClasses(Class c,LinkedList<String> superClasses)
	{
	        Class superClass = c.getSuperclass();
	        if(superClass == null)
	                return;
	        else
	        {
	                superClasses.add(superClass.getSimpleName());
	                getSuperClasses(superClass,superClasses);
	        }
	}
	

}
