/**
 * @className IrreflexiveRestrictPath.java
 * 
 * @ClassDescription An Irreflexive restrict path exists between two 
 * 	nodes A and B, if the path defined in the irreflexive restrict 
 * 	path is a path from node A to node B and A is not equal to B.
 * 	This class extends Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;

import SNeBR.Context;
import sneps.PathTrace;
import sneps.Nodes.Node;

public class IrreflexiveRestrictPath extends Path {
	
	/**
	 * The path defined in the current irreflexive restrict path.
	 */
	private Path path;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param path
	 * 			the path defined in the irreflexive restrict path.
	 */
	public IrreflexiveRestrictPath(Path path){
		this.path = path;
	}
	
	/**
	 * 
	 * @return the path defined in the current irreflexive restrict path.
	 */
	public Path getPath(){
		return this.path;
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
	
		LinkedList<Object[]> result = this.path.follow(node, trace, context);
		for (int i = 0; i < result.size(); i++){
			if(((Node)result.get(i)[0]).equals(node)){
				result.remove(i);
				i--;
			}
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		
		LinkedList<Object[]> result = this.path.followConverse(node, trace, context);
		for (int i = 0; i < result.size(); i++){
			if(((Node)result.get(i)[0]).equals(node)){
				result.remove(i);
				i--;
			}
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public IrreflexiveRestrictPath clone() {
		return new IrreflexiveRestrictPath(this.path.clone());
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().getSimpleName().equals("IrreflexiveRestrictPath"))
			return false;
		IrreflexiveRestrictPath irPath = (IrreflexiveRestrictPath) obj;
		if (!this.path.equals(irPath.getPath()))
			return false;
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from the 
	 * Object class.
	 */
	@Override
	public String toString(){
		return "Irreflexive-restrict(" + this.path.toString() + ")";
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		return new IrreflexiveRestrictPath(this.path.converse());
	}

}
