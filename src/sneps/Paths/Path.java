/**
 * @className Path.java
 * 
 * @ClassDescription A path in SNePS is a sequence of n-arcs 
 * 	with some constraints on the arcs included in the path and
 * 	on the way they are connected to form the path. This class
 * 	is an abstract class and it is the super class of all other 
 * 	types of paths.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;


import java.util.LinkedList;

import sneps.PathTrace;
import sneps.Relation;
import sneps.Nodes.Node;
import SNeBR.Context;

public abstract class Path {
	
	/**
	 * This method follows the current path starting at the given 
	 * node in the given context and adds the paths followed along 
	 * with their supports to the given path trace.
	 * 
	 * @param node
	 * 			the node that the current path will be followed
	 * 			starting at it.
	 * 
	 * @param trace
	 * 			the path trace representing the trace of following
	 * 			the current path.
	 * 
	 * @param context
	 * 			the context that the propositions in this path
	 * 			is asserted in.
	 * 
	 * @return a linked list of objects' arrays. Each object array is a pair
	 * 	that contains a node resulted from following the path along 
	 * 	with the path trace that was followed till reaching this node.
	 */
	 public abstract LinkedList<Object[]> follow(Node node,PathTrace trace,Context context);
	 
	/**
	 * This method follows the converse of current path starting  
	 * at the given node in the given context and adds the paths  
	 * followed along with their supports to the given path trace.
	 * 
	 * @param node
	 * 			the node that the converse of the current path 
	 * 			will be followed starting at it.
	 * 
	 * @param trace
	 * 			the path trace representing the trace of following
	 * 			the converse of the current path.
	 * 
	 * @param context
	 * 			the context that the propositions in this path
	 * 			is asserted in.
	 * 
	 * @return a linked list of objects' arrays. Each object array is a pair
	 * 	that contains a node resulted from following the converse of the path along 
	 * 	with the path trace that was followed till reaching this node.
	 */
	 public abstract LinkedList<Object[]> followConverse(Node node,PathTrace trace,Context context);
	 
	 /**
	  * This method overrides the clone method inherited from
	  * the Object class.
	  */
	 @Override
	 public abstract Path clone();
	 
	 /**
	  * This method overrides the equals method inherited from
	  * the Object class.
	  */
	 @Override
	 public abstract boolean equals(Object obj);
	 
	 /**
	  * This method is used to get the converse of the current
	  * path.
	  * 
	  * @return a path representing the converse of the current
	  *   path.
	  */
	 public abstract Path converse();
	 
	 public abstract LinkedList<Relation> firstRelations();
	 
}
