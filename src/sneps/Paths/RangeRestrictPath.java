/**
 * @className RangeRestrictPath.java
 * 
 * @ClassDescription The range restrict path involves specifying two 
 * 	paths and a node, for example, path Q, path P and node N. The range 
 * 	restrict path exists between two nodes A and B, if Q is a path from 
 * 	B to N and P is a path from A to B. This class extends Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;

import SNeBR.Context;
import sneps.Node;
import sneps.PathTrace;

public class RangeRestrictPath extends Path{
	
	/**
	 * a Path that should be followed to get all the reachable 
	 * nodes that have path Q as path from them to the node N 
	 * specified in this range restrict path.
	 * This path can be of any type.
	 */
	private Path p;
	
	/**
	 * A path that should exist between the node, that is reachable by 
	 * following path P from the starting node, and the node N specified 
	 * in this range restrict path.
	 * This path can be of any type.
	 */
	private Path q;
	
	/**
	 * The node (N) that represents the specified node in the current range 
	 * restrict path.
	 */
	private Node zNode;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param p
	 * 			the P path.
	 * @param q
	 * 			the Q path.
	 * @param node
	 * 			the specified node.
	 */
	public RangeRestrictPath(Path p, Path q, Node node){
		this.p = p;
		this.q = q;
		this.zNode = node;
	}
	
	/**
	 * 
	 * @return the P path of the current range restrict path.
	 */
	public Path getP(){
		return this.p;
	}
	
	/**
	 * 
	 * @return the Q path of the current range restrict path.
	 */
	public Path getQ(){
		return this.q;
	}
	
	/**
	 * 
	 * @return the node specified in the current range restrict path.
	 */
	public Node getNode(){
		return this.zNode;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		 LinkedList<Object[]> result = new LinkedList<Object[]>();
         LinkedList<Object[]> res = this.p.follow(node,trace,context);
         for(int i=0;i<res.size();i++)
         {
                 Object[] o = res.get(i);
                 Node n = (Node) o[0];
                 PathTrace pt = (PathTrace) o[1];
                 LinkedList<Object[]> temp = this.q.follow(n,pt,context);
                 for(int j=0;j<temp.size();j++)
                 {
                         Object[] ob = temp.get(j);
                         Node nt = (Node) ob[0];
                         PathTrace ptt = (PathTrace) ob[1];
                         if(nt.equals(this.zNode))
                         {
                                 Object[] r = new Object[2];
                                 r[0] = n;
                                 PathTrace ptrace = pt.clone();
                                 ptrace.addAllSupports(ptt.getSupports());
                                 r[1] = ptrace;
                                 result.add(r);
                         }

                        
                 }
         }
        
         return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		return new DomainRestrictPath(this.q,this.zNode,new ConversePath(this.p)).follow(node,trace,context);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public RangeRestrictPath clone() {
		return new RangeRestrictPath(this.p.clone(), this.q.clone(), this.zNode);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if(!obj.getClass().getSimpleName().equals("RangeRestrictPath"))
			return false;
		RangeRestrictPath rPath = (RangeRestrictPath) obj;
		if(! this.p.equals(rPath.getP()))
			return false;
		if(! this.q.equals(rPath.getQ()))
			return false;
		if(! this.zNode.equals(rPath.getNode()))
			return false;
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from the 
	 * Object class.
	 */
	@Override
	public String toString(){
		return "range-restrict("+this.p.toString()+", "+this.q.toString()+", "+this.zNode.toString()+")";

	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		return new DomainRestrictPath(q, zNode, p.converse());
	}

}
