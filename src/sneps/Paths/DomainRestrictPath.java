/**
 * @className DomainRestrictPath.java
 * 
 * @ClassDescription A domain restrict path involves specifying 
 * 	two paths and a node, for example, path Q, path P and node N. 
 * 	The domain restrict path exists between two nodes A and B, 
 * 	if Q is a path from A to N and P is a path from A to B.
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

public class DomainRestrictPath extends Path {
	
	/**
	 *  A path that should exist between the starting node 
	 *  and the node that is specified in this domain restrict path.
	 *  This path can be of any type.
	 */
	private Path q;
	
	/**
	 * A node specified in the domain restrict path that should be 
	 * reachable if path Q is to be followed from the starting node.
	 */
	private Node zNode;
	
	/**
	 * A path that should be followed from the starting node to get all 
	 * the nodes reachable by this path, given that path Q is path from 
	 * the starting node to the node specified in this domain restrict path.
	 * This path can be of any type.
	 */
	private Path p;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param q
	 *  		the Q path.
	 * @param zNode
	 * 			the specified node.
	 * @param p
	 * 			the P path
	 */	
	public DomainRestrictPath(Path q, Node zNode, Path p){
		this.q = q;
		this.zNode = zNode;
		this.p = p;		
	}

	/**
	 * 
	 * @return the Q path of the current domain restrict path.
	 */
	public Path getQ(){
		return this.q;
	}
	
	/**
	 * 
	 * @return the node specified in the current domain restrict path.
	 */
	public Node getZNode(){
		return this.zNode;
	}
	
	/**
	 * 
	 * @return the P path of the current domain restrict path.
	 */
	public Path getP(){
		return this.p;
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		LinkedList<Object[]> temp = q.follow(node, trace, context);
		for (int i = 0; i < temp.size(); i++){
			Object[] o = temp.get(i);
			Node n = (Node) o[0];
			PathTrace pt = (PathTrace) o[1];
			if (n.equals(this.zNode)){
				trace.addAllSupports(pt.getSupports());
				result = p.follow(node, trace, context);
			}
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		return new RangeRestrictPath(new ConversePath(this.p),this.q,this.zNode).follow(node,trace,context);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public DomainRestrictPath clone() {
		return new DomainRestrictPath(this.q.clone(), this.zNode, this.p.clone());
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if(! obj.getClass().getSimpleName().equals("DomainRestrictPath"))
			return false;
		DomainRestrictPath dPath = (DomainRestrictPath) obj;
		if(! this.q.equals(dPath.getQ()))
			return false;
		if(! this.p.equals(dPath.getP()))
			return false;
		if(! this.zNode.equals(dPath.getZNode()))
			return false;
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from the Object
	 * class.
	 */
	@Override
	public String toString(){
		return "Domain-restrict(" + this.q.toString() + ", " + this.zNode.toString() + ", "
				+ this.p.toString() + ")";
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		return new RangeRestrictPath(p.converse(), q, zNode);
	}

}
