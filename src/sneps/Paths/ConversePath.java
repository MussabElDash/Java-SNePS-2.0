/**
 * @className ConversePath.java
 * 
 * @ClassDescription A converse path exists between two nodes A and B, 
 * 	if node A can be reached after following the converse of the path 
 * 	specified in the converse path starting at B. This class extends 
 *  Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;

import SNeBR.Context;
import sneps.Node;
import sneps.PathTrace;

public class ConversePath extends Path{
	
	/**
	 * A path that its converse is to be followed. 
	 * This path can be of any type.
	 */
	private Path path;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param path
	 * 			the path defined in the converse path.
	 */
	public ConversePath(Path path){
		this.path = path;
	}
	
	/**
	 * 
	 * @return the path defined in the current converse
	 * 	path.
	 */
	public Path getPath(){
		return this.path;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		return this.path.followConverse(node, trace, context);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		return this.path.follow(node, trace, context);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public ConversePath clone() {
		return new ConversePath(this.path.clone());
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if(! obj.getClass().getSimpleName().equals("ConversePath"))
			return false;
		ConversePath conPath = (ConversePath) obj;
		if(! conPath.getPath().equals(this.path))
			return false;
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from the
	 * Object class.
	 */
	@Override
	public String toString(){
		return "Converse("+this.path.toString()+")";
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		return this.path;
	}

}
